package dev.migapril
package aws
package lambda

import io.circe.parser.parse
import io.circe.syntax.EncoderOps
import io.circe.{DecodingFailure, ParsingFailure}
import sttp.client3._

import dev.migapril.aws.lambda.HttpOps._
import dev.migapril.aws.lambda.lambda._

object Main {
  def main(args: Array[String]): Unit = {
    val runtimeApiHost = sys.env("AWS_LAMBDA_RUNTIME_API")
    handleEvents(runtimeApiHost)

    // to demonstrate how scala native compilation is working
    doJavaCode()
  }

  private val httpClient = SimpleHttpClient()

  // Receive and handle events infinitely
  def handleEvents(runtimeApiHost: String): Unit = {
    val nextEventUrl = getNextEventUrl(runtimeApiHost)
    val responseUrl: String => String = getResponseUrl(runtimeApiHost)

    while (true)
      getRequestData(nextEventUrl) match {
        case Left(requestError) => logError(s"Cannot receive request: $requestError")
        case Right(requestData) => handleRequestData(requestData, responseUrl)
      }
  }

  private def handleRequestData(requestData: RequestData, responseUrl: String => String) = (
    for {
      order <- parseRequestString(requestData.payload)
      _ <- handleOrder(order, requestData)
    } yield order
    ).fold(
    error => {
      logError(s"Error handling request: $error")
      postLambdaResponse(responseUrl(requestData.requestId))(
        ResponseData(code = error.code, message = error.cause)
      )
    },
    success =>
      postLambdaResponse(responseUrl(requestData.requestId))(
        ResponseData(
          code = 200,
          message = s"Successfully handled for id: ${success.id.value}",
        )
      ),
  )

  private def getRequestData(nextEventUrl: String) =
    for {
      tuple <- getLambdaRequest(nextEventUrl)
      (requestResponse, requestStr) = tuple
      deadlineMs <- header[Long](requestResponse, "lambda-runtime-deadline-ms")
      requestId <- header[String](requestResponse, "lambda-runtime-aws-request-id")
      traceId <- header[String](requestResponse, "Lambda-Runtime-Trace-Id")
      clientContext <- optionalHeader[String](requestResponse, "Lambda-Runtime-Client-Context")
      cognitoIdentity <- optionalHeader[String](requestResponse, "Lambda-Runtime-Cognito-Identity")
      functionArn <- optionalHeader[String](requestResponse, "Lambda-Runtime-Invoked-Function-Arn")
    } yield RequestData(
      payload = requestStr,
      deadlineMs = deadlineMs,
      requestId = requestId,
      traceId = traceId,
      clientContext = clientContext,
      cognitoIdentity = cognitoIdentity,
      functionArn = functionArn,
    )

  private def getLambdaRequest(nextEventUrl: String) = {
    val requestResponse = httpClient.get(nextEventUrl)
    fromSttpResponse(requestResponse).map((requestResponse, _))
  }

  private def fromSttpResponse(response: Response[Either[String, String]]): Either[Error, String] =
    response.body match {
      case Left(cause) => Left(Error(response.code.code, cause))
      case Right(value) => Right(value)
    }

  private def parseRequestString(requestStr: String): Either[Error, model.Order] = {
    for {
      json <- parse(requestStr)
      order <- json.as[model.Order]
    } yield order
  }.left.map {
    case ParsingFailure(message, _) => Error(400, s"Bad request: $message")
    case failure: DecodingFailure => Error(400, s"Bad request: ${failure.message}")
  }

  private def postLambdaResponse(responseUrl: String)(responseData: ResponseData) =
    httpClient.post(responseUrl, data = io.circe.Printer.noSpaces.print(responseData.asJson))

  private def handleOrder(
                           order: model.Order,
                           requestData: RequestData,
                         ): Either[Error, Unit] =
    Right {
      Console
        .out
        .println(
          s"Received: $order, requestId: ${requestData.requestId}, deadlineMs: ${requestData.deadlineMs}"
        )
      OrderStore.store(order)
    }

  private def getNextEventUrl(host: String) =
    s"http://$host/2018-06-01/runtime/invocation/next"

  private def getResponseUrl(host: String)(requestId: String) =
    s"http://$host/2018-06-01/runtime/invocation/$requestId/response"

  private def doJavaCode() = {
    // java code
    //    val _ = this.getClass.getConstructors
    //    val cd: javax.management.openmbean.CompositeData = ???
    //    val _ = java.lang.management.MonitorInfo.from(cd)
  }

  private def logError(message: String): Unit =
    Console.err.println(message)
}
