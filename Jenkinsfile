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
			checkout([$class: 'GitSCM', branches: [[name: '*/'+BranchName]], doGenerateSubmoduleConfigurations: false, extensions: [[$class: 'CloneOption', noTags: false, reference: '', shallow: true, timeout: 30],[$class: 'WipeWorkspace'], [$class: 'PruneStaleBranch'], [$class: 'LocalBranch']], submoduleCfg: [], userRemoteConfigs: [[credentialsId: '4edede71-63a0-455e-a9dd-d250f8955958', url: 'ssh://git@bitbucket.atlas.philips.com:7999/ur/user_registration_android.git']]])
			step([$class: 'StashNotifier'])
		}

		try {
		if (BranchName =~ /master|develop|release.*/) {
			stage ('build') {
			sh 'chmod -R 775 . && cd ./Source/Library && chmod -R 775 ./gradlew && ./gradlew clean assembleDebug && ./gradlew cC assembleRelease zipDocuments artifactoryPublish'
                //echo "fetch git config"
                //echo "******"
               // sh 'cd ./Source/Library && chmod -R 775 ./gradlew && ./gradlew :coppa:clean'
                
			}
			}	
			}
			else
			{
			stage ('build') {
				sh 'chmod -R 775 . && cd ./Source/Library && ./gradlew clean assembleDebug assembleRelease'
			}
			}
            currentBuild.result = 'SUCCESS'
        }

        catch(err) {
            currentBuild.result = 'FAILURE'
            error ("Someone just broke the build")
        }

        try {  
            if (env.triggerBy != "ppc" && (BranchName =~ /master|develop|release.*/)) {
            	stage ('callIntegrationPipeline') {
                    if (BranchName =~ "/") {
                        BranchName = BranchName.replaceAll('/','%2F')
                        echo "BranchName changed to ${BranchName}"
                    }
            		build job: "Platform-Infrastructure/ppc/ppc_android/${BranchName}", parameters: [[$class: 'StringParameterValue', name: 'componentName', value: 'usr'],[$class: 'StringParameterValue', name: 'libraryName', value: '']]
                    currentBuild.result = 'SUCCESS'
            	}            
            }
		}

		catch(err) {
            currentBuild.result = 'UNSTABLE'
        }


        	step([$class: 'StashNotifier'])
        	step([$class: 'Mailer', notifyEveryUnstableBuild: true, recipients: MailRecipient, sendToIndividuals: true])

	}
}
