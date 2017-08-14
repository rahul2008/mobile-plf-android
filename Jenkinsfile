#!/usr/bin/env groovy                                                                                                           

BranchName = env.BRANCH_NAME
JENKINS_ENV = env.JENKINS_ENV

properties([
    [$class: 'ParametersDefinitionProperty', parameterDefinitions: [[$class: 'StringParameterDefinition', defaultValue: '', description: 'triggerBy', name : 'triggerBy']]],
    [$class: 'BuildDiscarderProperty', strategy: [$class: 'LogRotator', numToKeepStr: '5']]
])

def MailRecipient = 'DL_CDP2_Callisto@philips.com, DL_App_Framework.com@philips.com'
def errors = []

node ('android&&docker') {
    timestamps {
        try {
            stage ('Checkout') {
                checkout([$class: 'GitSCM', branches: [[name: '*/'+BranchName]], doGenerateSubmoduleConfigurations: false, extensions: [[$class: 'WipeWorkspace'], [$class: 'PruneStaleBranch'], [$class: 'LocalBranch']], submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'd866c69b-16f0-4fce-823a-2a42bbf90a3d', url: 'ssh://tfsemea1.ta.philips.com:22/tfs/TPC_Region24/CDP2/_git/rap-android-reference-app']]])
                step([$class: 'StashNotifier'])
            }
            stage ('build') {
            sh '''#!/bin/bash -l
                chmod -R 775 .
                cd ./Source/AppFramework 
                ./gradlew --refresh-dependencies -PenvCode=${JENKINS_ENV} clean assembleRelease
            '''
            }
			if (BranchName =~ /master|release.*/) {
			stage ('build PSRA') {
            sh '''#!/bin/bash -l
                chmod -R 775 .
                cd ./Source/AppFramework
				./gradlew -PenvCode=${JENKINS_ENV} assemblePsraRelease
             '''
             }
		    }
            stage('test') {
                echo "lint & unit test"
               sh '''#!/bin/bash -l
                    chmod -R 755 .
                    cd ./Source/AppFramework
                    ./gradlew -PenvCode=${JENKINS_ENV} lintRelease testAppFrameworkHamburgerReleaseUnitTest
                '''
            }
            
            if (BranchName =~ /master|develop|release.*/) {
                stage('publish') {
                    echo "publish to artifactory"
                    sh '''#!/bin/bash -l
                        chmod -R 755 .
                        cd ./Source/AppFramework
                        ./gradlew -PenvCode=${JENKINS_ENV} zipDoc appFramework:aP :appFramework:printArtifactoryApkPath
                    '''
                }
                // HockeyApp publishing disabled on request of Raymond Kloprogge (2017-08-02)
                //stage('hockeyapp upload') {
                //    echo "Uploading to HockeyApp"
                //    sh '''#!/bin/bash -l
                //        chmod -R 755 .
                //        cd ./Source/AppFramework
                //        ./gradlew -PenvCode=${JENKINS_ENV} uploadToHockeyApp
                //    '''
                //}
            }
            stage ('save dependencies list') {
                sh '''#!/bin/bash -l       
                    chmod -R 775 . 
                    cd ./Source/AppFramework 
                    ./gradlew -PenvCode=${JENKINS_ENV} :appFramework:saveResDep :appFramework:saveAllResolvedDependencies :appFramework:saveAllResolvedDependenciesGradleFormat
                ''' 
            }
            stage('Trigger E2E Test'){
                APK_NAME = readFile("Source/AppFramework/apkname.txt").trim()
                if (BranchName =~ /master|develop|release.*/) {
                    if (BranchName =~ /develop.*/) {
                        BranchName = "develop"
                        echo "BranchName changed to ${BranchName}"
                    }
                    if (BranchName =~ /release.*/) {
                        BranchName = "release"
                        echo "BranchName changed to ${BranchName}"
                    }
                    if (BranchName =~ /master.*/) {
                        BranchName = "release"
                        echo "BranchName changed to ${BranchName}"
                    }
                    echo "APK_NAME = ${APK_NAME}"
                    build job: "Platform-Infrastructure/E2E_Tests/E2E_Android_${BranchName}", parameters: [[$class: 'StringParameterValue', name: 'APKPATH', value:APK_NAME]], wait: false
                }
            }
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
            stage ('reporting and archiving') {
                androidLint canComputeNew: false, canRunOnFailed: true, defaultEncoding: '', healthy: '', pattern: '', shouldDetectModules: true, unHealthy: '', unstableTotalHigh: ''
                junit allowEmptyResults: false, testResults: 'Source/AppFramework/*/build/test-results/*/*.xml' 
                publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'Source/AppFramework/appFramework/build/reports/tests/testAppFrameworkHamburgerReleaseUnitTest', reportFiles: 'index.html', reportName: 'AppFramework Hamburger Release UnitTest'])  
                archiveArtifacts '**/*dependencies*.lock'
                archiveArtifacts '**/build/**/*.apk'
            }
            stage('informing') {
                step([$class: 'Mailer', notifyEveryUnstableBuild: true, recipients: MailRecipient, sendToIndividuals: true])
            }
            stage('Cleaning workspace') {
                step([$class: 'WsCleanup', deleteDirs: true, notFailBuild: true])
            }            
        }
    }
}

node('master') {
    stage('Cleaning workspace') {
        def wrk = pwd() + "@script/"
        dir("${wrk}") {
            deleteDir()
        }
    }
}
