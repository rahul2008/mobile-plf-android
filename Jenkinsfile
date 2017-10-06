#!/usr/bin/env groovy  

BranchName = env.BRANCH_NAME
JENKINS_ENV = env.JENKINS_ENV

properties([
    [$class: 'ParametersDefinitionProperty', parameterDefinitions: [[$class: 'StringParameterDefinition', defaultValue: '', description: 'triggerBy', name : 'triggerBy']]],
    [$class: 'BuildDiscarderProperty', strategy: [$class: 'LogRotator', numToKeepStr: '10']]
])

def errors = []

node('Android') {
    timestamps {
        try {
            stage('Checkout') {

             def jobBaseName = "${env.JOB_BASE_NAME}".replace('%2F', '/')
                if (env.BRANCH_NAME != jobBaseName)
                { 
                   echo "ERROR: Branches DON'T MATCH"
                   echo "Branchname  = " + env.BRANCH_NAME
                   echo "jobBaseName = " + jobBaseName
                   exit 1
                }

            checkout([$class: 'GitSCM', branches: [[name: env.BRANCH_NAME]], doGenerateSubmoduleConfigurations: false, extensions: [[$class: 'CloneOption', noTags: false, reference: '', shallow: true, timeout: 30],[$class: 'WipeWorkspace'], [$class: 'PruneStaleBranch'], [$class: 'LocalBranch', localBranch: "**"]], recursiveSubmodules: true, submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'd866c69b-16f0-4fce-823a-2a42bbf90a3d', url: 'ssh://tfsemea1.ta.philips.com:22/tfs/TPC_Region24/CDP2/_git/ews-android-easywifisetupuapp']]])
            }

            stage('Build') {
                    sh '''#!/bin/bash -l
                                chmod -R 755 . 
                                cd ./Source/Library 
                                ./gradlew --refresh-dependencies -PenvCode=${JENKINS_ENV} clean assembleDebug lint 
                            '''

            }
                
            stage('Archive results') {
                echo "stage Archive results"
                archiveArtifacts artifacts: 'Source/Library/demoapplication/build/outputs/apk/*.apk', fingerprint: true, onlyIfSuccessful: true
                archiveArtifacts artifacts: 'Source/Library/ews/build/outputs/aar/*.aar', fingerprint: true, onlyIfSuccessful: true
            }

        } catch(err) {
            errors << "errors found: ${err}"
        } finally {
            stage('Clean up workspace') {
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