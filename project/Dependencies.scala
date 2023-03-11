import sbt._

object Version {
  val circe = "0.14.2"
  val derevo = "0.13.0"
  val monocle = "3.1.0"
  val newtype = "0.4.4"
  val refined = "0.10.2"
  val squants = "1.8.3"
  val tofu = "0.10.3"
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

  object com {
    object eed3si9n {
      object expecty {
        val expecty =
          "com.eed3si9n.expecty" %% "expecty" % "0.16.0"
      }
    }

    object github {
      object alexarchambault {
        val `scalacheck-shapeless_1.16` =
          "com.github.alexarchambault" %% "scalacheck-shapeless_1.16" % "1.3.1"
      }

      object liancheng {
        val `organize-imports` =
          "com.github.liancheng" %% "organize-imports" % "0.6.0"
      }
    }

    object olegpy {
      val `better-monadic-for` =
        "com.olegpy" %% "better-monadic-for" % "0.3.1"
    }
  }

  object org {
    object augustjune {
      val `context-applied` =
        "org.augustjune" %% "context-applied" % "0.1.4"
    }

    object scalacheck {
      val scalacheck =
        "org.scalacheck" %% "scalacheck" % "1.17.0"
    }

    object scalatest {
      val scalatest =
        "org.scalatest" %% "scalatest" % "3.2.15"
    }

    object scalatestplus {
      val `scalacheck-1-16` =
        "org.scalatestplus" %% "scalacheck-1-17" % "3.2.15.0"
    }

    object typelevel {
      val `discipline-scalatest` =
        "org.typelevel" %% "discipline-scalatest" % "2.2.0"

      val `kind-projector` =
        "org.typelevel" %% "kind-projector" % "0.13.2" cross CrossVersion.full
    }
  }
}
