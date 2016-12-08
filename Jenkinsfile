#!/usr/bin/env groovy																											

BranchName = env.BRANCH_NAME

properties([
    [$class: 'ParametersDefinitionProperty', parameterDefinitions: [[$class: 'StringParameterDefinition', defaultValue: '', description: 'triggerBy', name : 'triggerBy']]],
    [$class: 'BuildDiscarderProperty', strategy: [$class: 'LogRotator', numToKeepStr: '50']]
])

def MailRecipient = 'pascal.van.kempen@philips.com,ambati.muralikrishna@philips.com,ramesh.r.m@philips.com'

node_ext = "build_t"
if (env.triggerBy == "ppc") {
  node_ext = "build_p"
}

node ('android_pipeline &&' + node_ext) {
	timestamps {
		stage ('Checkout') {
			checkout([$class: 'GitSCM', branches: [[name: '*/'+BranchName]], doGenerateSubmoduleConfigurations: false, extensions: [[$class: 'WipeWorkspace'], [$class: 'PruneStaleBranch'], [$class: 'LocalBranch']], submoduleCfg: [], userRemoteConfigs: [[credentialsId: '4edede71-63a0-455e-a9dd-d250f8955958', url: 'ssh://git@atlas.natlab.research.philips.com:7999/ur/user_registration_android.git']]])
			step([$class: 'StashNotifier'])
		}

		try {
			stage ('build') {
                echo "fetch git config"
                echo "******"
				sh './check_and_delete_artifact.sh'
               // sh 'cd ./Source/Library && chmod -R 775 ./gradlew && ./gradlew :coppa:clean'
                sh 'cd ./Source/Library && chmod -R 775 ./gradlew && ./gradlew clean assembleDebug cC assembleRelease zipDocuments artifactoryPublish'
			}
            currentBuild.result = 'SUCCESS'
        }

        catch(err) {
            currentBuild.result = 'FAILURE'
            error ("Someone just broke the build")
        }

        try {  
            if (env.triggerBy != "ppc" && !(BranchName =~ "eature")) {
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
