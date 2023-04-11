package dev.migapril.aws.lambda

object lambda {
  /** AWS request for our lambda.
   *
   * @param payload         payload containing Order json.
   * @param deadlineMs      from AWS header 'Lambda-Runtime-Deadline-Ms': Function execution deadline counted in milliseconds since the Unix epoch.
   * @param requestId       from AWS header 'Lambda-Runtime-Aws-Request-Id': AWS request ID associated with the request.
   * @param traceId         from AWS header 'Lambda-Runtime-Trace-Id': X-Ray tracing header.
   * @param clientContext   from AWS header 'Lambda-Runtime-Client-Context': Information about the client application and device when invoked through the AWS Mobile SDK.
   * @param cognitoIdentity from AWS header 'Lambda-Runtime-Cognito-Identity': Information about the Amazon Cognito identity provider when invoked through the AWS Mobile SDK.
   * @param functionArn     from AWS header 'Lambda-Runtime-Invoked-Function-Arn': The ARN requested. This can be different in each invoke that executes the same version.
   */
  final case class RequestData(
                                payload: String,
                                deadlineMs: Long,
                                requestId: String,
                                traceId: String,
                                clientContext: String,
                                cognitoIdentity: String,
                                functionArn: String,
                              )

  /** Response data toward AWS.
   *
   * @param code    integer response code following HTTP semantic.
   * @param message optional message to inform clients about invocation status.
   */
  final case class ResponseData(
                                 code: Int,
                                 message: String,
                               )

  object ResponseData {
    import io.circe.Codec
    import io.circe.generic.semiauto._

    implicit val responseDataCodec: Codec[ResponseData] = deriveCodec
  }
}
