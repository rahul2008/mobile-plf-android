#!/usr/bin/env groovy                                                                                                           

BranchName = env.BRANCH_NAME
JENKINS_ENV = env.JENKINS_ENV

properties([
    [$class: 'ParametersDefinitionProperty', parameterDefinitions: [[$class: 'StringParameterDefinition', defaultValue: '', description: 'triggerBy', name : 'triggerBy']]],
    [$class: 'BuildDiscarderProperty', strategy: [$class: 'LogRotator', numToKeepStr: '5']]
])

def MailRecipient = 'DL_CDP2_Callisto@philips.com, DL_App_Framework.com@philips.com'

node ('android&&keystore') {
    timestamps {
        stage ('Checkout') {
            checkout([$class: 'GitSCM', branches: [[name: '*/'+BranchName]], doGenerateSubmoduleConfigurations: false, extensions: [[$class: 'WipeWorkspace'], [$class: 'PruneStaleBranch'], [$class: 'LocalBranch']], submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'd866c69b-16f0-4fce-823a-2a42bbf90a3d', url: 'ssh://tfsemea1.ta.philips.com:22/tfs/TPC_Region24/CDP2/_git/rap-android-reference-app']]])
            step([$class: 'StashNotifier'])
        }

        try {
            stage ('build') {
                sh 'cd ./Source/AppFramework && ./gradlew --refresh-dependencies -PenvCode=${JENKINS_ENV} clean assembleRelease assembleLeakCanary'
            }
            
            sh 'cd ./Source/AppFramework && ./gradlew --refresh-dependencies -PenvCode=${JENKINS_ENV} assembleRelease assembleLeakCanary'

            if(env.BRANCH_NAME == 'master') {
                stage ('Release') {
                    sh 'cd ./Source/AppFramework && ./gradlew zipDoc appFramework:aP'
                }
            }

            if(env.BRANCH_NAME =~ 'develop' ) {
                stage ('Release') {
                    sh 'cd ./Source/AppFramework && ./gradlew zipDoc appFramework:aP'
                }
            }

            if(env.BRANCH_NAME =~ /release\/.*/) {
                stage ('Release') {
                    sh 'cd ./Source/AppFramework && ./gradlew zipDoc appFramework:aP'
                }
            }

            stage ('save dependencies list') {
            	sh 'chmod -R 775 . && cd ./Source/AppFramework && ./gradlew -PenvCode=${JENKINS_ENV} saveResDep'
            }
            archiveArtifacts '**/dependencies.lock'

            currentBuild.result = 'SUCCESS'
            archiveArtifacts '**/build/**/*.apk'
        }

        catch(err) {
            currentBuild.result = 'FAILURE'
            error ("Someone just broke the build")
        }

        if (env.triggerBy != "ppc" && !(BranchName =~ "eature")) {
            stage ('callIntegrationPipeline') {
                if (BranchName =~ "/") {
                    BranchName = BranchName.replaceAll('/','%2F')
                    echo "BranchName changed to ${BranchName}"
                }
                build job: "Platform-Infrastructure/ppc/ppc_android/${BranchName}", parameters: [[$class: 'StringParameterValue', name: 'componentName', value: 'rap'],[$class: 'StringParameterValue', name: 'libraryName', value: '']], wait: false
            }
        }

        stage('informing') {
            step([$class: 'StashNotifier'])
            step([$class: 'Mailer', notifyEveryUnstableBuild: true, recipients: MailRecipient, sendToIndividuals: true])
        }

    }
}