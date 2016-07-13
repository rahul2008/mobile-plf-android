node('Android') {
    stage 'Checkout'
    checkout scm

    stage 'Android Build'
    sh 'cd ./Source/DICommClientSample && ./gradlew assembleDebug'
    #Test
}