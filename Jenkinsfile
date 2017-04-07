#!/usr/bin/env groovy																											

def BranchName = env.BRANCH_NAME
JENKINS_ENV = env.JENKINS_ENV

properties([
    [$class: 'ParametersDefinitionProperty', parameterDefinitions: [[$class: 'StringParameterDefinition', defaultValue: '', description: 'triggerBy', name : 'triggerBy']]],
    [$class: 'BuildDiscarderProperty', strategy: [$class: 'LogRotator', numToKeepStr: '10']]
])

def MailRecipient = 'DL_CDP2_Callisto@philips.com,DL_App_chassis@philips.com '

node_ext = "build_t"
if (env.triggerBy == "ppc") {
  node_ext = "build_p"
}

node ('Ubuntu &&' + node_ext) {
	timestamps {
		stage ('Checkout') {
            echo "branch to checkout ${BranchName}"
			checkout([$class: 'GitSCM', branches: [[name: '*/'+BranchName]], doGenerateSubmoduleConfigurations: false, extensions: [[$class: 'WipeWorkspace'], [$class: 'PruneStaleBranch'], [$class: 'LocalBranch']], submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'd866c69b-16f0-4fce-823a-2a42bbf90a3d', url: 'ssh://git@bitbucket.atlas.philips.com:7999/pr/hor-productregistration-android.git']]])
			step([$class: 'StashNotifier'])
		}
        
		try {
            if (BranchName =~ /master|develop|release.*/) {
                stage ('build') {
                    sh 'chmod -R 775 . && cd ./Source/DemoApp && ./gradlew --refresh-dependencies -PenvCode=${JENKINS_ENV} clean assembleDebug && ./gradlew --refresh-dependencies -PenvCode=${JENKINS_ENV} lint test jacocoTestReport assembleRelease zipDocuments artifactoryPublish'
                }
            } else {
                stage ('build') {
                    sh 'chmod -R 775 . && cd ./Source/DemoApp && ./gradlew --refresh-dependencies -PenvCode=${JENKINS_ENV} clean assembleDebug assembleRelease'
                }
            }
			stage ('save dependencies list') {
            	sh 'chmod -R 775 . && cd ./Source/DemoApp && ./gradlew -PenvCode=${JENKINS_ENV} saveResDep'
            	sh 'chmod -R 775 . && cd ./Source/Library && ./gradlew -PenvCode=${JENKINS_ENV} saveResDep'
            }
            archiveArtifacts '**/dependencies.lock'
            currentBuild.result = 'SUCCESS'
        } catch(err) {
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
            		build job: "Platform-Infrastructure/ppc/ppc_android/${BranchName}", parameters: [[$class: 'StringParameterValue', name: 'componentName', value: 'prg'],[$class: 'StringParameterValue', name: 'libraryName', value: '']]
                    currentBuild.result = 'SUCCESS'
            	}            
            }            
		} catch(err) {
            currentBuild.result = 'UNSTABLE'
        }

        stage('informing') {
        	step([$class: 'StashNotifier'])
        	step([$class: 'Mailer', notifyEveryUnstableBuild: true, recipients: MailRecipient, sendToIndividuals: true])
        }

	} // end timestamps
} // end node ('android')
