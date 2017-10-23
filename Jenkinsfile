#!/usr/bin/env groovy                                                                                                           

BranchName = env.BRANCH_NAME
JENKINS_ENV = env.JENKINS_ENV

properties([
    [$class: 'ParametersDefinitionProperty', parameterDefinitions: [[$class: 'BooleanParameterDefinition', defaultValue: false, description: 'Force PSRA build ', name : 'PSRAbuild']]],
    [$class: 'BuildDiscarderProperty', strategy: [$class: 'LogRotator', numToKeepStr: '5']]
])

def MailRecipient = 'DL_CDP2_Callisto@philips.com, DL_App_Framework.com@philips.com'
def errors = []

node ('android&&docker') {
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

            if (!params.PSRAbuild && (BranchName =~ /master|develop|release\/platform_.*/)) {
                stage('Trigger OPA Build'){
                    def committerName = sh (script: "git show -s --format='%an' HEAD", returnStdout: true).trim()
                    if (BranchName =~ "/") {
                        BranchName = BranchName.replaceAll('/','%2F')
                        echo "BranchName changed to ${BranchName}"
                    }
                    build job: "Platform-Infrastructure/opa-android/${BranchName}", parameters: [[$class: 'StringParameterValue', name: 'committerName', value:committerName]], wait: false
                }
            }
            else
            {
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

                stage('test') {
                    echo "lint & unit test"
                   sh '''#!/bin/bash -l
                        chmod -R 755 .
                        cd ./Source/AppFramework
                        ./gradlew -PenvCode=${JENKINS_ENV} lintRelease testAppFrameworkHamburgerReleaseUnitTest
                    '''
                }

                stage ('save dependencies list') {
                    sh '''#!/bin/bash -l       
                        chmod -R 775 . 
                        cd ./Source/AppFramework 
                        ./gradlew -PenvCode=${JENKINS_ENV} saveResDep saveAllResolvedDependencies saveAllResolvedDependenciesGradleFormat
                    '''
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
