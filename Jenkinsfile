node('Android') {
    stage 'Checkout'
    checkout([$class: 'GitSCM', branches: [[name: '**']], doGenerateSubmoduleConfigurations: false, extensions: [[$class: 'RelativeTargetDirectory', relativeTargetDir: 'android-commlib-all']], submoduleCfg: []])
    sh 'ls -al'

    /*stage 'Build'
    sh 'cd ./Source/project && ./gradlew assembleDebug'

    stage 'Lint'
    sh 'cd ./Source/project && ./gradlew lintDebug || true'
    step([$class: 'LintPublisher', healthy: '0', unHealthy: '20', unstableTotalAll: '20'])

    if(env.BRANCH_NAME == "develop" || env.BRANCH_NAME == "master"){
        stage 'Publish'
        sh 'cd ./Source/project && ./gradlew zipDocuments artifactoryPublish'
    }*/
}
