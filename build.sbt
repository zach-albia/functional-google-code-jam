name := "functional-google-code-jam"

version := "1.0"

lazy val runner = (project in file("runner"))
  .settings(
    name := "runner",
    version := Settings.version,
    scalaVersion := Settings.versions.scala,
    scalacOptions := Settings.scalacOptions,
    libraryDependencies := Settings.runnerDeps.value
  ).dependsOn(solutions)

lazy val solutions = (project in file("solutions"))
  .settings(
    name := "solutions",
    version := Settings.version,
    scalaVersion := Settings.versions.scala,
    scalacOptions := Settings.scalacOptions,
    libraryDependencies := Settings.solutionDeps.value
  )
    