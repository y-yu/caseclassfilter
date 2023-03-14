lazy val root = (project in file("."))
  .settings(
    organization := "com.github.y-yu",
    name := "caseclassfilter",
    version := "0.1",
    description := "Case class fields filter by the type",
    homepage := Some(url("https://github.com/y-yu")),
    licenses := Seq("MIT" -> url(s"https://github.com/y-yu/caseclassfilter/blob/master/LICENSE")),
    scalaVersion := "2.13.4",
    scalacOptions ++= Seq(
      "-deprecation",
      "-encoding", "UTF-8",
      "-Xlint",
      "-language:implicitConversions", "-language:higherKinds", "-language:existentials",
      "-unchecked"
    ),
    libraryDependencies ++= Seq(
      "com.chuusai" %% "shapeless" % "2.3.3",
      "org.scalatest" %% "scalatest" % "3.2.15" % "test",
    )
  )