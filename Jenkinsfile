#!/usr/bin/env groovy                                                                                                           

BranchName = env.BRANCH_NAME

properties([
    [$class: 'ParametersDefinitionProperty', parameterDefinitions: [[$class: 'StringParameterDefinition', defaultValue: '', description: 'triggerBy', name : 'triggerBy']]],
    [$class: 'BuildDiscarderProperty', strategy: [$class: 'LogRotator', numToKeepStr: '50']]
])


def MailRecipient = 'pascal.van.kempen@philips.com,ambati.muralikrishna@philips.com,ramesh.r.m@philips.com'

node ('android_pipeline') {
    timestamps {
        stage ('Checkout') {
            checkout([$class: 'GitSCM', branches: [[name: '*/'+BranchName]], doGenerateSubmoduleConfigurations: false, extensions: [[$class: 'CleanBeforeCheckout']], submoduleCfg: [], userRemoteConfigs: [[credentialsId: '4edede71-63a0-455e-a9dd-d250f8955958', url: 'ssh://git@atlas.natlab.research.philips.com:7999/mail/app-infra_android.git']]])
            step([$class: 'StashNotifier'])
        }
        try {
            stage ('build') {
                sh 'cd ./Source/Library && chmod -R 775 ./gradlew && ./gradlew clean assembleDebug lint cC assembleRelease zipDocuments artifactoryPublish'
            }
            
            /* next if-then + stage is mandatory for the platform CI pipeline integration */
            if (env.triggerBy != "ppc") {
                stage ('callIntegrationPipeline') {
                    if (BranchName =~ "/") {
                        BranchName = BranchName.replaceAll('/','%2F')
                        echo "BranchName changed to ${BranchName}"
                    }
                    build job: "Platform-Infrastructure/ppc/ppc_android/${BranchName}", parameters: [[$class: 'StringParameterValue', name: 'componentName', value: 'ail'],[$class: 'StringParameterValue', name: 'libraryName', value: '']]
                }            
            }
            
        } //end try
        
        catch(err) {
            error ("Someone just broke the build")
        }

        stage('informing') {
            step([$class: 'StashNotifier'])
            step([$class: 'Mailer', notifyEveryUnstableBuild: true, recipients: MailRecipient, sendToIndividuals: true])
        }

    } // end timestamps
} // end node ('android')
