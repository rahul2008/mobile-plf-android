node('Android') {
    stage 'Checkout'
    checkout scm

    stage 'Build'
    sh 'cd ./Source/DICommClientSample && ./gradlew assembleDebug'

    stage 'Unit test'
    sh 'rm -rf ./Source/DICommClient/dicommClientLib/build/test-results/debug'
    sh 'cd ./Source/DICommClientSample && ./gradlew testDebugUnitTest || true'
    step([$class: 'JUnitResultArchiver', testResults: 'Source/DICommClient/dicommClientLib/build/test-results/debug/*.xml'])
}