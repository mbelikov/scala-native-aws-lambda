package dev.migapril.aws

import cats.kernel.Eq
import cats.{Monoid, Show}
import io.circe.{Decoder, Encoder}
import squants.Money
import squants.market.{Currency, USD}

package object lambda extends OrphanInstances

trait OrphanInstances {
  implicit val moneyDecoder: Decoder[Money] =
    Decoder[BigDecimal].map(USD.apply)

  implicit val moneyEncoder: Encoder[Money] =
    Encoder[BigDecimal].contramap(_.amount)

  implicit val moneyMonoid: Monoid[Money] =
    new Monoid[Money] {
      def empty: Money = USD(0)

      def combine(x: Money, y: Money): Money = x + y
    }

  implicit val currencyEq: Eq[Currency] =
    Eq.and(Eq.and(Eq.by(_.code), Eq.by(_.symbol)), Eq.by(_.name))

  implicit val moneyEq: Eq[Money] = Eq.and(Eq.by(_.amount), Eq.by(_.currency))

  implicit val moneyShow: Show[Money] = Show.fromToString
}
