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

    Pipeline = load "Source/common/jenkins/Pipeline.groovy"
    Slack = load "Source/common/jenkins/Slack.groovy"
    def gradle = 'cd ./Source/DICommClientSample && ./gradlew -PenvCode=${JENKINS_ENV}'

    Slack.notify('#conartists') {
        stage('Build') {
            sh "$gradle --refresh-dependencies assembleRelease assembleDebug saveResDep generateJavadocPublicApi"
        }

        stage('Unit Test') {
            sh 'find . -path "**build/test-results" -exec rm -r "{}" \\;'
            sh "$gradle test lintDebug || true"
        }

        stage("Gather reports") {
            step([$class: 'JUnitResultArchiver', testResults: '**/testDebugUnitTest/*.xml'])
            step([$class: 'LintPublisher', healthy: '0', unHealthy: '20', unstableTotalAll: '20'])
            step([$class: 'JacocoPublisher', execPattern: '**/*.exec', classPattern: '**/classes', sourcePattern: '**/src/main/java', exclusionPattern: '**/R.class,**/R$*.class,**/BuildConfig.class,**/Manifest*.*,**/*Activity*.*,**/*Fragment*.*'])
            publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: false, reportDir: 'Source/DICommClientSample/build/report/dicommClientLib/pitest/debug/', reportFiles: 'index.html', reportName: 'Pitest'])
            publishHTML([allowMissing: false, alwaysLinkToLastBuild: true, keepAll: true, reportDir: 'Documents/External/dicommClientLib-api', reportFiles: 'index.html', reportName: 'Commlib API'])
        }

        stage('Archive artifacts') {
            archiveArtifacts artifacts: 'Source/DICommClient/dicommClientLib/build/outputs/aar/*.aar', fingerprint: true, onlyIfSuccessful: true
            archiveArtifacts artifacts: '**/build/outputs/apk/*.apk', fingerprint: true, onlyIfSuccessful: true
            archiveArtifacts '**/dependencies.lock'
        }

        if (env.BRANCH_NAME == "develop" || env.BRANCH_NAME =~ "release" || env.BRANCH_NAME == "master") {
            stage('Publish') {
                sh "$gradle zipDocuments artifactoryPublish"
            }
        }
    }
    Pipeline.trigger(env.triggerBy, env.BRANCH_NAME, "CommLib", "cml")
}
