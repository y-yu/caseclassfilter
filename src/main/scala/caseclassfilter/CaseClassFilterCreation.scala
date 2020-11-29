package caseclassfilter


trait CaseClassFilterCreation {
  implicit class FromA[A](private val a: A) {
    def filterFieldType[B](
      implicit f: CaseClassFilter[A, B]
    ): Seq[B] = f.filterFieldType(a)
  }
}