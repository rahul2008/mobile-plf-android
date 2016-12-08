#!/usr/bin/env groovy																											

def BranchName = env.BRANCH_NAME

properties([
    [$class: 'ParametersDefinitionProperty', parameterDefinitions: [[$class: 'StringParameterDefinition', defaultValue: '', description: 'triggerBy', name : 'triggerBy']]],
    [$class: 'BuildDiscarderProperty', strategy: [$class: 'LogRotator', numToKeepStr: '50']]
])

def MailRecipient = 'pascal.van.kempen@philips.com,ambati.muralikrishna@philips.com,ramesh.r.m@philips.com'

node_ext = "build_t"
if (env.triggerBy == "ppc") {
  node_ext = "build_p"
}

node ('Ubuntu &&' + node_ext) {
	timestamps {
		stage ('Checkout') {
            echo "branch to checkout ${BranchName}"
			checkout([$class: 'GitSCM', branches: [[name: '*/'+BranchName]], doGenerateSubmoduleConfigurations: false, extensions: [[$class: 'WipeWorkspace'], [$class: 'PruneStaleBranch'], [$class: 'LocalBranch']], submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'd866c69b-16f0-4fce-823a-2a42bbf90a3d', url: 'ssh://git@atlas.natlab.research.philips.com:7999/pr/hor-productregistration-android.git']]])
			step([$class: 'StashNotifier'])
		}
        
		try {
			stage ('build') {
				sh './check_and_delete_artifact.sh'
                sh 'chmod -R 775 . && cd ./Source/Library && ./gradlew clean assembleDebug lint test jacocoTestReport assembleRelease zipDocuments artifactoryPublish'
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
            		build job: "Platform-Infrastructure/ppc/ppc_android/${BranchName}", parameters: [[$class: 'StringParameterValue', name: 'componentName', value: 'prg'],[$class: 'StringParameterValue', name: 'libraryName', value: '']]
                    currentBuild.result = 'SUCCESS'
            	}            
            }            
		}
		
		catch(err) {
            currentBuild.result = 'UNSTABLE'
        }

        stage('informing') {
        	step([$class: 'StashNotifier'])
        	step([$class: 'Mailer', notifyEveryUnstableBuild: true, recipients: MailRecipient, sendToIndividuals: true])
        }

	} // end timestamps
} // end node ('android')
