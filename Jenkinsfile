node('Android') {
    stage 'Checkout'
    checkout scm

    stage 'Build'
    sh 'cd ./Source/AppFramework && ./gradlew assembleDebug'

    stage 'Release'
    sh 'cd ./Source/AppFramework && ./gradlew assembleDebug zipDoc aP'
}