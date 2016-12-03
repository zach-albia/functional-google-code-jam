import sbt._

object Settings {
  val version = "0.0.1"

  object versions {
    val fastParse = "0.4.2"
    val fs2 = "0.9.2"
    val scala = "2.12.0"
    val scalacheck = "1.13.4"
    val scalazCore = "7.2.7"
  }

  val scalacOptions = Seq(
    "-Xlint",
    "-unchecked",
    "-deprecation",
    "-feature"
  )

  val libraryDependencies = Def.setting(Seq(
    "com.lihaoyi" %% "fastparse" % versions.fastParse,
    "co.fs2" %% "fs2-core" % versions.fs2,
    "co.fs2" %% "fs2-io" % versions.fs2,
    // test
    "org.scalacheck" %% "scalacheck" % versions.scalacheck % "test"
  ))
}