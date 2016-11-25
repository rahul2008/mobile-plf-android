#!/usr/bin/env groovy																											

def BranchName = env.BRANCH_NAME

properties([
    [$class: 'ParametersDefinitionProperty', parameterDefinitions: [[$class: 'StringParameterDefinition', defaultValue: '', description: 'triggerBy', name : 'triggerBy']]],
    [$class: 'BuildDiscarderProperty', strategy: [$class: 'LogRotator', numToKeepStr: '50']]
])

def MailRecipient = 'pascal.van.kempen@philips.com,ambati.muralikrishna@philips.com,ramesh.r.m@philips.com'

node ('android_pipeline') {
	timestamps {
		stage ('Checkout') {
            echo "branch to checkout ${BranchName}"
			checkout([$class: 'GitSCM', branches: [[name: '*/'+BranchName]], doGenerateSubmoduleConfigurations: false, extensions: [[$class: 'CleanBeforeCheckout']], submoduleCfg: [], userRemoteConfigs: [[credentialsId: '4edede71-63a0-455e-a9dd-d250f8955958', url: 'ssh://git@atlas.natlab.research.philips.com:7999/pr/hor-productregistration-android.git']]])
			step([$class: 'StashNotifier'])
		}
		try {
			stage ('build') {
                sh 'cd ./Source/Library && ./gradlew clean assembleDebug lint test jacocoTestReport assembleRelease zipDocuments artifactoryPublish'
			}
			
            /* next if-then + stage is mandatory for the platform CI pipeline integration */
            if (env.triggerBy != "ppc") {
            	stage ('callIntegrationPipeline') {
            		build job: "Platform-Infrastructure/ppc/ppc_android/${BranchName}", parameters: [[$class: 'StringParameterValue', name: 'componentName', value: 'prg'],[$class: 'StringParameterValue', name: 'libraryName', value: '']]
            	}            
            }
            
		} //end try
		
		catch(err) {
            echo "Someone just broke the build"
            error ("Someone just broke the build")
        }

        stage('informing') {
        	step([$class: 'StashNotifier'])
        	step([$class: 'Mailer', notifyEveryUnstableBuild: true, recipients: MailRecipient, sendToIndividuals: true])
        }

	} // end timestamps
} // end node ('android')
