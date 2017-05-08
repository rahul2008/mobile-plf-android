#!/usr/bin/env groovy																											

BranchName = env.BRANCH_NAME

properties([
    [$class: 'ParametersDefinitionProperty', parameterDefinitions: [[$class: 'StringParameterDefinition', defaultValue: '', description: 'triggerBy', name : 'triggerBy']]],
    [$class: 'BuildDiscarderProperty', strategy: [$class: 'LogRotator', numToKeepStr: '50']]
])

def MailRecipient = 'DL_CDP2_Callisto@philips.com,DL_App_chassis@philips.com'

node ('android&&keystore') {
	timestamps {
		stage ('Checkout') {
			checkout([$class: 'GitSCM', branches: [[name: '*/'+BranchName]], doGenerateSubmoduleConfigurations: false, extensions: [[$class: 'WipeWorkspace'], [$class: 'PruneStaleBranch'], [$class: 'LocalBranch']], submoduleCfg: [], userRemoteConfigs: [[credentialsId: '4edede71-63a0-455e-a9dd-d250f8955958', url: 'ssh://tfsemea1.ta.philips.com:22/tfs/TPC_Region24/CDP2/_git/ufw-android-uappframework']]])
			step([$class: 'StashNotifier'])
		}
		
		try {
		if (BranchName =~ /master|develop|release.*/) {
			stage ('build') {
                sh """#!/bin/bash -l
                    chmod -R 775 .
                    cd ./Source/Library
                    ./gradlew --refresh-dependencies -PenvCode=${JENKINS_ENV} clean assembleDebug lint
                    ./gradlew --refresh-dependencies -PenvCode=${JENKINS_ENV} assembleRelease zipDocuments artifactoryPublish
                """
			}
			}
			else
			{
			stage ('build') {

        	sh '''#!/bin/bash -l
				    chmod -R 775 .
				    cd ./Source/Library
				    ./gradlew --refresh-dependencies -PenvCode=${JENKINS_ENV} clean assembleDebug lint cC
				'''
			}

			}
			stage ('save dependencies list') {
            	sh """#!/bin/bash -l
            	    cd ./Source/Library
            	    ./gradlew -PenvCode=${JENKINS_ENV} saveResDep
            	"""
            }
	        stage('Unit test') {
	            	sh """#!/bin/bash -l
	            	    cd ./Source/Library
	            	    ./gradlew clean copyResDirectoryToClasses :uAppFwLib:testDebugUnitTest
	            	"""
	        }

	        stage ('reporting') {
	        	androidLint canComputeNew: false, canRunOnFailed: true, defaultEncoding: '', healthy: '', pattern: '', shouldDetectModules: true, unHealthy: '', unstableTotalHigh: '0'
	        	publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: false, reportDir: 'Source/Library/uAppFwLib/build/reports/androidTests/connected', reportFiles: 'index.html', reportName: 'androidTests'])  
	        	publishHTML([allowMissing: false, alwaysLinkToLastBuild: true, keepAll: false, reportDir: 'Source/Library/uAppFwLib/build/reports/coverage/debug', reportFiles: 'index.html', reportName: 'coverage_debug']) 
	        	publishHTML([allowMissing: false, alwaysLinkToLastBuild: true, keepAll: false, reportDir: 'Source/Library/uAppFwLib/build/reports/tests/debug', reportFiles: 'index.html', reportName: 'tests_debug']) 
	            archiveArtifacts '**/dependencies.lock'
	        }

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
        		build job: "Platform-Infrastructure/ppc/ppc_android/${BranchName}", parameters: [[$class: 'StringParameterValue', name: 'componentName', value: 'afw'],[$class: 'StringParameterValue', name: 'libraryName', value: 'uAppFwLib']], wait: false
        	}            
        }

        stage('informing') {
        	step([$class: 'StashNotifier'])
        	step([$class: 'Mailer', notifyEveryUnstableBuild: true, recipients: MailRecipient, sendToIndividuals: true])
        }

	} // end timestamps
} // end node ('android')
