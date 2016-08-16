node('Android') {
    stage 'Checkout'
    checkout scm

    stage 'Build'
    sh 'cd ./Source/DICommClientSample && ./gradlew assembleDebug'

    stage 'Unit test'
    sh 'rm -rf ./Source/DICommClient/dicommClientLib/build/test-results/debug'
    sh 'cd ./Source/DICommClientSample && ./gradlew testDebugUnitTest || true'
    step([$class: 'JUnitResultArchiver', testResults: 'Source/DICommClient/dicommClientLib/build/test-results/debug/*.xml'])

    stage 'Archive App'
    step([$class: 'ArtifactArchiver', artifacts: 'Source/DICommClientSample/sampleApp/build/outputs/apk/*.apk', excludes: null, fingerprint: true, onlyIfSuccessful: true])

    if(env.BRANCH_NAME == "develop"){
        stage 'Publish'
        sh 'cd ./Source/ShineLib && ./gradlew zipDocuments artifactoryPublish'
    }
}