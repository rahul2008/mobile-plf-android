#!/usr/bin/env groovy                                                                                                           

/* please see ReadMe.md for explanation */ 


BranchName = env.BRANCH_NAME
JENKINS_ENV = env.JENKINS_ENV

properties([
    [$class: 'ParametersDefinitionProperty', parameterDefinitions: [[$class: 'StringParameterDefinition', defaultValue: '', description: 'triggerBy', name : 'triggerBy']]],
    [$class: 'BuildDiscarderProperty', strategy: [$class: 'LogRotator', numToKeepStr: '10']]
])

def MailRecipient = 'DL_CDP2_Callisto@philips.com,DL_CDP2_TeamSabers@philips.com'
def errors = []

node ('android&&device') {
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

                checkout([$class: 'GitSCM', branches: [[name: '*/'+BranchName]], doGenerateSubmoduleConfigurations: false, extensions: [[$class: 'CloneOption', noTags: false, reference: '', shallow: true, timeout: 30],[$class: 'WipeWorkspace'], [$class: 'PruneStaleBranch'], [$class: 'LocalBranch', localBranch: "**"]], submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'd866c69b-16f0-4fce-823a-2a42bbf90a3d', url: 'ssh://tfsemea1.ta.philips.com:22/tfs/TPC_Region24/CDP2/_git/dcc-android-consumercare-app']]])
                step([$class: 'StashNotifier'])
            }
            if (BranchName =~ /master|develop|release\/platform_.*/) {
                stage ('build') {
                    sh '''#!/bin/bash -l
                        chmod -R 755 . 
                        cd ./Source/DemoApp 
                        ./gradlew --refresh-dependencies -PenvCode=${JENKINS_ENV} clean assembleDebug lint
                        ./gradlew -PenvCode=${JENKINS_ENV} assembleRelease cC testRelease saveResDep zipDocuments artifactoryPublish
                    '''
                }
            } else {
                stage ('build') {
                    sh '''#!/bin/bash -l
                        chmod -R 755 . 
                        cd ./Source/DemoApp 
                        ./gradlew --refresh-dependencies -PenvCode=${JENKINS_ENV} clean assembleDebug lint 
                        ./gradlew -PenvCode=${JENKINS_ENV} assembleRelease cC testRelease
                    '''
                }
            }
            stage ('save dependencies list') {
                sh '''#!/bin/bash -l
                    cd ./Source/DemoApp 
                    ./gradlew -PenvCode=${JENKINS_ENV} saveResDep saveAllResolvedDependencies saveAllResolvedDependenciesGradleFormat                  
                    cd ../Library 
                    ./gradlew -PenvCode=${JENKINS_ENV} saveResDep saveAllResolvedDependencies saveAllResolvedDependenciesGradleFormat
                '''
            }

            if (env.triggerBy != "ppc" && (BranchName =~ /master|develop|release\/platform_.*/)) {
                stage ('callIntegrationPipeline') {
                    if (BranchName =~ "/") {
                        BranchName = BranchName.replaceAll('/','%2F')
                        echo "BranchName changed to ${BranchName}"
                    }
                    build job: "Platform-Infrastructure/ppc/ppc_android/${BranchName}", parameters: [[$class: 'StringParameterValue', name: 'componentName', value: 'dcc'],[$class: 'StringParameterValue', name: 'libraryName', value: '']], wait: false
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
            stage ('reporting') {
                androidLint canComputeNew: false, canRunOnFailed: true, defaultEncoding: '', healthy: '', pattern: '', shouldDetectModules: true, unHealthy: '', unstableTotalHigh: ''
                junit allowEmptyResults: true, testResults: 'Source/DemoApp/launchDigitalCare/build/reports/lint-results.xml'
                junit allowEmptyResults: true, testResults: 'Source/DemoUApp/DemoUApp/build/reports/lint-results.xml'
                junit allowEmptyResults: true, testResults: 'Source/Library/digitalCare/build/test-results/**/*.xml'                
                publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'Source/Library/digitalCare/build/reports/tests/testReleaseUnitTest', reportFiles: 'index.html', reportName: 'unit test release'])
                archiveArtifacts '**/*dependencies*.lock'
            }
            stage('informing') {
                step([$class: 'StashNotifier'])
                step([$class: 'Mailer', notifyEveryUnstableBuild: true, recipients: MailRecipient, sendToIndividuals: true])
            }
            stage('Cleaning workspace') {
                step([$class: 'WsCleanup', deleteDirs: true, notFailBuild: true])
            }
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