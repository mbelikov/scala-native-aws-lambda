# scala-native-aws-lambda

> **_STATUS:_** wip
> - [x] building & executable creation automation
> - [ ] logging
> - [ ] redis lib integration
> - [ ] AWS SQS libs integration

Spike about how the

- scala-native and
- graalvm-native-image

could be both used for AWS lambdas implementation with Scala.
The code base is shared between the builds.

The need of the scala-native support limits the choice of the used Scala libraries: not every Scala library by far
provides a scala-native build.

## Compilation

    sbt compile

## Test

    sbt test

## Build

The project has an alias to build both scala-native and graalvm natime image flavours of the lambda:

    sbt build-all

It is still possible to build them separately.

NB: the builds are only possible for the same target CPU architecture and OS where the build is running.

### Scala native build

    sbt nativeLink

### Graalvm native image build

Ensure you have graalvm tools in the path like

    export PATH="$PATH:/path-to-graalvm/Contents/Home/bin"

and run

    sbt "graalvm_native_service / GraalVMNativeImage / packageBin"
