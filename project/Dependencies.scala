import sbt._
import org.portablescala.sbtplatformdeps.PlatformDepsPlugin.autoImport._
import sbt.Keys.libraryDependencies

object Version {
  val circe = "0.14.5"
  val derevo = "0.13.0"
  val monocle = "3.2.0"
  val newtype = "0.4.4"
  val refined = "0.10.2"
  val squants = "1.8.3"
  val tofu = "0.11.1"

  val logback = "1.4.5"
  val slf4j = "2.0.2"

  val `scala-native-crypto` = "0.0.4"
  val sttp = "3.8.14"
}

object Dependencies {
  def derevo(artifact: String): ModuleID = "tf.tofu" %% s"derevo-$artifact" % Version.derevo

  def circe(artifact: String): ModuleID = "io.circe" %% s"circe-$artifact" % Version.circe

  val squants = "org.typelevel" %% "squants" % Version.squants
  val refined = "eu.timepit" %% "refined" % Version.refined
  val `refined-cats` = "eu.timepit" %% "refined-cats" % Version.refined
  val newtype = "io.estatico" %% "newtype" % Version.newtype

  val `derevo-core` = derevo("core")
  val `derevo-cats` = derevo("cats")
  val `derevo-circe-magnolia` = derevo("circe-magnolia")
  val `derevo-pureconfig` = derevo("pureconfig")
  val `tofu-zio-core` = "tf.tofu" %% "tofu-zio-core" % Version.tofu

  val `circe-core` = circe("core")
  val `circe-generic` = circe("generic")
  val `circe-parser` = circe("parser")
  val `circe-refined` = circe("refined")

  val `monocle-core` = "dev.optics" %% "monocle-core" % Version.monocle

  val `java-logback-core` = "ch.qos.logback" % "logback-core" % Version.logback
  val `java-logback-classic` = "ch.qos.logback" % "logback-classic" % Version.logback
  val `java-slf4j` = "org.slf4j" % "slf4j-api" % Version.slf4j

  val baseDependencies = Seq(libraryDependencies ++= {
    def derevo(artifact: String): ModuleID = "tf.tofu" %%% s"derevo-$artifact" % Version.derevo

    def circe(artifact: String): ModuleID = "io.circe" %%% s"circe-$artifact" % Version.circe

    Seq(
      "eu.timepit" %%% "refined" % Version.refined,
      // no scala native versions:
      //      "eu.timepit" %%% "refined-cats" % Version.refined,
      //      "io.estatico" %%% "newtype" % Version.newtype,
      "org.typelevel" %%% "squants" % Version.squants,
      //      derevo("core"),
      //      derevo("cats"),
      //      derevo("circe-magnolia"),
      //      derevo("pureconfig"),
      circe("core"),
      circe("generic"),
      circe("parser"),
      circe("refined"),
      "dev.optics" %%% "monocle-core" % Version.monocle,
      "org.typelevel" %%% "kittens" % "3.0.0",

      // no scala native versions:
      //      refined,
      //      `refined-cats`,
      //      newtype,
      //      squants,

      //      `derevo-core`,
      //      `derevo-cats`,
      //      `derevo-circe-magnolia`,
      //      `circe-core`,
      //      `circe-generic`,
      //      `circe-refined`,
      //      `monocle-core`,
    )
  })

  val nativeAdditionalDependencies = Seq(libraryDependencies ++= {
    Seq(
      // for Scala native's java.security.SecureRandom,
      // s. https://github.com/scala-native/scala-native/issues/2600#issuecomment-1242191978
      // you should install openssl to use it (e.g. on macOS: `brew install openssl` or on Debian: `sudo apt install libssl-dev`)
      // s. also https://scala-native.org/en/stable/lib/javalib.html#supported-classes
      "com.github.lolgab" %%% "scala-native-crypto" % Version.`scala-native-crypto`
    )
  })

  val serviceDependencies = Seq(libraryDependencies ++= {
    Seq(
      "com.softwaremill.sttp.client3" %%% "core" % Version.sttp
    )
  })

  val baseTestDependencies = Seq(libraryDependencies ++= {
    Seq(
      "com.eed3si9n.expecty" %% "expecty" % "0.16.0",
      "com.github.alexarchambault" %% "scalacheck-shapeless_1.16" % "1.3.1",
      "org.scalacheck" %% "scalacheck" % "1.17.0",
      "org.scalatest" %% "scalatest" % "3.2.15",
      "org.scalatestplus" %% "scalacheck-1-17" % "3.2.15.0",
      "org.typelevel" %% "discipline-scalatest" % "2.2.0",
    ).map(_ % Test)
  })

  // compiler plugins
  val `better-monadic-for` = "com.olegpy" %% "better-monadic-for" % "0.3.1"
  val `context-applied` = "org.augustjune" %% "context-applied" % "0.1.4"
  val `kind-projector` = "org.typelevel" %% "kind-projector" % "0.13.2" cross CrossVersion.full

  val `organize-imports` = "com.github.liancheng" %% "organize-imports" % "0.6.0"
}
