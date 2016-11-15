#!/usr/bin/env groovy																											

/* please see ReadMe.md for explanation */ 

/* following 2 lines are mandatory for the platform CI pipeline integration */
BranchName = env.BRANCH_NAME
properties([[$class: 'ParametersDefinitionProperty', parameterDefinitions: [[$class: 'StringParameterDefinition', defaultValue: '', description: 'triggerBy', name : 'triggerBy']]]])


if (!env.CHANGE_ID) {
    properties([[$class: 'BuildDiscarderProperty', strategy: [$class: 'LogRotator', numToKeepStr: '5']]])
    if (BranchName =~ /release\/.*/ || BranchName == 'master') {					/* if branchname is master or contains release, build on commit only	*/
        properties([pipelineTriggers(),])
    }
} // end if (!env.CHANGE_ID)

def MailRecipient = 'pascal.van.kempen@philips.com,ambati.muralikrishna@philips.com,ramesh.r.m@philips.com'

node ('Ubuntu && 24.0.3') {
	timestamps {
		stage ('Checkout') {
			checkout([$class: 'GitSCM', branches: [[name: '*/spike']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'acb45cf5-594a-4209-a56b-b0e75ae62849', url: 'ssh://git@atlas.natlab.research.philips.com:7999/maf/app-framework_android.git']]])
			// step([$class: 'StashNotifier'])
		}
		try {
			stage ('build') {
                sh 'cd ./Source/AppFramework && ./gradlew clean assembleDebug'
			}
			
            /* next if-then + stage is mandatory for the platform CI pipeline integration */
            if (env.triggerBy != "ppc") {
            	stage ('callIntegrationPipeline') {
            		build job: "Platform-Infrastructure/ppc/ppc_android/${BranchName}", parameters: [[$class: 'StringParameterValue', name: 'componentName', value: 'afw'],[$class: 'StringParameterValue', name: 'libraryName', value: '']]
            	}            
            }
            
		} //end try
		
		catch(err) {
            echo "Someone just broke the build"
        }

        stage('informing') {
        	// step([$class: 'StashNotifier'])
        	step([$class: 'Mailer', notifyEveryUnstableBuild: true, recipients: MailRecipient, sendToIndividuals: true])
        }

	} // end timestamps
} // end node ('android')
