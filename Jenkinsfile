node('23.0.3') {
    stage 'Checkout'
    checkout scm

    stage 'Build'
    sh 'cd ./Source/AppFramework && ./gradlew assembleDebug'

    stage 'Release'
    sh 'cd ./Source/AppFramework && ./gradlew zipDoc appFramework:aP'

    stage 'Notify Bitbucket'
    sh 'echo \"This is required but does not work out of the box!\"'

}