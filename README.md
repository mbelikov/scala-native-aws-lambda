# scala-native-aws-lambda

Spike how the scala-native and graalvm-native-image could be used for creation AWS lambdas with Scala

## Compilation

    sbt compile

## Test

    sbt test

## Scala native build

    sbt nativeLink

## Graalvm native image build

Ensure you have graalvm tools in the path like

    export PATH="$PATH:/path-to-graalvm/Contents/Home/bin"

and run

    sbt "graalvm_native_service / GraalVMNativeImage / packageBin"
