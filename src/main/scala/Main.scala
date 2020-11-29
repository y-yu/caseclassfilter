import caseclassfilter.CaseClassFilter._

object Main {
  case class A(
    a: String,
    b: Int,
    c: Double,
    d: Seq[String]
  )

  def main(args: Array[String]): Unit = {
    val strings = A(
      a = "This will be filtered in.\n",
      b = 1,
      c = 3.14,
      d = Seq("These", "would", "be", "collected", "too", "\n")
    ).filterFieldType[String]

    println(strings.mkString(" "))
  }
}
