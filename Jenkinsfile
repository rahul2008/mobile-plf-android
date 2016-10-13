node('Android') {
    stage 'Checkout'
    checkout scm

    stage 'Build'
    sh 'cd ./Source/cloudcontroller-api && ./gradlew assembleDebug'

    stage 'Unit test'
    sh 'rm -rf ./Source/cloudcontroller-api/build/test-results/debug'
    sh 'cd ./Source/cloudcontroller-api && ./gradlew test || true'
    step([$class: 'JUnitResultArchiver', testResults: 'Source/cloudcontroller-api/build/test-results/*/*.xml'])

    stage 'Lint'
    sh 'cd ./Source/cloudcontroller-api && ./gradlew lintDebug || true'
    step([$class: 'LintPublisher', healthy: '0', unHealthy: '20', unstableTotalAll: '20'])

    stage 'Archive Apps'
    step([$class: 'ArtifactArchiver', artifacts: 'Source/cloudcontroller-api/build/outputs/aar/*.aar', excludes: null, fingerprint: true, onlyIfSuccessful: true])

    if(env.BRANCH_NAME == "develop" || env.BRANCH_NAME == "master"){
        stage 'Publish'
        sh 'cd ./Source/cloudcontroller-api && ./gradlew zipDocuments artifactoryPublish'
    }
}
