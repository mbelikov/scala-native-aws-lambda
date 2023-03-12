package dev.migapril.aws.lambda

import dev.migapril.aws.lambda.model.{ItemId, Order, OrderId, PaymentId, Quantity}
import io.circe.{Codec, KeyDecoder}
import io.circe.generic.semiauto._

trait DeriveCirce {
  implicit val quantityCodec: Codec[Quantity] = deriveCodec[Quantity]
  implicit val itemIdCodec: Codec[ItemId] = deriveCodec[ItemId]
  implicit val orderIdCodec: Codec[OrderId] = deriveCodec[OrderId]
  implicit val paymentIdCodec: Codec[PaymentId] = deriveCodec[PaymentId]
  implicit val itemIdKeyDecoder: KeyDecoder[ItemId] = ???
  implicit val orderCodec: Codec[Order] = deriveCodec[Order]
}
