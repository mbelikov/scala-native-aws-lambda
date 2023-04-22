package dev.migapril.aws
package lambda

import dev.migapril.aws.lambda.model.Order

object OrderStore extends Logging {
  def store(order: Order): Either[Error, Unit] = Right(logInfo(s"Stored: $order"))
}
