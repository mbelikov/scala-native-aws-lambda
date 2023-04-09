package dev.migapril.aws
package lambda

import scala.util._

import sttp.client3
import sttp.client3._

case class Error(code: Int, cause: String)

object HttpOps {
  implicit class SttpShortCuts(private val client: SimpleHttpClient) extends AnyVal {
    def get(url: String): Response[Either[String, String]] =
      client.send(client3.basicRequest.get(uri"$url"))

    def post(url: String, data: String): Response[Either[String, String]] =
      client.send(client3.basicRequest.post(uri"$url").body(data))
  }

  def header[T: StringParser](in: Response[Either[String, String]], h: String): Either[Error, T] =
    in.header(h) match {
      case Some(stringValue) => StringParser[T].parse(stringValue)
      case None => Left(Error(400, s"Bad request: missing header '$h'"))
    }

  def optionalHeader[T: StringParser : DefaultValue](
                                                      in: Response[Either[String, String]],
                                                      h: String,
                                                    ): Either[Error, T] =
    in.header(h) match {
      case Some(stringValue) => StringParser[T].parse(stringValue)
      case None => Right(DefaultValue[T].value())
    }

  trait StringParser[T] {
    def parse(s: String): Either[Error, T]
  }

  object StringParser {
    def apply[T](implicit parser: StringParser[T]): StringParser[T] = parser

    implicit val stringStringParser: StringParser[String] = (s: String) => Right(s)

    implicit val longStringParser: StringParser[Long] = (s: String) =>
      Try(s.toLong) match {
        case Failure(exception) =>
          Left(Error(400, s"Bad request: cannot parse '$s' as Long: ${exception.getMessage}"))
        case Success(value) => Right(value)
      }
  }

  trait DefaultValue[T] {
    def value(): T
  }

  object DefaultValue {
    def apply[T](implicit instance: DefaultValue[T]): DefaultValue[T] = instance

    implicit val defaultStringValue: DefaultValue[String] = () => "-"
  }
}
