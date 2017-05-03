#!/usr/bin/env groovy																											

/* please see ReadMe.md for explanation */ 


BranchName = env.BRANCH_NAME
JENKINS_ENV = env.JENKINS_ENV

properties([
    [$class: 'ParametersDefinitionProperty', parameterDefinitions: [[$class: 'StringParameterDefinition', defaultValue: '', description: 'triggerBy', name : 'triggerBy']]],
    [$class: 'BuildDiscarderProperty', strategy: [$class: 'LogRotator', numToKeepStr: '10']]
])

def MailRecipient = 'DL_CDP2_Callisto@philips.com,DL_CDP2_TeamSabers@philips.com'

node ('android&&device&&keystore') {
	timestamps {
		stage ('Checkout') {
			checkout([$class: 'GitSCM', branches: [[name: '*/'+BranchName]], doGenerateSubmoduleConfigurations: false, extensions: [[$class: 'CloneOption', noTags: false, reference: '', shallow: true, timeout: 30],[$class: 'WipeWorkspace'], [$class: 'PruneStaleBranch'], [$class: 'LocalBranch']], submoduleCfg: [], userRemoteConfigs: [[credentialsId: '4edede71-63a0-455e-a9dd-d250f8955958', url: 'ssh://tfsemea1.ta.philips.com:22/tfs/TPC_Region24/CDP2/_git/dcc-android-consumercare-app']]])
			step([$class: 'StashNotifier'])
		}
		try {
            if (BranchName =~ /master|develop|release.*/) {
                stage ('build') {
                    sh 'chmod -R 755 . && cd ./Source/DemoApp && chmod -R 775 ./gradlew && ./gradlew --refresh-dependencies -PenvCode=${JENKINS_ENV} clean assembleDebug && ./gradlew --refresh-dependencies -PenvCode=${JENKINS_ENV} clean lint assembleRelease cC zipDocuments artifactoryPublish'
                }
            } else {
                stage ('build') {
                    sh 'chmod -R 775 . && cd ./Source/DemoApp && ./gradlew --refresh-dependencies -PenvCode=${JENKINS_ENV} clean assembleRelease'
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
        
        if (env.triggerBy != "ppc" && (BranchName =~ /master|develop|release.*/)) {
        	stage ('callIntegrationPipeline') {
                if (BranchName =~ "/") {
                    BranchName = BranchName.replaceAll('/','%2F')
                    echo "BranchName changed to ${BranchName}"
                }
        		build job: "Platform-Infrastructure/ppc/ppc_android/${BranchName}", parameters: [[$class: 'StringParameterValue', name: 'componentName', value: 'dcc'],[$class: 'StringParameterValue', name: 'libraryName', value: '']], wait: false
        	}            
        }

        stage('informing') {
        	step([$class: 'StashNotifier'])
        	step([$class: 'Mailer', notifyEveryUnstableBuild: true, recipients: MailRecipient, sendToIndividuals: true])
        }

	} // end timestamps
} // end node ('android')
