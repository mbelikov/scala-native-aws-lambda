import Dependencies._
import MyUtil.excl

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
    .aggregate(domain_model.jvm, service, scala_native_service, graalvm_native_service)
    .settings(publish / skip := true)

lazy val domain_model =
  crossProject(JVMPlatform, NativePlatform)
    .in(file(s"./modules/$domain"))
    .settings(name := s"${`scala-native-aws-lambda`}-$domain")
    .settings(commonSettings)
    .settings(baseDependencies)
    .settings(baseTestDependencies)
    .nativeSettings(nativeAdditionalDependencies)

lazy val service =
  project
    .in(file(s"./modules/$srvc"))
    .settings(name := s"${`scala-native-aws-lambda`}-$srvc")
    .settings(commonSettings)
    .settings(baseTestDependencies)
    .settings(serviceDependencies)
    .dependsOn(domain_model.jvm)

lazy val scala_native_service =
  project
    .enablePlugins(ScalaNativePlugin)
    .in(file(s"./modules/${`scala-native`}"))
    .settings(name := s"${`scala-native-aws-lambda`}-${`scala-native`}")
    .settings(scalaNativeSettings)
    .settings(baseDependencies)
    .settings(nativeAdditionalDependencies)
    .settings(baseTestDependencies)
    .settings(serviceDependencies)
    .settings(Compile / sources := (service / Compile / sources).value)
    .dependsOn(domain_model.native)

lazy val graalvm_native_service =
  project
    .enablePlugins(GraalVMNativeImagePlugin)
    .in(file(s"./modules/${`graalvm-native`}"))
    .settings(name := s"${`scala-native-aws-lambda`}-${`graalvm-native`}")
    .settings(graalvmNativeSettings)
    .settings(serviceDependencies)
    .settings(Compile / sources := (service / Compile / sources).value)
    .dependsOn(domain_model.jvm)

lazy val commonSettings = {
  lazy val commonCompilerPlugins = Seq(
    addCompilerPlugin(`better-monadic-for`),
    addCompilerPlugin(`context-applied`),
    addCompilerPlugin(`kind-projector`),
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

lazy val scalaNativeSettings = {
  // import to add Scala Native options
  import scala.scalanative.build._

  nativeConfig ~= { c =>
    c.withLTO(LTO.none) // thin
      .withMode(Mode.debug) // releaseFast
      .withGC(GC.immix) // commix
      .withLinkingOptions(c.linkingOptions :+ "-L/opt/homebrew/Cellar/openssl@3/3.1.0/lib")
  }
}

lazy val graalvmNativeSettings =
// graalvm native image configuration
  graalVMNativeImageOptions ++= Seq(
    "--no-fallback", // build a standalone image or report a failure
    "--install-exit-handlers",
    "--enable-http",
    "-H:+StaticExecutableWithDynamicLibC",
    "-H:+RemoveSaturatedTypeFlows",
    "--link-at-build-time",
    "--report-unsupported-elements-at-runtime",
    "--verbose",
    "-O1",
  )
