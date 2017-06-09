#!/usr/bin/env groovy																											

BranchName = env.BRANCH_NAME
JENKINS_ENV = env.JENKINS_ENV

properties([
    [$class: 'ParametersDefinitionProperty', parameterDefinitions: [[$class: 'StringParameterDefinition', defaultValue: '', description: 'triggerBy', name : 'triggerBy']]],
    [$class: 'BuildDiscarderProperty', strategy: [$class: 'LogRotator', numToKeepStr: '10']]
])

def MailRecipient = 'DL_CDP2_Callisto@philips.com,DL_App_chassis@philips.com'
def errors = []

node ('android&&device') {
	timestamps {
		try {
            stage ('Checkout') {
                checkout([$class: 'GitSCM', branches: [[name: '*/'+BranchName]], doGenerateSubmoduleConfigurations: false, extensions: [[$class: 'WipeWorkspace'], [$class: 'PruneStaleBranch'], [$class: 'LocalBranch']], submoduleCfg: [], userRemoteConfigs: [[credentialsId: '4edede71-63a0-455e-a9dd-d250f8955958', url: 'ssh://tfsemea1.ta.philips.com:22/tfs/TPC_Region24/CDP2/_git/iap-android-in-app-purchase']]])
                step([$class: 'StashNotifier'])
            }
            if (BranchName =~ /master|develop|release.*/) {
                stage ('build') {
                sh '''#!/bin/bash -l
                    chmod -R 775 .
                    cd ./Source/DemoApp 
                    ./gradlew --refresh-dependencies -PenvCode=${JENKINS_ENV} clean assembleDebug lint 
                    ./gradlew -PenvCode=${JENKINS_ENV} assembleRelease cC test zipDocuments artifactoryPublish
                '''
                }
            } else {
                stage ('build') {
                sh '''#!/bin/bash -l
                    chmod -R 775 . 
                    cd ./Source/DemoApp 
                    ./gradlew --refresh-dependencies -PenvCode=${JENKINS_ENV} clean assembleDebug lint 
                    ./gradlew -PenvCode=${JENKINS_ENV} assembleRelease cC test
                '''
                }
            }
			stage ('save dependencies list') {
            sh '''#!/bin/bash -l
            	chmod -R 775 . 
                cd ./Source/DemoApp 
                ./gradlew -PenvCode=${JENKINS_ENV} saveResDep
            	cd ../Library 
                ./gradlew -PenvCode=${JENKINS_ENV} saveResDep
            '''
            }

           stage ('reporting') {
                androidLint canComputeNew: false, canRunOnFailed: true, defaultEncoding: '', healthy: '', pattern: '', shouldDetectModules: true, unHealthy: '', unstableTotalHigh: '0'
                junit allowEmptyResults: true, testResults: 'Source/Library/*/build/test-results/*/*.xml'
                publishHTML([allowMissing: true, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'Source/Library/iap/build/reports/tests/debug', reportFiles: 'index.html', reportName: 'unit test debug']) 
                publishHTML([allowMissing: true, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'Source/Library/iap/build/reports/tests/release', reportFiles: 'index.html', reportName: 'unit test release'])
                publishHTML([allowMissing: true, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'Source/Library/iap/build/reports/coverage/debug', reportFiles: 'index.html', reportName: 'coverage tests'])
                publishHTML([allowMissing: true, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'Source/Library/iap/build/reports/androidTests/connected', reportFiles: 'index.html', reportName: 'connected tests'])
                archiveArtifacts '**/dependencies.lock'
            }

            if (env.triggerBy != "ppc" && (BranchName =~ /master|develop|release.*/)) {
                stage ('callIntegrationPipeline') {
                    if (BranchName =~ "/") {
                        BranchName = BranchName.replaceAll('/','%2F')
                        echo "BranchName changed to ${BranchName}"
                    }
                    build job: "Platform-Infrastructure/ppc/ppc_android/${BranchName}", parameters: [[$class: 'StringParameterValue', name: 'componentName', value: 'iap'],[$class: 'StringParameterValue', name: 'libraryName', value: '']], wait: false
                }            
            }
        } catch(err) {
            errors << "errors found: ${err}"      
        } finally {
            if (errors.size() > 0) {
                stage ('error reporting') {
                    currentBuild.result = 'FAILURE'
                    for (int i = 0; i < errors.size(); i++) {
                        echo errors[i]; 
                    }
                }                
            }     
            stage('informing') {
                step([$class: 'StashNotifier'])
                step([$class: 'Mailer', notifyEveryUnstableBuild: true, recipients: MailRecipient, sendToIndividuals: true])
            }
            stage('Cleaning workspace') {
                step([$class: 'WsCleanup', deleteDirs: true, notFailBuild: true])
            }
        }
	} // end timestamps
} // end node ('android')

node('master') {
    stage('Cleaning workspace') {
        def wrk = pwd() + "@script/"
        dir("${wrk}") {
            deleteDir()
        }
    }
}
