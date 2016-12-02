import sbt._

object Settings {
  val version = "0.0.1"

  object versions {
    val scala = "2.12.0"
    val scalazCore = "7.2.7"
    val fastParse = "0.4.2"
    val fs2 = "0.9.2"
  }

  val scalacOptions = Seq(
    "-Xlint",
    "-unchecked",
    "-deprecation",
    "-feature"
  )

  val runnerDeps = Def.setting(Seq(
    "org.scalaz" %% "scalaz-core" % versions.scalazCore
  ))

  val solutionDeps = Def.setting(Seq(
    "com.lihaoyi" %% "fastparse" % versions.fastParse,
    "co.fs2" %% "fs2-core" % versions.fs2,
    "co.fs2" %% "fs2-io" % versions.fs2
  ))
}