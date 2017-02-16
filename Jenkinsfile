#!/usr/bin/env groovy																											

BranchName = env.BRANCH_NAME
JENKINS_ENV = env.JENKINS_ENV

properties([
    [$class: 'ParametersDefinitionProperty', parameterDefinitions: [[$class: 'StringParameterDefinition', defaultValue: '', description: 'triggerBy', name : 'triggerBy']]],
    [$class: 'BuildDiscarderProperty', strategy: [$class: 'LogRotator', numToKeepStr: '10']]
])

def MailRecipient = 'DL_CDP2_Callisto@philips.com, DL_CDP2_MobileUIToolkit@philips.com, ambati.muralikrishna@philips.com'

node_ext = "build_t"
if (env.triggerBy == "ppc") {
  node_ext = "build_p"
}

node ('Ubuntu && 24.0.3 &&' + node_ext) {
	timestamps {
		stage ('Checkout') {
			checkout([$class: 'GitSCM', branches: [[name: '*/'+BranchName]], doGenerateSubmoduleConfigurations: false, extensions: [[$class: 'WipeWorkspace'], [$class: 'PruneStaleBranch'], [$class: 'LocalBranch']], submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'bbd4d9e8-2a6c-4970-b856-4e4cf901e857', url: 'ssh://tfsemea1.ta.philips.com:22/tfs/TPC_Region24/CDP2/_git/uit-android']]])
			step([$class: 'StashNotifier'])
		}
		try {
			stage ('build') {
                sh 'chmod -R 775 . && cd ./Source/CatalogApp && chmod -R 775 ./gradlew && ./gradlew --refresh-dependencies -PenvCode=${JENKINS_ENV} clean assembleDebug && ./gradlew --refresh-dependencies -PenvCode=${JENKINS_ENV} assembleRelease zipDocuments artifactoryPublish'

			}
			stage ('save dependencies list') {
            	sh 'chmod -R 775 . && cd ./Source/CatalogApp && ./gradlew -PenvCode=${JENKINS_ENV} saveResDep'
            }
            archiveArtifacts '**/dependencies.lock'
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
            		build job: "Platform-Infrastructure/ppc/ppc_android/${BranchName}", parameters: [[$class: 'StringParameterValue', name: 'componentName', value: 'uit'],[$class: 'StringParameterValue', name: 'libraryName', value: '']]
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

	} 
}
