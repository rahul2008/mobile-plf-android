node('Android') {
    stage('Checkout') {
        checkout scm
    }

    Slack = load "Source/common/jenkins/Slack.groovy"

    Slack.notify('#conartists') {
        stage('Build') {
            sh 'cd ./Source/cloudcontroller-api && ./gradlew --refresh-dependencies -PenvCode=${JENKINS_ENV} assembleRelease'
        }

        stage('Lint') {
            sh 'cd ./Source/cloudcontroller-api && ./gradlew lintDebug || true'
            step([$class: 'LintPublisher', healthy: '0', unHealthy: '20', unstableTotalAll: '20'])
        }

        stage('Archive artifacts') {
            step([$class: 'ArtifactArchiver', artifacts: 'Source/cloudcontroller-api/build/outputs/aar/*.aar', excludes: null, fingerprint: true, onlyIfSuccessful: true])
        }

        if (env.BRANCH_NAME == "develop" || env.BRANCH_NAME == "master" || env.BRANCH_NAME =~ "release") {
            stage('Publish') {
                sh 'cd ./Source/cloudcontroller-api && ./gradlew -PenvCode=${JENKINS_ENV} zipDocuments artifactoryPublish'
            }
        }

        stage('Save Dependencies') {
            sh 'cd ./Source/cloudcontroller-api && ./gradlew -PenvCode=${JENKINS_ENV} saveResDep'
            archiveArtifacts '**/dependencies.lock'
        }
    }
}
