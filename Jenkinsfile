#!/usr/bin/env groovy																											

BranchName = env.BRANCH_NAME

properties([
    [$class: 'ParametersDefinitionProperty', parameterDefinitions: [[$class: 'StringParameterDefinition', defaultValue: '', description: 'triggerBy', name : 'triggerBy']]],
    [$class: 'BuildDiscarderProperty', strategy: [$class: 'LogRotator', numToKeepStr: '50']]
])

def MailRecipient = 'benit.dhotekar@philips.com, DL_CDP2_Callisto@philips.com, abhishek.gadewar@philips.com, krishna.kumar.a@philips.com, ramesh.r.m@philips.com'

node ('Ubuntu && 23.0.3') {
	timestamps {
		stage ('Checkout') {
			checkout scm
			step([$class: 'StashNotifier'])
		}
		try {
			stage ('build') {
                sh 'cd ./Source/AppFramework && ./gradlew clean assembleDebug'
			}
			
            sh 'cd ./Source/AppFramework && ./gradlew assembleDebug'

            if(env.BRANCH_NAME == 'master') {
                stage 'Release'
                sh 'cd ./Source/AppFramework && ./gradlew zipDoc appFramework:aP'
            }

            if(env.BRANCH_NAME == 'develop') {
                stage 'Release'
                sh 'cd ./Source/AppFramework && ./gradlew zipDoc appFramework:aP'
            }

            if(env.BRANCH_NAME =~ /release\/.*/) {
                stage 'Release'
                sh 'cd ./Source/AppFramework && ./gradlew zipDoc appFramework:aP'
            }


            /* next if-then + stage is mandatory for the platform CI pipeline integration */
            if (env.triggerBy != "ppc") {
            	stage ('callIntegrationPipeline') {
            		build job: "Platform-Infrastructure/ppc/ppc_android/${BranchName}", parameters: [[$class: 'StringParameterValue', name: 'componentName', value: 'rap'],[$class: 'StringParameterValue', name: 'libraryName', value: '']]
            	}            
            }

            currentBuild.result = 'SUCCESS'
            
		} //end try
		
		catch(err) {
            currentBuild.result = 'FAILED'
            echo "Someone just broke the build"
        }

        stage('informing') {
        	step([$class: 'StashNotifier'])
        	step([$class: 'Mailer', notifyEveryUnstableBuild: true, recipients: MailRecipient, sendToIndividuals: true])
        }

	} // end timestamps
} // end node ('android')
