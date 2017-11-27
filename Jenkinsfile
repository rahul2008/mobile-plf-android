#!/usr/bin/env groovy                                                                                                           

/* please see ReadMe.md for explanation */ 


BranchName = env.BRANCH_NAME
JENKINS_ENV = env.JENKINS_ENV

properties([
    [$class: 'ParametersDefinitionProperty', parameterDefinitions: [[$class: 'StringParameterDefinition', defaultValue: '', description: 'triggerBy', name : 'triggerBy']]],
    [$class: 'BuildDiscarderProperty', strategy: [$class: 'LogRotator', numToKeepStr: '10']]
])

def MailRecipient = 'DL_CDP2_Callisto@philips.com,DL_CDP2_OneBackend@philips.com'
def errors = []

node ('android&&docker') {
    timestamps {
        try {
            stage ('Checkout') {
                checkout([$class: 'GitSCM', branches: [[name: '*/'+BranchName]], doGenerateSubmoduleConfigurations: false, extensions: [[$class: 'CloneOption', noTags: false, reference: '', shallow: true, timeout: 30],[$class: 'WipeWorkspace'], [$class: 'PruneStaleBranch'], [$class: 'LocalBranch']], submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'd866c69b-16f0-4fce-823a-2a42bbf90a3d', url: 'ssh://tfsemea1.ta.philips.com:22/tfs/TPC_Region24/CDP2/_git/mya-android-my-account']]])
                step([$class: 'StashNotifier'])
            }
            if (BranchName =~ /master|develop|release\/platform_.*/) {
                stage ('build') {
                    sh '''#!/bin/bash -l
                        chmod -R 755 . 
                        cd ./Source/DemoApp 
                        ./gradlew --refresh-dependencies -PenvCode=${JENKINS_ENV} clean assembleDebug lint
                        ./gradlew -PenvCode=${JENKINS_ENV} assembleRelease cC testRelease zipDocuments artifactoryPublish
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
                    build job: "Platform-Infrastructure/ppc/ppc_android/${BranchName}", parameters: [[$class: 'StringParameterValue', name: 'componentName', value: 'mya'],[$class: 'StringParameterValue', name: 'libraryName', value: '']], wait: false
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
                junit allowEmptyResults: true, testResults: 'Source/DemoApp/app/build/test-results/**/*.xml'
                junit allowEmptyResults: true, testResults: 'Source/DemoUApp/DemoUApp/build/test-results/**/*.xml'
                junit allowEmptyResults: true, testResults: 'Source/Library/ConsentAccessToolkit/build/test-results/**/*.xml'
                junit allowEmptyResults: true, testResults: 'Source/Library/ConsentWidgets/build/test-results/**/*.xml'
                junit allowEmptyResults: true, testResults: 'Source/Library/MyAccount/build/test-results/**/*.xml'
                
                publishHTML([allowMissing: true, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'Source/DemoApp/app/build/reports/tests/testReleaseUnitTest', reportFiles: 'index.html', reportName: 'DemoApp - release test'])
                
                publishHTML([allowMissing: true, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'Source/DemoUApp/DemoUApp/build/reports/tests/testReleaseUnitTest', reportFiles: 'index.html', reportName: 'DemoUApp - release test'])
                
                publishHTML([allowMissing: true, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'Source/Library/ConsentAccessToolkit/build/reports/tests/testReleaseUnitTest', reportFiles: 'index.html', reportName: 'ConsentAccessToolkit - release test'])
                
                publishHTML([allowMissing: true, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'Source/Library/ConsentWidgets/build/reports/tests/testReleaseUnitTest', reportFiles: 'index.html', reportName: 'ConsentWidgets - release test'])
                
                publishHTML([allowMissing: true, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'Source/Library/MyAccount/build/reports/tests/testReleaseUnitTest', reportFiles: 'index.html', reportName: 'MyAccount - release test'])
                
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
