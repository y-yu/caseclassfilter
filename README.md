Case class filter by type 
==================================

![CI](https://github.com/y-yu/caseclassfilter/workflows/CI/badge.svg)

## Abstract

`CaseClassFilter` can extract case class field values that match
the specified type via `filterFieldType`'s type parameter.

```scala
import caseclassfilter.CaseClassFilter._

case class A(
  a: String,
  b: Int,
  c: Double,
  d: Seq[String]
)

val strings = A(
  a = "This will be filtered in.\n",
  b = 1,
  c = 3.14,
  d = Seq("These", "would", "be", "collected", "too", "\n")
).filterFieldType[String]

println(strings.mkString(" "))
// Output:
//   This will be filtered in.
//    These would be collected too
```

## See also

- [狙った型を持つフィールドの値を網羅的に集めるScalaマクロ](https://zenn.dev/articles/c4d53a534f45e1/) (Japanese)