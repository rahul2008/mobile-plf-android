#!/usr/bin/env groovy                                                                                                           
BranchName = env.BRANCH_NAME
JENKINS_ENV = env.JENKINS_ENV

properties([
    [$class: 'ParametersDefinitionProperty', parameterDefinitions: [[$class: 'StringParameterDefinition', defaultValue: '', description: 'triggerBy', name : 'triggerBy']]],
    [$class: 'BuildDiscarderProperty', strategy: [$class: 'LogRotator', numToKeepStr: '10']],
    pipelineTriggers([cron('0 * * * *')]),
])

def MailRecipient = 'DL_CDP2_Callisto@philips.com'
def errors = []

node ('opa') {
    timestamps {
        try {
		    stage('Cleaning workspace before build') {
				step([$class: 'WsCleanup', deleteDirs: true, notFailBuild: false])
            }

			stage ('Checkout') {
                checkout poll: false, scm: [$class: 'GitSCM', branches: [[name: '**']], doGenerateSubmoduleConfigurations: false, extensions: [[$class: 'SubmoduleOption', disableSubmodules: false, parentCredentials: true, recursiveSubmodules: true, reference: '', trackingSubmodules: false], [$class: 'LocalBranch', localBranch: "**"]], submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'bbd4d9e8-2a6c-4970-b856-4e4cf901e857', url: 'ssh://tfsemea1.ta.philips.com:22/tfs/TPC_Region24/CDP2/_git/opa-android']]]
            }
			
			stage ('update') {
			//	sh '''#!/bin/bash -l
			//	    echo Updating
			//		set -e
			//	    echo Reset
			//		git submodule foreach --recursive git reset --hard
			//		echo Update components
			//		bash ./update_components.sh pull
			//	'''
			}

			stage ('build') {
				sh '''#!/bin/bash -l
					set -e
					chmod -R 755 . 
					#do not use -PenvCode=${JENKINS_ENV} since the option 'opa' is hardcoded in the archive
					./gradlew --refresh-dependencies clean 
					./gradlew assembleRelease
				'''
			}
			stage ('unit test') {
				sh '''#!/bin/bash -l
					set -e
					chmod -R 755 . 
					#do not use -PenvCode=${JENKINS_ENV} since the option 'opa' is hardcoded in the archive
					./gradlew :ail:cC :uid:createDebugCoverageReport :icf:test :usr:cC :usr:test :iap:test :dcc:cC :dcc:testRelease :pse:cC :ufw:test :prg:test :prg:jacocoTestReport :dsc:testReleaseUnitTest :dpr:test :rap:testAppFrameworkHamburgerReleaseUnitTest :commlib:test :bluelib:test :commlib-all:testDebug :commlib-all:pitestDebug :sdb:cC
				'''
			}
			stage ('lint') {
				sh '''#!/bin/bash -l
					set -e
					chmod -R 755 . 
					#do not use -PenvCode=${JENKINS_ENV} since the option 'opa' is hardcoded in the archive
					./gradlew :ail:lint :uit:lint :icf:lint :usr:lint :iap:lint :dcc:lint :pse:lint :prg:lint :dsc:lintRelease :dpr:lint :cloudcontroller-api:lintDebug :commlib:lintDebug :bluelib:lintDebug :commlib-all:lintDebug
				    #prx:lint and rap:lintRelease are not working and we are keeping it as known issues
				'''
			}
			stage ('publish') {
				sh '''#!/bin/bash -l
					set -e
					./gradlew artifactoryPublish :rap:printArtifactoryApkPath
				'''
			}
			stage ('push success to OPA archive') {
				sh '''#!/bin/bash -l
					set -e
					git push --set-upstream origin develop
				'''
			}			
			// stage('E2E test') {
			// 	APK_NAME = readFile("apkname.txt").trim()
			// 	echo "APK_NAME = ${APK_NAME}"
			// 	build job: "Platform-Infrastructure/E2E_Tests/E2E_Android_develop", parameters: [[$class: 'StringParameterValue', name: 'APKPATH', value:APK_NAME]], wait: true
			// }
			
        } catch(err) {
            errors << "errors found: ${err}"      
        } finally {
            if (errors.size() > 0) {
                stage ('error reporting') {
                    currentBuild.result = 'FAILURE'
                    for (int i = 0; i < errors.size(); i++) {
                        echo errors[i]; 
                    }
                }                
            } 
            stage('informing') {
                step([$class: 'Mailer', notifyEveryUnstableBuild: true, recipients: MailRecipient, sendToIndividuals: true])
            }
            //stage('Cleaning workspace') {
            //    step([$class: 'WsCleanup', deleteDirs: true, notFailBuild: true])
            //}
        }         
    } // end timestamps
} // end node ('android')

node('master') {
    stage('Cleaning workspace') {
        def wrk = pwd() + "@script/"
        dir("${wrk}") {
            deleteDir()
        }
    }
}