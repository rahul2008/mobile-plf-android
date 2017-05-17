/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

/* following line is mandatory for the platform CI pipeline integration */
properties([[$class: 'ParametersDefinitionProperty', parameterDefinitions: [[$class: 'StringParameterDefinition', defaultValue: '', description: 'triggerBy', name: 'triggerBy']]]])

node('Android') {
    stage('Checkout') {
        checkout scm
    }

    Slack = load "Source/common/jenkins/Slack.groovy"
    Pipeline = load "Source/common/jenkins/Pipeline.groovy"

    Slack.notify('#conartists') {
        stage('Build') {
            sh 'cd ./Source/cloudcontroller-api && ./gradlew --refresh-dependencies -PenvCode=${JENKINS_ENV} assembleRelease generateJavadocPublicApi'
        }

        stage('Lint') {
            sh 'cd ./Source/cloudcontroller-api && ./gradlew lintDebug || true'
            step([$class: 'LintPublisher', healthy: '0', unHealthy: '20', unstableTotalAll: '20'])
        }

        stage('Archive artifacts') {
            step([$class: 'ArtifactArchiver', artifacts: 'Source/cloudcontroller-api/build/outputs/aar/*.aar', excludes: null, fingerprint: true, onlyIfSuccessful: true])
            publishHTML([allowMissing: false, alwaysLinkToLastBuild: true, keepAll: true, reportDir: 'Documents/External/cloudcontroller-api', reportFiles: 'index.html', reportName: 'Cloudcontroller API'])
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
    Pipeline.trigger(env.triggerBy, env.BRANCH_NAME, "CloudControllerApi", "cca")
}
