package dev.migapril
package aws
package lambda

import java.util.UUID

import squants.Money

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
 *
 * The aim is to use Scala's type safety for data definitions and use only data libs supporting Scala native.
 * Following libs support Scala native:
 *
 *   - refined (since 0.10.1)
 *   - squants (since 1.8.0)
 *   - circe (since 0.14.3)
 *
 * Following libs don't support Scala native:
 *
 *   - newtype (s. https://github.com/estatico/scala-newtype/pull/61 & https://github.com/marcesquerra/scala-newtype/tree/AddScalaNative)
 *   - derevo (s. https://github.com/tofu-tf/derevo/issues/309)
 */
object model extends DeriveCirce with DeriveCats {
  case class Quantity(value: Int) extends AnyVal

  case class ItemId(value: UUID) extends AnyVal

  case class OrderId(value: UUID) extends AnyVal

  case class PaymentId(value: UUID) extends AnyVal

  case class Order(
                    id: OrderId,
                    paymentId: PaymentId,
                    items: Map[ItemId, Quantity],
                    total: Money,
                  )
}
