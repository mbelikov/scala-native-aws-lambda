package dev.migapril.aws.lambda

import dev.migapril.aws.lambda.model.{ItemId, Order}
import io.circe.{Codec, Decoder, Encoder, KeyDecoder, KeyEncoder}
import io.circe.generic.semiauto._
import shapeless.Unwrapped

trait DeriveCirce {
  implicit def encodeAnyVal[W, U](
                                   implicit
                                   ev: W <:< AnyVal,
                                   unwrapped: Unwrapped.Aux[W, U],
                                   unwrappedEncoder: Encoder[U],
                                 ): Encoder[W] =
    Encoder.instance[W](v => unwrappedEncoder(unwrapped.unwrap(v)))

  implicit def decodeAnyVal[W, U](
                                   implicit
                                   ev: W <:< AnyVal,
                                   unwrapped: Unwrapped.Aux[W, U],
                                   unwrappedDecoder: Decoder[U],
                                 ): Decoder[W] = unwrappedDecoder.map(unwrapped.wrap)

  implicit val itemsKeyEncoder: KeyEncoder[ItemId] =
    KeyEncoder.encodeKeyUUID.contramap(itemId => itemId.value)
  implicit val itemsKeqDecoder: KeyDecoder[ItemId] =
    KeyDecoder.decodeKeyUUID.map(uuid => ItemId(uuid))

  implicit val orderCodec: Codec[Order] = deriveCodec
}
