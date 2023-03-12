package dev.migapril
package aws
package lambda

import java.util.UUID

object Main {
  def main(args: Array[String]): Unit = {
    println("─" * 100)

    println("hello world")

    // java code
    StoreOrder.store(
      model.Order(
        model.OrderId(UUID.randomUUID()),
        model.PaymentId(UUID.randomUUID()),
        Map.empty,
        squants.market.EUR(55),
      )
    )

    println("─" * 100)
  }
}
