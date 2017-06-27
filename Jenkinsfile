#!/usr/bin/env groovy																											

BranchName = env.BRANCH_NAME
JENKINS_ENV = env.JENKINS_ENV

properties([
    [$class: 'ParametersDefinitionProperty', parameterDefinitions: [[$class: 'StringParameterDefinition', defaultValue: '', description: 'triggerBy', name : 'triggerBy']]],
    [$class: 'BuildDiscarderProperty', strategy: [$class: 'LogRotator', numToKeepStr: '50']]
])

def MailRecipient = 'DL_CDP2_Callisto@philips.com, DL_App_chassis@philips.com'
def errors = []

node ('android&&device') {
	timestamps {
		try {
            stage ('Checkout') {
                checkout([$class: 'GitSCM', branches: [[name: '*/'+BranchName]], doGenerateSubmoduleConfigurations: false, extensions: [[$class: 'CloneOption', noTags: false, reference: '', shallow: true, timeout: 30],[$class: 'WipeWorkspace'], [$class: 'PruneStaleBranch'], [$class: 'LocalBranch']], submoduleCfg: [], userRemoteConfigs: [[credentialsId: '4edede71-63a0-455e-a9dd-d250f8955958', url: 'ssh://tfsemea1.ta.philips.com:22/tfs/TPC_Region24/CDP2/_git/usr-android-user-registration']]])
                step([$class: 'StashNotifier'])
            }
    		if (BranchName =~ /master|develop|release.*/) {
    			stage ('build') {
                    sh '''#!/bin/bash -l
                        chmod -R 775 . 
                        cd ./Source/Library 
                        ./gradlew --refresh-dependencies -PenvCode=${JENKINS_ENV} clean assembleDebug lint
                        ./gradlew -PenvCode=${JENKINS_ENV} assembleRelease cC test zipDocuments artifactoryPublish
                    '''
                }
    		} else {
    			stage ('build') {
                    sh '''#!/bin/bash -l
                        chmod -R 775 . 
                        cd ./Source/Library 
                        ./gradlew --refresh-dependencies -PenvCode=${JENKINS_ENV} clean assembleDebug lint
                        ./gradlew -PenvCode=${JENKINS_ENV} assembleRelease cC test
                    '''
    			}
            }

    		stage ('save dependencies list') {
                sh '''#!/bin/bash -l
            	   chmod -R 775 . 
                   cd ./Source/Library 
                   ./gradlew -PenvCode=${JENKINS_ENV} saveResDep saveAllResolvedDependencies saveAllResolvedDependenciesGradleFormat
                '''
            }

            if (env.triggerBy != "ppc" && (BranchName =~ /master|develop|release.*/)) {
                stage ('callIntegrationPipeline') {
                    if (BranchName =~ "/") {
                        BranchName = BranchName.replaceAll('/','%2F')
                        echo "BranchName changed to ${BranchName}"
                    }
                    build job: "Platform-Infrastructure/ppc/ppc_android/${BranchName}", parameters: [[$class: 'StringParameterValue', name: 'componentName', value: 'usr'],[$class: 'StringParameterValue', name: 'libraryName', value: '']], wait: false 
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
            stage ('reporting') {
                androidLint canComputeNew: false, canRunOnFailed: true, defaultEncoding: '', healthy: '', pattern: '', shouldDetectModules: true, unHealthy: '', unstableTotalHigh: '0'
                junit allowEmptyResults: false, testResults: 'Source/Library/**/build/test-results/**/*.xml'
                junit allowEmptyResults: false, testResults: 'Source/Library/**/build/outputs/androidTest-results/*/*.xml'
                publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'Source/Library/RegistrationApi/build/reports/tests/testDebugUnitTest', reportFiles: 'index.html', reportName: 'unit test debug']) 
                publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'Source/Library/RegistrationApi/build/reports/tests/testReleaseUnitTest', reportFiles: 'index.html', reportName: 'unit test release'])
                publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'Source/Library/RegistrationApi/build/reports/androidTests/connected', reportFiles: 'index.html', reportName: 'connected tests RegistrationApi'])
                // publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'Source/Library/coppa/build/reports/androidTests/connected', reportFiles: 'index.html', reportName: 'connected tests coppa'])
                publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'Source/Library/Jump/build/reports/androidTests/connected', reportFiles: 'index.html', reportName: 'connected tests Jump'])
                publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'Source/Library/hsdp/build/reports/androidTests/connected', reportFiles: 'index.html', reportName: 'connected tests hsdp'])
                archiveArtifacts '**/*dependencies*.lock'
            }
        	stage('informing') {
                step([$class: 'StashNotifier'])
            	step([$class: 'Mailer', notifyEveryUnstableBuild: true, recipients: MailRecipient, sendToIndividuals: true])
            }
            stage('Cleaning workspace') {
                step([$class: 'WsCleanup', deleteDirs: true, notFailBuild: true])
            }
        }
	}
}

node('master') {
    stage('Cleaning workspace') {
        def wrk = pwd() + "@script/"
        dir("${wrk}") {
            deleteDir()
        }
    }
}