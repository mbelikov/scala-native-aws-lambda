package dev.migapril.aws.lambda

import cats.implicits.toShow
import dev.migapril.aws.lambda.model.Order

object StoreOrder {
  def store(order: Order): Unit =
  //    val logger: org.slf4j.Logger = org.slf4j.LoggerFactory.getLogger("hello")
  //    logger.info("world for")
  //    logger.info(order.show)

    println(order.show)
}
