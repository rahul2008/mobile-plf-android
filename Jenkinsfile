#!/usr/bin/env groovy
// please look at: https://jenkins.io/doc/book/pipeline/syntax/
BranchName = env.BRANCH_NAME
String param_string_cron = BranchName == "develop" ? "H H(20-21) * * * %buildType=PSRA \nH H(21-22) * * * %buildType=TICS" : ""

def MailRecipient = 'DL_CDP2_Callisto@philips.com'
def nodes = '27.0.2 && device'
if (BranchName == "develop") {
    nodes = nodes + " && TICS"
}

pipeline {
    agent {
        node {
            label nodes
        }
    }
    parameters {
        choice(choices: 'Normal\nPSRA\nLeakCanary\nTICS', description: 'What type of build to build?', name: 'buildType')
    }
    triggers {
        parameterizedCron(param_string_cron)
    }
    environment {
        EPOCH_TIME = sh(script: 'date +%s', returnStdout: true).trim()
    }
    options {
        timestamps()
        buildDiscarder(logRotator(numToKeepStr: '24'))
    }
    stages {
        stage('Build+test') {
            steps {
                echo "Node labels: ${nodes}"
                sh 'printenv'

                timeout(time: 1, unit: 'HOURS') {
                    InitialiseBuild()
                    BuildAndUnitTest()
                }
            }
        }

        stage('Publish tests') {
            steps {
                PublishUnitTestsresults()
            }
        }

        stage('Lint+Jacoco') {
            when {
                expression { return params.buildType == 'TICS' }
            }
            steps {
                echo '9999Lint+Jacoco'
            }
        }

        stage('PSRAbuild') {
            when {
                allOf {
                    expression { return params.buildType == 'PSRA' }
                }
            }
            steps {
                    echo '9999referenceApp:assemblePsraRelease'
            }
        }

        stage('LeakCanarybuild') {
            when {
                allOf {
                    expression { return params.buildType == 'LeakCanary' }
                    anyOf { branch 'master'; branch 'develop'; branch 'release/platform_*' }
                }
            }
            steps {
                    echo '9999referenceApp:assembleLeakCanary'
            }
        }

        stage('Publish to artifactory') {
            when {
                anyOf { branch 'master'; branch 'develop*'; branch 'release/platform_*' }
            }
            steps {
                echo '9999Publish to artifactory'
            }
        }

        stage('Publish PSRA apk') {
            when {
                allOf {
                    expression { return params.buildType == 'PSRA' }
                }
            }
            steps {
                echo '9999Publish to PSRA apk'
            }
        }

        stage('TICS') {
           when {
               expression { return params.buildType == 'TICS' }
          }
            steps {
                script {
                    echo "9999Running TICS..."
                }
            }
        }

        stage('Trigger E2E Test') {
            when {
                allOf {
                    not { expression { return params.buildType == 'LeakCanary' } }
                    anyOf { branch 'master'; branch 'develop'; branch 'release/platform_*' }
                }
            }
            steps {
                echo "9999Trigger E2E Test"
            }
        }

        stage('LeakCanary E2E Test') {
            when {
                allOf {
                    expression { return params.buildType == 'LeakCanary' }
                    anyOf { branch 'master'; branch 'develop'; branch 'release/platform_*' }
                }
            }
            steps {
                echo "9999LeakCanary E2E Test"
            }
        }
    }
    post {
        always{
            deleteDir()
            step([$class: 'Mailer', notifyEveryUnstableBuild: true, recipients: MailRecipient, sendToIndividuals: true])
        }
    }
}

def InitialiseBuild() {
    committerName = sh (script: "git show -s --format='%an' HEAD", returnStdout: true).trim()
    currentBuild.description = "Submitter: " + committerName + ";Node: ${env.NODE_NAME}"
    echo currentBuild.description
}

def BuildAndUnitTest() {
    echo '9999IntegrationTests: Starting'
    sh '''#!/bin/bash -l
        set -e
        chmod -R 755 .
        ./gradlew --refresh-dependencies --full-stacktrace assembleRelease \
            :commlib-integration-tests:testReleaseUnitTest \
    '''
    echo '9999IntegrationTests: Before Archiving'
    //archiveArtifacts 'Source/rap/Source/AppFramework/appFramework/build/outputs/apk/release/*.apk'
    echo '9999IntegrationTests: Ending'
}

def PublishUnitTestsresults() {

    def cucumber_path = 'Source/cml/Source/Library/build/cucumber-reports'
    def cucumber_filename = 'Source/cml/cucumber-report-android-commlib.json'

    if (fileExists("$cucumber_path/report.json")) {
        step([$class: 'CucumberReportPublisher', jsonReportDirectory: cucumber_path, fileIncludePattern: '*.json'])
    } else {
        echo 'No Cucumber result found, nothing to publish'
    }
}
