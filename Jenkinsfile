/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

/* following line is mandatory for the platform CI pipeline integration */
properties([[$class: 'ParametersDefinitionProperty', parameterDefinitions: [[$class: 'StringParameterDefinition', defaultValue: '', description: 'triggerBy', name: 'triggerBy']]]])

node('Android') {
    stage('Checkout') {
        //sh 'find . -path "**/build/test-results" -delete' DOES NOT WORK ON NON-EMPTY FOLDERS
        checkout scm
    }

    Slack = load 'Source/common/jenkins/Slack.groovy'
    Pipeline = load 'Source/common/jenkins/Pipeline.groovy'
    def gradle = 'cd Source && ./gradlew -PenvCode=${JENKINS_ENV}'

    Slack.notify('#conartists') {
        boolean publishing = (env.BRANCH_NAME.startsWith("develop") || env.BRANCH_NAME.startsWith("release") || env.BRANCH_NAME == "master")

        stage('Build') {
            sh "$gradle generateJavadocPublicApi assemble saveResDep testDebug"
        }

        if (publishing) {
            for (lib in ["commlib-testutils", "cloudcontroller-api", "cloudcontroller", "commlib-api", "commlib-ble", "commlib-lan", "commlib-cloud", "commlib"]) {
                def libgradle = "cd Source/Library/$lib && ./gradlew -u -PenvCode=\${JENKINS_ENV}"
                stage("Publish $lib") {
                    sh "$libgradle assembleRelease zipDocuments artifactoryPublish"
                }
            }
        }

        stage('Archive results') {
            archiveArtifacts artifacts: '**/build/outputs/aar/*.aar', fingerprint: true, onlyIfSuccessful: true
            archiveArtifacts artifacts: '**/build/outputs/apk/*.apk', fingerprint: true, onlyIfSuccessful: true
            archiveArtifacts '**/dependencies.lock'
        }
    }
}
