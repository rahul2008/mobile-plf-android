#!/usr/bin/env groovy																											

def BranchName = env.BRANCH_NAME
JENKINS_ENV = env.JENKINS_ENV

properties([
    [$class: 'ParametersDefinitionProperty', parameterDefinitions: [[$class: 'StringParameterDefinition', defaultValue: '', description: 'triggerBy', name : 'triggerBy']]],
    [$class: 'BuildDiscarderProperty', strategy: [$class: 'LogRotator', numToKeepStr: '10']]
])

def MailRecipient = 'DL_CDP2_Callisto@philips.com,DL_App_chassis@philips.com '

node ('android&&device') {
	timestamps {
		stage ('Checkout') {
            echo "branch to checkout ${BranchName}"
			checkout([$class: 'GitSCM', branches: [[name: '*/'+BranchName]], doGenerateSubmoduleConfigurations: false, extensions: [[$class: 'WipeWorkspace'], [$class: 'PruneStaleBranch'], [$class: 'LocalBranch']], submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'd866c69b-16f0-4fce-823a-2a42bbf90a3d', url: 'ssh://git@bitbucket.atlas.philips.com:7999/pr/hor-productregistration-android.git']]])
			step([$class: 'StashNotifier'])
		}
        
		try {
            if (BranchName =~ /master|develop|release.*/) {
                stage ('build') {
                    sh '''#!/bin/bash -l
                        chmod -R 775 . 
                        cd ./Source/DemoApp 
                        ./gradlew --refresh-dependencies -PenvCode=${JENKINS_ENV} clean assembleDebug lint
                        ./gradlew -PenvCode=${JENKINS_ENV} assembleRelease cC test jacocoTestReport  zipDocuments artifactoryPublish
                    ''' 
                }
            } 
            else {
                stage ('build') {
                    sh '''#!/bin/bash -l
                        chmod -R 775 . 
                        cd ./Source/DemoApp 
                        ./gradlew --refresh-dependencies -PenvCode=${JENKINS_ENV} clean assembleDebug lint 
                        ./gradlew -PenvCode=${JENKINS_ENV} assembleRelease cC test jacocoTestReport 
                    '''
                }
            }

			stage ('save dependencies list') {
                sh '''#!/bin/bash -l
            	   chmod -R 775 . 
                   cd ./Source/DemoApp 
                   ./gradlew -PenvCode=${JENKINS_ENV} saveResDep
            	   ../Library 
                   ./gradlew -PenvCode=${JENKINS_ENV} saveResDep
                '''
            }

            stage ('reporting') {
                androidLint canComputeNew: false, canRunOnFailed: true, defaultEncoding: '', healthy: '', pattern: '', shouldDetectModules: true, unHealthy: '', unstableTotalHigh: '0'
                junit allowEmptyResults: true, testResults: 'Source/Library/*/build/test-results/*/*.xml'
                publishHTML([allowMissing: true, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'Source/Library/product-registration-lib/build/reports/jacoco/jacocoTestReport/html', reportFiles: 'index.html', reportName: 'jacocoTestReport']) 
                publishHTML([allowMissing: true, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'Source/Library/product-registration-lib/build/reports/tests/testDebugUnitTest/debug', reportFiles: 'index.html', reportName: 'unit test debug']) 
                publishHTML([allowMissing: true, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'Source/Library/product-registration-lib/build/reports/tests/testReleaseUnitTest/release', reportFiles: 'index.html', reportName: 'unit test release']) 
                publishHTML([allowMissing: true, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'Source/Library/product-registration-lib/build/reports/coverage/debug', reportFiles: 'index.html', reportName: 'coverage debug']) 
                archiveArtifacts '**/dependencies.lock'
            }

        } catch(err) {
            currentBuild.result = 'FAILURE'
            error ("Someone just broke the build", err.toString())
        }

        if (env.triggerBy != "ppc" && (BranchName =~ /master|develop|release.*/)) {
        	stage ('callIntegrationPipeline') {
                if (BranchName =~ "/") {
                    BranchName = BranchName.replaceAll('/','%2F')
                    echo "BranchName changed to ${BranchName}"
                }
        		build job: "Platform-Infrastructure/ppc/ppc_android/${BranchName}", parameters: [[$class: 'StringParameterValue', name: 'componentName', value: 'prg'],[$class: 'StringParameterValue', name: 'libraryName', value: '']], wait: false
        	}            
        }            

        stage('informing') {
        	step([$class: 'StashNotifier'])
        	step([$class: 'Mailer', notifyEveryUnstableBuild: true, recipients: MailRecipient, sendToIndividuals: true])
        }

	} // end timestamps
} // end node ('android')
