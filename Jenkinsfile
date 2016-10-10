node('Android') {
    stage 'Checkout'
    checkout scm

    stage 'Build'
    sh 'cd ./Source/CloudController && ./gradlew assembleDebug'

    stage 'Unit test'
    sh 'rm -rf ./Source/CloudController/build/test-results/debug'
    sh 'cd ./Source/CloudController && ./gradlew test || true'
    step([$class: 'JUnitResultArchiver', testResults: 'Source/ShineLib/*/build/test-results/*/*.xml'])

    stage 'Lint'
    sh 'cd ./Source/CloudController && ./gradlew lintDebug || true'
    step([$class: 'LintPublisher', healthy: '0', unHealthy: '20', unstableTotalAll: '20'])

    stage 'Archive Apps'
    step([$class: 'ArtifactArchiver', artifacts: 'Source/CloudController/build/outputs/aar/*.aar', excludes: null, fingerprint: true, onlyIfSuccessful: true])

    if(env.BRANCH_NAME == "develop" || env.BRANCH_NAME == "master"){
        stage 'Publish'
        sh 'cd ./Source/ShineLib && ./gradlew zipDocuments artifactoryPublish'
    }
}
