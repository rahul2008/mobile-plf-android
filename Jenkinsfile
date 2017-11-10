#!/usr/bin/env groovy                                                                                                           

BranchName = env.BRANCH_NAME
JENKINS_ENV = env.JENKINS_ENV

properties([
    [$class: 'ParametersDefinitionProperty', parameterDefinitions: [
        [$class: 'StringParameterDefinition', defaultValue: '', description: 'triggerBy', name : 'triggerBy'],
        [$class: 'BooleanParameterDefinition', defaultValue: false, description: 'Force PSRA build ', name : 'PSRAbuild'],
		[$class: 'BooleanParameterDefinition', defaultValue: false, description: 'LeakCanary build ', name : 'LeakCanarybuild']
    ]],

    [$class: 'BuildDiscarderProperty', strategy: [$class: 'LogRotator', numToKeepStr: '20']]

])

def MailRecipient = 'DL_CDP2_Callisto@philips.com, DL_App_Framework.com@philips.com'
def errors = []

node ('Platform-Android-Ehv-003') {
    timestamps {
        try {
            stage ('Checkout') {
                def jobBaseName = "${env.JOB_BASE_NAME}".replace('%2F', '/')
                if (env.BRANCH_NAME != jobBaseName)
                { 
                   echo "ERROR: Branches DON'T MATCH"
                   echo "Branchname  = " + env.BRANCH_NAME
                   echo "jobBaseName = " + jobBaseName
                   exit 1
                }

                checkout([$class: 'GitSCM', branches: [[name: '*/'+BranchName]], doGenerateSubmoduleConfigurations: false, extensions: [[$class: 'WipeWorkspace'], [$class: 'PruneStaleBranch'], [$class: 'LocalBranch', localBranch: "**"]], submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'd866c69b-16f0-4fce-823a-2a42bbf90a3d', url: 'ssh://tfsemea1.ta.philips.com:22/tfs/TPC_Region24/CDP2/_git/rap-android-reference-app']]])
                step([$class: 'StashNotifier'])
            }
            stage ('build') {
            sh '''#!/bin/bash -l
                chmod -R 775 .
                cd ./Source/AppFramework 
                ./gradlew --refresh-dependencies -PenvCode=${JENKINS_ENV} clean assembleRelease
            '''
            }
			if (params.PSRAbuild || (BranchName =~ /master|release\/platform_.*/))  {
			stage ('build PSRA') {
            sh '''#!/bin/bash -l
                chmod -R 775 .
                cd ./Source/AppFramework
				./gradlew -PenvCode=${JENKINS_ENV} assemblePsraRelease
             '''
             }
		    }
			if (params.LeakCanarybuild || (BranchName =~ /master|release\/platform_.*/))  {
			stage ('build LeakCanary') {
            sh '''#!/bin/bash -l
                chmod -R 775 .
                cd ./Source/AppFramework
				./gradlew -PenvCode=${JENKINS_ENV} assembleLeakCanary
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
            
            if (params.PSRAbuild || (BranchName =~ /master|develop|release\/platform_.*/)) {
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
			
			if (params.LeakCanarybuild || (BranchName =~ /master|develop|release\/platform_.*/)) {
            stage('publishing leakcanaryapps') {
                boolean MasterBranch = (BranchName ==~ /master.*/)
                boolean ReleaseBranch = (BranchName ==~ /release\/platform_.*/)
                boolean DevelopBranch = (BranchName ==~ /develop.*/)

                def shellcommand = '''#!/bin/bash -l
                    export BASE_PATH=`pwd`
                    echo $BASE_PATH
                    TIMESTAMP=`date -u +%Y%m%d%H%M%S`
                    TIMESTAMPEXTENSION=".$TIMESTAMP"

                    cd $BASE_PATH/Source/AppFramework/appFramework/build/outputs/apk
                    PUBLISH_IPA=false
                    APK_NAME="RefApp_LeakCanary_"${TIMESTAMP}".apk"
                    ARTIFACTORY_URL="http://artifactory-ehv.ta.philips.com:8082/artifactory"
                    ARTIFACTORY_REPO="unknown"

                    if [ '''+MasterBranch+''' = true ]
                    then
                        PUBLISH_APK=true
                        ARTIFACTORY_REPO="platform-pkgs-android-release"
                    elif [ '''+ReleaseBranch+''' = true ]
                    then
                        PUBLISH_APK=true
                        ARTIFACTORY_REPO="platform-pkgs-android-stage"
                    elif [ '''+DevelopBranch+''' = true ]
                    then
                        PUBLISH_APK=true
                        ARTIFACTORY_REPO="platform-pkgs-android-snapshot"
                    else
                        echo "Not published as build is not on a master, develop or release branch" . $BranchName
                    fi

                    if [ $PUBLISH_APK = true ]
                    then
                        mv referenceApp-leakCanary.apk $APK_NAME
                        curl -L -u readerwriter:APBcfHoo7JSz282DWUzMVJfUsah -X PUT $ARTIFACTORY_URL/$ARTIFACTORY_REPO/com/philips/cdp/referenceApp/LeakCanary/ -T $APK_NAME
                    fi

                    if [ $? != 0 ]
                    then
                        exit 1
                    else
                        cd $BASE_PATH
                    fi
                '''
                sh shellcommand
            }
            }
            stage ('save dependencies list') {
                sh '''#!/bin/bash -l       
                    chmod -R 775 . 
                    cd ./Source/AppFramework 
                    ./gradlew -PenvCode=${JENKINS_ENV} saveResDep saveAllResolvedDependencies saveAllResolvedDependenciesGradleFormat
                ''' 
            }
			
            stage('Trigger E2E Test'){
                if (BranchName =~ /master|develop|release\/platform_.*/) {
					echo "APK_NAME = ${APK_NAME}"
					if(params.LeakCanarybuild){                    
						build job: "Platform-Infrastructure/E2E_Tests/Reliability/LeakCanary_Android_develop", parameters: [[$class: 'StringParameterValue', name: 'APKPATH', value:APK_NAME]], wait: false
					} else {
					  APK_NAME=readFile("Source/AppFramework/apkname.txt").trim()
						def jobBranchName = BranchName.replace('/', '_')
						echo "jobBranchName = ${jobBranchName}"
						build job: "Platform-Infrastructure/E2E_Tests/E2E_Android_${jobBranchName}", parameters: [[$class: 'StringParameterValue', name: 'APKPATH', value:APK_NAME]], wait: false
						}
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
