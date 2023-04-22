package dev.migapril.aws.lambda

trait Logging {
  def logError(message: => String): Unit =
    Console.err.println(message)

  def logInfo(message: => String): Unit =
    Console.out.println(message)
}
