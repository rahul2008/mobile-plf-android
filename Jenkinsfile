#!/usr/bin/env groovy																											

BranchName = env.BRANCH_NAME
JENKINS_ENV = env.JENKINS_ENV

properties([
    [$class: 'ParametersDefinitionProperty', parameterDefinitions: [[$class: 'StringParameterDefinition', defaultValue: '', description: 'triggerBy', name : 'triggerBy']]],
    [$class: 'BuildDiscarderProperty', strategy: [$class: 'LogRotator', numToKeepStr: '50']]
])

def MailRecipient = 'DL_CDP2_Callisto@philips.com, DL_App_chassis@philips.com'

node ('android&&device') {
	timestamps {
		stage ('Checkout') {
			checkout([$class: 'GitSCM', branches: [[name: '*/'+BranchName]], doGenerateSubmoduleConfigurations: false, extensions: [[$class: 'CloneOption', noTags: false, reference: '', shallow: true, timeout: 30],[$class: 'WipeWorkspace'], [$class: 'PruneStaleBranch'], [$class: 'LocalBranch']], submoduleCfg: [], userRemoteConfigs: [[credentialsId: '4edede71-63a0-455e-a9dd-d250f8955958', url: 'ssh://tfsemea1.ta.philips.com:22/tfs/TPC_Region24/CDP2/_git/usr-android-user-registration']]])
			step([$class: 'StashNotifier'])
		}

		try {
		if (BranchName =~ /master|develop|release.*/) {
			stage ('build') {
			sh 'chmod -R 775 . && cd ./Source/Library && chmod -R 775 ./gradlew && ./gradlew --refresh-dependencies -PenvCode=${JENKINS_ENV} clean assembleDebug && ./gradlew --refresh-dependencies -PenvCode=${JENKINS_ENV} cC assembleRelease zipDocuments artifactoryPublish'
                //echo "fetch git config"
                //echo "******"
               // sh 'cd ./Source/Library && chmod -R 775 ./gradlew && ./gradlew :coppa:clean'
                
			}
			}	
			else
			{
			stage ('build') {
				sh 'chmod -R 775 . && cd ./Source/Library && ./gradlew --refresh-dependencies -PenvCode=${JENKINS_ENV} clean assembleDebug assembleRelease'
			}
			}
			stage ('save dependencies list') {
            	sh 'chmod -R 775 . && cd ./Source/Library && ./gradlew -PenvCode=${JENKINS_ENV} saveResDep'
            }
            archiveArtifacts '**/dependencies.lock'
            currentBuild.result = 'SUCCESS'
        }

        catch(err) {
            currentBuild.result = 'FAILURE'
            error ("Someone just broke the build")
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

    	step([$class: 'StashNotifier'])
    	step([$class: 'Mailer', notifyEveryUnstableBuild: true, recipients: MailRecipient, sendToIndividuals: true])

	}
}
