package caseclassfilter

/**
  * This is a type class that means we can extract `B` from `A`.
  *
  * @tparam A type to be look forward for
  * @tparam B type to be extracted
  */
trait CaseClassFilter[A, B] {
  def filterFieldType(a: A): Seq[B]
}

object CaseClassFilter
  extends CaseClassFilterInstances
  with CaseClassFilterCreation
