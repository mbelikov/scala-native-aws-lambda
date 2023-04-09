package dev.migapril.aws
package lambda

import dev.migapril.aws.lambda.model.Order

object OrderStore {
  def store(order: Order): Either[Error, Unit] =
    Right(
      Console
        .out
        .println(
          s"Stored: $order"
        )
    )
}
