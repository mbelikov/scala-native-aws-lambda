package dev.migapril.aws.lambda

import derevo.cats._
import derevo.circe.magnolia._
import derevo.derive
import io.estatico.newtype.macros.newtype
import squants.Money
import optics.uuid

import java.util.UUID

/** Model class definitions with auto derived decoder, encoder, show etc. type classes.
 *
 * It could be used like this:
 *
 * <pre>
 * import model._
 * import io.circe.syntax._
 * import cats.implicits.toShow
 *
 * def randUUID = UUID.randomUUID()
 *
 * val order = Order(
 * OrderId(randUUID),
 * PaymentId(randUUID),
 * Map(ItemId(randUUID) -> Quantity(5), ItemId(randUUID) -> Quantity(3)),
 * USD(33),
 * )
 *
 * println {
 * order.asJson
 * }
 *
 * println {
 * order.show
 * }
 * </pre>
 */
object model {
  @derive(decoder, encoder, eqv, show)
  @newtype
  case class Quantity(value: Int)

  @derive(decoder, encoder, keyDecoder, keyEncoder, eqv, show, uuid)
  @newtype
  case class ItemId(value: UUID)

  @derive(decoder, encoder, eqv, show)
  @newtype
  case class OrderId(value: UUID)

  @derive(decoder, encoder, eqv, show)
  @newtype
  case class PaymentId(value: UUID)

  @derive(decoder, encoder, eqv, show)
  case class Order(
                    id: OrderId,
                    paymentId: PaymentId,
                    items: Map[ItemId, Quantity],
                    total: Money,
                  )
}
