node('Android') {
    stage 'Checkout'
    checkout scm

    stage 'Build'
    sh 'cd ./Source/cloudcontroller-api && ./gradlew --refresh-dependencies -PenvCode=${JENKINS_ENV} assembleRelease'

    stage 'Lint'
    sh 'cd ./Source/cloudcontroller-api && ./gradlew lintDebug || true'
    step([$class: 'LintPublisher', healthy: '0', unHealthy: '20', unstableTotalAll: '20'])

    if(env.BRANCH_NAME == "develop" || env.BRANCH_NAME == "master" || env.BRANCH_NAME =~ "release" ){
        stage 'Publish'
        sh 'cd ./Source/cloudcontroller-api && ./gradlew -PenvCode=${JENKINS_ENV} zipDocuments artifactoryPublish'
    }

    stage ('save dependencies list') {
    	sh 'cd ./Source/cloudcontroller-api && ./gradlew -PenvCode=${JENKINS_ENV} saveResDep'
    }
    archiveArtifacts '**/dependencies.lock'
}
