#!/usr/bin/env groovy                                                                                                           

BranchName = env.BRANCH_NAME

properties([
    [$class: 'ParametersDefinitionProperty', parameterDefinitions: [[$class: 'StringParameterDefinition', defaultValue: '', description: 'triggerBy', name : 'triggerBy']]],
    [$class: 'BuildDiscarderProperty', strategy: [$class: 'LogRotator', numToKeepStr: '25']]
])

def MailRecipient = 'benit.dhotekar@philips.com, DL_CDP2_Callisto@philips.com, abhishek.gadewar@philips.com, krishna.kumar.a@philips.com, ramesh.r.m@philips.com'

node_ext = "build_t"
if (env.triggerBy == "ppc") {
  node_ext = "build_p"
}

node ('Ubuntu && 23.0.3 &&' + node_ext) {
    timestamps {
        stage ('Checkout') {
            checkout([$class: 'GitSCM', branches: [[name: '*/'+BranchName]], doGenerateSubmoduleConfigurations: false, extensions: [[$class: 'WipeWorkspace'], [$class: 'PruneStaleBranch'], [$class: 'LocalBranch']], submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'd866c69b-16f0-4fce-823a-2a42bbf90a3d', url: 'ssh://tfsemea1.ta.philips.com:22/tfs/TPC_Region24/CDP2/_git/rap-android-reference-app']]])
            step([$class: 'StashNotifier'])
        }

        try {
            stage ('build') {
                sh 'cd ./Source/AppFramework && ./gradlew clean assembleDebug assembleLeakCanary'
            }
            
            sh 'cd ./Source/AppFramework && ./gradlew assembleDebug assembleLeakCanary'

            if(env.BRANCH_NAME == 'master') {
                stage ('Release') {
                    sh 'cd ./Source/AppFramework && chmod -R 775 ../../check_and_delete_artifact.sh && ../../check_and_delete_artifact.sh referenceApp && ./gradlew zipDoc appFramework:aP'
                }
            }

            if(env.BRANCH_NAME == 'develop') {
                stage ('Release') {
                    sh 'cd ./Source/AppFramework && chmod -R 775 ../../check_and_delete_artifact.sh && ../../check_and_delete_artifact.sh referenceApp && ./gradlew zipDoc appFramework:aP'
                }
            }

            if(env.BRANCH_NAME =~ /release\/.*/) {
                stage ('Release') {
                    sh 'cd ./Source/AppFramework && chmod -R 775 ../../check_and_delete_artifact.sh && ../../check_and_delete_artifact.sh referenceApp && ./gradlew zipDoc appFramework:aP'
                }
            }

            currentBuild.result = 'SUCCESS'
            archiveArtifacts '**/build/**/*.apk'
        }

        catch(err) {
            currentBuild.result = 'FAILURE'
            error ("Someone just broke the build")
        }

        try {
            if (env.triggerBy != "ppc" && !(BranchName =~ "eature")) {
                stage ('callIntegrationPipeline') {
                    if (BranchName =~ "/") {
                        BranchName = BranchName.replaceAll('/','%2F')
                        echo "BranchName changed to ${BranchName}"
                    }
                    build job: "Platform-Infrastructure/ppc/ppc_android/${BranchName}", parameters: [[$class: 'StringParameterValue', name: 'componentName', value: 'rap'],[$class: 'StringParameterValue', name: 'libraryName', value: '']]
                }
            }

            currentBuild.result = 'SUCCESS'

        }

        catch(err) {
            currentBuild.result = 'UNSTABLE'
        }

        stage('informing') {
            step([$class: 'StashNotifier'])
            step([$class: 'Mailer', notifyEveryUnstableBuild: true, recipients: MailRecipient, sendToIndividuals: true])
        }

    }
}