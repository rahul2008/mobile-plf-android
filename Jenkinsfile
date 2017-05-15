#!/usr/bin/env groovy																											

BranchName = env.BRANCH_NAME
JENKINS_ENV = env.JENKINS_ENV

properties([
    [$class: 'ParametersDefinitionProperty', parameterDefinitions: [[$class: 'StringParameterDefinition', defaultValue: '', description: 'triggerBy', name : 'triggerBy']]],
    [$class: 'BuildDiscarderProperty', strategy: [$class: 'LogRotator', numToKeepStr: '10']]
])

def MailRecipient = 'DL_CDP2_Callisto@philips.com, DL_CDP2_MobileUIToolkit@philips.com, ambati.muralikrishna@philips.com'

node ('android') {
	timestamps {
		stage ('Checkout') {
			checkout([$class: 'GitSCM', branches: [[name: '*/'+BranchName]], doGenerateSubmoduleConfigurations: false, extensions: [[$class: 'CloneOption', noTags: false, reference: '', shallow: true, timeout: 30], [$class: 'WipeWorkspace'], [$class: 'PruneStaleBranch'], [$class: 'LocalBranch']], submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'bbd4d9e8-2a6c-4970-b856-4e4cf901e857', url: 'ssh://tfsemea1.ta.philips.com:22/tfs/TPC_Region24/CDP2/_git/uit-android']]])
			step([$class: 'StashNotifier'])
		}
		try {
            if (BranchName =~ /master|develop|release.*/) {
                stage ('build') {
                    sh '''#!/bin/bash -l
                        chmod -R 775 . 
                        cd ./Source/CatalogApp 
                        ./gradlew --refresh-dependencies -PenvCode=${JENKINS_ENV} clean assembleDebug lint
                        ./gradlew -PenvCode=${JENKINS_ENV} Release zipDocuments artifactoryPublish
                    '''
                }
            } else {
                stage ('build') {
                    sh '''#!/bin/bash -l
                        chmod -R 775 . 
                        cd ./Source/CatalogApp 
                        ./gradlew --refresh-dependencies -PenvCode=${JENKINS_ENV} clean assembleDebug lint
                        ./gradlew -PenvCode=${JENKINS_ENV} Release 
                    '''
                }
            }
			stage ('save dependencies list') {
                sh '''#!/bin/bash -l
            	   chmod -R 775 . 
                   cd ./Source/CatalogApp 
                   ./gradlew -PenvCode=${JENKINS_ENV} saveResDep
            	   cd ../UiKit 
                   ./gradlew -PenvCode=${JENKINS_ENV} saveResDep
                '''
            }

           stage ('reporting') {
                androidLint canComputeNew: false, canRunOnFailed: true, defaultEncoding: '', healthy: '', pattern: '', shouldDetectModules: true, unHealthy: '', unstableTotalHigh: '0'
                junit allowEmptyResults: true, testResults: 'Source/Library/*/build/test-results/*/*.xml'
                // publishHTML([allowMissing: true, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'Source/UiKit/uikitlib/build/reports/tests/debug', reportFiles: 'index.html', reportName: 'unit test debug']) 
                archiveArtifacts '**/dependencies.lock'
            }


        } catch(err) {
            currentBuild.result = 'FAILURE'
            error ("Someone just broke the build", err.toString())
        }        
			
        if (env.triggerBy != "ppc" && (BranchName =~ /master|develop|release.*/)) {
        	stage ('callIntegrationPipeline') {
                if (BranchName =~ "/") {
                    BranchName = BranchName.replaceAll('/','%2F')
                    echo "BranchName changed to ${BranchName}"
                }
        		build job: "Platform-Infrastructure/ppc/ppc_android/${BranchName}", parameters: [[$class: 'StringParameterValue', name: 'componentName', value: 'uit'],[$class: 'StringParameterValue', name: 'libraryName', value: '']], wait: false
        	}            
        }

        stage('informing') {
        	step([$class: 'StashNotifier'])
        	step([$class: 'Mailer', notifyEveryUnstableBuild: true, recipients: MailRecipient, sendToIndividuals: true])
        }

	} 
}
