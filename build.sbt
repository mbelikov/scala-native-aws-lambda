import Dependencies._

ThisBuild / organization := "dev.migapril"
ThisBuild / scalaVersion := "2.13.10"

val `scala-native-aws-lambda` = "scala-native-aws-lambda"
val domain = "domain"
val srvc = "service"
val `scala-native` = "scala-native-service"
val `graalvm-native` = "graalvm-native-service"

lazy val root =
  project
    .in(file("."))
    .settings(name := `scala-native-aws-lambda`)
    .aggregate(domain_model, service, scala_native_service, graalvm_native_service)
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

lazy val scala_native_service =
  project
    .enablePlugins(ScalaNativePlugin)
    .in(file(s"./modules/${`scala-native`}"))
    .settings(name := s"${`scala-native-aws-lambda`}-${`scala-native`}")
    .settings(scalaNativeSettings)
    .settings(
      excludeDependencies ++= Seq(excl(com.github.alexarchambault.`scalacheck-shapeless_1.16`))
    )
    .settings(Compile / sources := (service / Compile / sources).value)
    .dependsOn(domain_model)

lazy val graalvm_native_service =
  project
    .enablePlugins(GraalVMNativeImagePlugin)
    .in(file(s"./modules/${`graalvm-native`}"))
    .settings(name := s"${`scala-native-aws-lambda`}-${`graalvm-native`}")
    .settings(graalvmNativeSettings)
    .settings(
      excludeDependencies ++= Seq(excl(com.github.alexarchambault.`scalacheck-shapeless_1.16`))
    )
    .settings(Compile / sources := (service / Compile / sources).value)
    .dependsOn(domain_model)

import sbt.librarymanagement.InclExclRule

def excl(m: ModuleID): InclExclRule = InclExclRule(m.organization, m.name)

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
    refined,
    `refined-cats`,
    newtype,
    squants,
    `derevo-core`,
    `derevo-cats`,
    `derevo-circe-magnolia`,
    `circe-core`,
    `circe-generic`,
    `circe-refined`,
    `monocle-core`,
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

lazy val scalaNativeSettings = {
  // import to add Scala Native options
  import scala.scalanative.build._

  nativeConfig ~= { c =>
    c.withLTO(LTO.none) // thin
      .withMode(Mode.debug) // releaseFast
      .withGC(GC.immix) // commix
  }
}

lazy val graalvmNativeSettings =
// graalvm native image configuration
  graalVMNativeImageOptions ++= Seq(
    //    "--allow-incomplete-classpath",
    "--no-fallback", // build a standalone image or report a failure
    "--link-at-build-time",
    "--report-unsupported-elements-at-runtime",
    "--verbose",
    "-O1",
  )
