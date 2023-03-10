import Dependencies._

ThisBuild / organization := "dev.migapril"
ThisBuild / scalaVersion := "2.13.10"

val `scala-native-aws-lambda` = "scala-native-aws-lambda"
val domain = "domain"
val srvc = "service"

lazy val root =
  project
    .in(file("."))
    .settings(name := `scala-native-aws-lambda`)
    .aggregate(domain_model, service)
    .settings(publish / skip := true)

lazy val domain_model =
  project
    .in(file(s"./modules/$domain"))
    .settings(name := s"${`scala-native-aws-lambda`}-$domain")
    .settings(commonSettings)
    .settings(baseDependencies)

lazy val service =
  project
    .in(file(s"./modules/$srvc"))
    .settings(name := s"${`scala-native-aws-lambda`}-$srvc")
    .settings(commonSettings)
    .settings(baseDependencies)
    .settings(serviceDependencies)
    .dependsOn(domain_model)

lazy val commonSettings = {
  lazy val commonCompilerPlugins = Seq(
    addCompilerPlugin(com.olegpy.`better-monadic-for`),
    addCompilerPlugin(org.augustjune.`context-applied`),
    addCompilerPlugin(org.typelevel.`kind-projector`),
  )

  lazy val commonScalacOptions = Seq(
    Compile / console / scalacOptions := {
      (Compile / console / scalacOptions)
        .value
        .filterNot(_.contains("wartremover"))
        .filterNot(Scalac.Lint.toSet)
        .filterNot(Scalac.FatalWarnings.toSet) :+ "-Wconf:any:silent"
    },
    Test / console / scalacOptions :=
      (Compile / console / scalacOptions).value,
  )

  lazy val otherCommonSettings = Seq(
    update / evictionWarningOptions := EvictionWarningOptions.empty
  )

  Seq(
    commonCompilerPlugins,
    commonScalacOptions,
    otherCommonSettings,
  ).reduceLeft(_ ++ _)
}

lazy val baseDependencies = Seq(
  libraryDependencies ++= Seq(
    // main domain libs
  ),
  libraryDependencies ++= Seq(
    com.eed3si9n.expecty.expecty,
    com.github.alexarchambault.`scalacheck-shapeless_1.16`,
    org.scalacheck.scalacheck,
    org.scalatest.scalatest,
    org.scalatestplus.`scalacheck-1-16`,
    org.typelevel.`discipline-scalatest`,
  ).map(_ % Test),
)

lazy val serviceDependencies = Seq(
  libraryDependencies ++= Seq(
    // additional service main libs
  )
//  libraryDependencies ++= Seq(
//    // additional service test libs
//  ).map(_ % Test),
)
