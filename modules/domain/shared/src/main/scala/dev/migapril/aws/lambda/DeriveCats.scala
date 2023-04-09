package dev.migapril.aws.lambda

import cats.Show
import cats.derived.cached.showPretty._
import cats.kernel.Eq

import dev.migapril.aws.lambda.model.Order

trait DeriveCats {
  implicit val orderShow: Show[Order] = Show[Order]
  implicit val orderEq: Eq[Order] = Eq[Order]
}
