import Dependencies._

ThisBuild / scalafixScalaBinaryVersion := scalaBinaryVersion.value
ThisBuild / scalafixDependencies ++= Seq(
  `organize-imports`
)

ThisBuild / semanticdbEnabled := true
ThisBuild / semanticdbVersion := scalafixSemanticdb.revision
