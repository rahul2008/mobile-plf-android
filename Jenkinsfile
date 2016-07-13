node('Android') {
    stage 'Checkout'
    checkout scm

    stage 'Build'
    sh 'cd ./Source/DICommClientSample && ./gradlew assembleDebug'

    stage 'Unit test'
    sh 'cd ./Source/DICommClientSample && ./gradlew testDebugUnitTest || true'
}