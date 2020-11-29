package caseclassfilter

trait CaseClassFilter[A, B] {
  def filterFieldType(a: A): Seq[B]
}

object CaseClassFilter
  extends CaseClassFilterInstances
  with CaseClassFilterCreation
