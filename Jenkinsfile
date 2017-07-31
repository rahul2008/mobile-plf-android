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

    def cucumber_path = 'Source/Library/build/cucumber-reports'
    def cucumber_filename = 'cucumber-report-android-commlib.json'

    Slack.notify('#conartists') {
        boolean publishing = (env.BRANCH_NAME.startsWith("develop") || env.BRANCH_NAME.startsWith("release") || env.BRANCH_NAME == "master")

        stage('Build') {
            sh "$gradle generateJavadocPublicApi saveResDep assemble testDebug"
        }

        stage("Gather reports") {
            step([$class: 'JUnitResultArchiver', testResults: '**/testDebugUnitTest/*.xml'])
            step([$class: 'LintPublisher', healthy: '0', unHealthy: '50', unstableTotalAll: '50'])
            step([$class: 'JacocoPublisher', execPattern: '**/*.exec', classPattern: '**/classes', sourcePattern: '**/src/main/java', exclusionPattern: '**/R.class,**/R$*.class,**/BuildConfig.class,**/Manifest*.*,**/*Activity*.*,**/*Fragment*.*'])
            for (lib in ["commlib-api", "commlib-ble", "commlib-lan", "commlib-cloud"]) {
                publishHTML([allowMissing: false, alwaysLinkToLastBuild: true, keepAll: true, reportDir: "Documents/External/$lib-api", reportFiles: 'index.html', reportName: "$lib API documentation"])
            }

            if (fileExists("$cucumber_path/report.json")) {
                step([$class: 'CucumberReportPublisher', jsonReportDirectory: cucumber_path, fileIncludePattern: '*.json'])
            } else {
                echo 'No Cucumber result found, nothing to publish'
            }
        }

        if (publishing) {
            for (lib in ["commlib-testutils", "cloudcontroller-api", "cloudcontroller", "commlib-api", "commlib-ble", "commlib-lan", "commlib-cloud", "commlib"]) {
                def libgradle = "cd Source/Library/$lib && ./gradlew -u -PenvCode=\${JENKINS_ENV}"
                stage("Publish $lib") {
                    sh "$libgradle assembleRelease saveResDep zipDocuments artifactoryPublish"
                }
            }
        }

        stage('Archive results') {
            archiveArtifacts artifacts: '**/build/outputs/aar/*.aar', fingerprint: true, onlyIfSuccessful: true
            archiveArtifacts artifacts: '**/build/outputs/apk/*.apk', fingerprint: true, onlyIfSuccessful: true
            archiveArtifacts '**/dependencies.lock'

            sh "mv $cucumber_path/report.json $cucumber_path/$cucumber_filename"
            archiveArtifacts artifacts: "$cucumber_path/$cucumber_filename", fingerprint: true, onlyIfSuccessful: true
        }
    }
}
