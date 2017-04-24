#!/usr/bin/env groovy																											

BranchName = env.BRANCH_NAME

properties([
    [$class: 'ParametersDefinitionProperty', parameterDefinitions: [[$class: 'StringParameterDefinition', defaultValue: '', description: 'triggerBy', name : 'triggerBy']]],
    [$class: 'BuildDiscarderProperty', strategy: [$class: 'LogRotator', numToKeepStr: '50']]
])

def MailRecipient = 'DL_CDP2_Callisto@philips.com,DL_App_chassis@philips.com'

node_ext = "build_t"
if (env.triggerBy == "ppc") {
  node_ext = "build_p"
}

node ('android_pipeline &&' + node_ext) {
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
                    ./gradlew --refresh-dependencies -PenvCode=${JENKINS_ENV} clean assembleDebug
                    ./gradlew --refresh-dependencies -PenvCode=${JENKINS_ENV} lint assembleRelease zipDocuments artifactoryPublish
                """
			}
			}
			else
			{
			stage ('build') {
        sh """#!/bin/bash -l
				    chmod -R 775 .
				    cd ./Source/Library
				    ./gradlew --refresh-dependencies -PenvCode=${JENKINS_ENV} clean assembleDebug assembleRelease
				"""
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
              step([$class: 'JUnitResultArchiver', testResults: 'Source/Library/*/build/test-results/*/*.xml'])
        }
            archiveArtifacts '**/dependencies.lock'
            currentBuild.result = 'SUCCESS'
        }

        catch(err) {
            currentBuild.result = 'FAILURE'
            error ("Someone just broke the build")
        }

        try{    
            if (env.triggerBy != "ppc" && (BranchName =~ /master|develop|release.*/)) {
            	stage ('callIntegrationPipeline') {
                    if (BranchName =~ "/") {
                        BranchName = BranchName.replaceAll('/','%2F')
                        echo "BranchName changed to ${BranchName}"
                    }
            		build job: "Platform-Infrastructure/ppc/ppc_android/${BranchName}", parameters: [[$class: 'StringParameterValue', name: 'componentName', value: 'afw'],[$class: 'StringParameterValue', name: 'libraryName', value: 'uAppFwLib']]
                    currentBuild.result = 'SUCCESS'
            	}            
            }
            
		} //end try
		
		catch(err) {
            currentBuild.result = 'UNSTABLE'
        }

        stage('informing') {
        	step([$class: 'StashNotifier'])
        	step([$class: 'Mailer', notifyEveryUnstableBuild: true, recipients: MailRecipient, sendToIndividuals: true])
        }

	} // end timestamps
} // end node ('android')
