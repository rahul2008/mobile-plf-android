#!/usr/bin/env groovy                                                                                                           

BranchName = env.BRANCH_NAME
JENKINS_ENV = env.JENKINS_ENV

properties([
    [$class: 'ParametersDefinitionProperty', parameterDefinitions: [[$class: 'StringParameterDefinition', defaultValue: '', description: 'triggerBy', name : 'triggerBy']]],
    [$class: 'BuildDiscarderProperty', strategy: [$class: 'LogRotator', numToKeepStr: '5']]
])

def MailRecipient = 'DL_CDP2_Callisto@philips.com, DL_App_Framework.com@philips.com'

node ('android&&keystore') {
    timestamps {
        stage ('Checkout') {
            checkout([$class: 'GitSCM', branches: [[name: '*/'+BranchName]], doGenerateSubmoduleConfigurations: false, extensions: [[$class: 'WipeWorkspace'], [$class: 'PruneStaleBranch'], [$class: 'LocalBranch']], submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'd866c69b-16f0-4fce-823a-2a42bbf90a3d', url: 'ssh://tfsemea1.ta.philips.com:22/tfs/TPC_Region24/CDP2/_git/rap-android-reference-app']]])
            step([$class: 'StashNotifier'])
        }

        try {
            if (BranchName =~ /master|develop|release.*/) {
                stage ('build') {
                sh '''#!/bin/bash -l
                    chmod -R 775 .
                    cd ./Source/AppFramework 
                    ./gradlew --refresh-dependencies -PenvCode=${JENKINS_ENV} clean assembleDebug lint assembleLeakCanary
                    ./gradlew -PenvCode=${JENKINS_ENV} assembleRelease test assembleLeakCanary zipDoc appFramework:aP
                '''
                }
            } else {
                stage ('build') {
                sh '''#!/bin/bash -l
                    chmod -R 775 . 
                    cd ./Source/DemoApp 
                    ./gradlew --refresh-dependencies -PenvCode=${JENKINS_ENV} clean assembleDebug lint assembleLeakCanary
                    ./gradlew -PenvCode=${JENKINS_ENV} assembleRelease test assembleLeakCanary 
                '''
                }
            }


            stage ('save dependencies list') {
                sh '''#!/bin/bash -l       
                    chmod -R 775 . 
                    cd ./Source/AppFramework 
                    ./gradlew -PenvCode=${JENKINS_ENV} saveResDep
                ''' 
                }

            stage ('reporting and archiving') {
                androidLint canComputeNew: false, canRunOnFailed: true, defaultEncoding: '', healthy: '', pattern: '', shouldDetectModules: true, unHealthy: '', unstableTotalHigh: '0'
                junit allowEmptyResults: true, testResults: 'Source/Library/*/build/test-results/*/*.xml'
                // publishHTML([allowMissing: true, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'Source/Library/productselection/build/reports/coverage/debug', reportFiles: 'index.html', reportName: 'coverage debug']) 
                // publishHTML([allowMissing: true, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'Source/Library/productselection/build/reports/androidTests/connected', reportFiles: 'index.html', reportName: 'connected tests']) 
                archiveArtifacts '**/dependencies.lock'
                archiveArtifacts '**/build/**/*.apk
            }

        }
            currentBuild.result = 'SUCCESS'
    }

        catch(err) {
            currentBuild.result = 'FAILURE'
            error ("Someone just broke the build", err.toString())
        }

        if (env.triggerBy != "ppc" && (BranchName =~ /master|develop|release.*/)) {
            stage ('callIntegrationPipeline') {
                if (BranchName =~ "/") {
                    BranchName = BranchName.replaceAll('/','%2F')
                    echo "BranchName changed to ${BranchName}"
                }
                build job: "Platform-Infrastructure/ppc/ppc_android/${BranchName}", parameters: [[$class: 'StringParameterValue', name: 'componentName', value: 'rap'],[$class: 'StringParameterValue', name: 'libraryName', value: '']], wait: false
            }
        }

        stage('informing') {
            step([$class: 'StashNotifier'])
            step([$class: 'Mailer', notifyEveryUnstableBuild: true, recipients: MailRecipient, sendToIndividuals: true])
        }

    }
}