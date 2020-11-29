package caseclassfilter

import java.net.URI
import shapeless.:+:
import shapeless.::
import shapeless.CNil
import shapeless.Coproduct
import shapeless.Generic
import shapeless.HList
import shapeless.HNil
import shapeless.Inl
import shapeless.Inr
import shapeless.Lazy
import scala.annotation.unused

trait CaseClassFilterInstances extends CaseClassFilterLowPriorityInstances {

  implicit def pureFilterInstance[A, B <: A]: B CaseClassFilter A =
    (a: A) => Seq(a)

  implicit def hNilInstance[A]: HNil CaseClassFilter A =
    (_: HNil) => Nil

  implicit def hConsInstance1[H, T <: HList, A](implicit
    head: H CaseClassFilter A,
    tail: T CaseClassFilter A
  ): CaseClassFilter[H :: T, A] =
    (a: H :: T) => head.filterFieldType(a.head) ++ tail.filterFieldType(a.tail)

  implicit def hListInstance[A, B, L <: HList](implicit
    gen: Generic.Aux[A, L],
    hList: Lazy[L CaseClassFilter B]
  ): A CaseClassFilter B =
    (a: A) => hList.value.filterFieldType(gen.to(a))

  implicit def instanceCNil[A]: CNil CaseClassFilter A =
    (_: CNil) => Nil

  implicit def instanceCCons[H, T <: Coproduct, A](implicit
    inl: H CaseClassFilter A,
    inr: T CaseClassFilter A
  ): CaseClassFilter[H :+: T, A] = {
    case Inl(head) => inl.filterFieldType(head)

    case Inr(tail) => inr.filterFieldType(tail)
  }

  implicit def instanceCoproduct[A, B, C <: Coproduct](implicit
    gen: Generic.Aux[A, C],
    coproduct: Lazy[C CaseClassFilter B]
  ): A CaseClassFilter B =
    (a: A) => coproduct.value.filterFieldType(gen.to(a))

  implicit def optionInstance[A, B](implicit
    pureFilter: A CaseClassFilter B
  ): CaseClassFilter[Option[A], B] =
    (a: Option[A]) => a.map(pureFilter.filterFieldType).getOrElse(Nil)

  implicit def seqInstance[A, B](implicit
    pureFilter: A CaseClassFilter B
  ): CaseClassFilter[Seq[A], B] =
    (a: Seq[A]) => a.flatMap(pureFilter.filterFieldType)

  implicit def setInstance[A, B](implicit
    pureFilter: A CaseClassFilter B
  ): CaseClassFilter[Set[A], B] =
    (a: Set[A]) => a.toSeq.flatMap(pureFilter.filterFieldType)

  implicit def mapInstance[A, B](implicit
    pureFilterA: CaseClassFilter[Seq[A], B],
    pureFilterB: CaseClassFilter[Seq[B], B]
  ): CaseClassFilter[Map[A, B], B] =
    (a: Map[A, B]) => pureFilterA.filterFieldType(a.keys.toSeq) ++ pureFilterB.filterFieldType(a.values.toSeq)

  implicit def eitherInstances[A, B, C](implicit
    pureFilterA: A CaseClassFilter C,
    pureFilterB: B CaseClassFilter C
  ): CaseClassFilter[A Either B, C] = {
    case Left(a) => pureFilterA.filterFieldType(a)

    case Right(b) => pureFilterB.filterFieldType(b)
  }
}

/**
  * There are instances for the types which don't have other types.
  */
trait CaseClassFilterLowPriorityInstances {

  // Types in the list is end to search.
  type EmptyTypeList =
    Int :: Long :: Boolean :: Double :: String :: URI :: Null :: HNil

  /**
    * Type level list `contains`.
    *
    * @tparam L HList
    * @tparam A The type to be found
    */
  trait Contains[L <: HList, A]

  object Contains {
    implicit def head[L <: HList, A]: ::[A, L] Contains A =
      new Contains[::[A, L], A] {}

    implicit def tail[L <: HList, A, B](implicit
      @unused EV: L Contains A
    ): ::[B, L] Contains A = new Contains[::[B, L], A] {}
  }

  implicit def emptyInstance[A, B](implicit
    @unused EV: EmptyTypeList Contains A
  ): A CaseClassFilter B =
    (_: A) => Nil
}

