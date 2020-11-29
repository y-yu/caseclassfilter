package caseclassfilter

import org.scalatest.diagrams.Diagrams
import org.scalatest.flatspec.AnyFlatSpec
import CaseClassFilter._

class CaseClassFilterTest extends AnyFlatSpec with Diagrams {
  private trait Target

  "CaseClassFilter" should "get the fields that is specified its type" in {
    case class Dummy(
      a: String,
      b: Target,
      c: Double,
      d: Seq[Target]
    )

    val actual = Dummy(
      a = "dummy",
      b = new Target{},
      c = 3.14,
      d = Seq(new Target{}, new Target{})
    ).filterFieldType[Target]

    assert(actual.length === 3)
  }

  it should "return empty if the case class doesn't have `Target`" in {
    case class Dummy(
      a: String,
      b: Double
    )

    val actual = Dummy(
      a = "dummy",
      b = 3.14
    ).filterFieldType[Target]


    assert(actual.isEmpty)
  }

  it should "look forward `Target` from the nested case class" in {
    case class Dummy1(
      a: String,
      b: Target,
      c: Dummy2
    )
    case class Dummy2(
      d: Target,
      e: Double
    )
    val actual = Dummy1(
      a = "dummy",
      b = new Target {},
      c = Dummy2(
        d = new Target {},
        e = 3.14
      )
    ).filterFieldType[Target]

    assert(actual.length === 2)
  }

  it should "look forward subtype of `Target`" in {
    case class SubTarget() extends Target
    case class Dummy(
      a: String,
      b: SubTarget
    )
    val actual = Dummy(
      a = "dummy",
      b = SubTarget()
    ).filterFieldType[Target]

    assert(actual.length === 1)
  }

  it should "throw a compile error if the case class has a `Float` field" in {
    case class Dummy(
      a: String,
      b: Float
    )

    assertDoesNotCompile(
      """Dummy("a", (3.14).toFloat).filterFieldType[Target]""".stripMargin
    )
  }
}
