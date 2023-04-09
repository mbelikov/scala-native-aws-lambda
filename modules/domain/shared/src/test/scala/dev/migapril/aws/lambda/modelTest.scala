package dev.migapril.aws.lambda

import java.util.UUID

import org.scalatest.funspec.AnyFunSpec

class modelTest extends AnyFunSpec {
  describe("Derived model")(
    it("circe decoder / encoder should work for json") {
      import model._
      import io.circe.syntax._
      import io.circe.Json
      import squants.market.MoneyConversions._

      def randUUID = UUID.randomUUID()

      def randItemId = ItemId(randUUID)

      val orderId = OrderId(randUUID)
      val paymentId = PaymentId(randUUID)
      val items = Map(randItemId -> Quantity(5), randItemId -> Quantity(3))

      val order = Order(
        orderId,
        paymentId,
        items,
        33.USD,
      )

      val orderJson = Json.obj(
        "id" -> orderId.value.toString.asJson,
        "paymentId" -> paymentId.value.toString.asJson,
        "items" -> items.map {
          case (itemId, quantity) => itemId.value.toString -> quantity.value.asJson
        }.asJson,
        "total" -> 33.USD.asJson,
      )

      assert(
        order.asJson === orderJson
      )

      assert(orderJson.as[Order] === Right(order))
    }
  )
}
