#!/usr/bin/env groovy

if (!env.CHANGE_ID) {
    /* Only keep the 5 most recent builds. */
    properties([[$class: 'BuildDiscarderProperty',
                    strategy: [$class: 'LogRotator', numToKeepStr: '5']],
                    pipelineTriggers([cron('H/30 * * * *')]),
                    ])
    if (env.BRANCH_NAME =~ /release\/.*/ || env.BRANCH_NAME == 'master') {
        properties([pipelineTriggers(),])
    }
}

node('Android && 23.0.3') {
    timestamps{
        def MailRecipient = 'benit.dhotekar@philips.com, DL_CDP2_Callisto@philips.com, abhishek.gadewar@philips.com, krishna.kumar.a@philips.com, ramesh.r.m@philips.com'
        stage 'Checkout'
        checkout scm

        step([$class: 'StashNotifier'])
        try {
            //Build stuff starts
            stage 'Build'
            sh 'cd ./Source/AppFramework && ./gradlew assembleDebug'

            if(env.BRANCH_NAME == 'master') {
                stage 'Release'
                sh 'cd ./Source/AppFramework && ./gradlew zipDoc appFramework:aP'
            }

            if(env.BRANCH_NAME == 'develop') {
                stage 'Release'
                sh 'cd ./Source/AppFramework && ./gradlew zipDoc appFramework:aP'
            }

            if(env.BRANCH_NAME =~ /release\/.*/) {
                stage 'Release'
                sh 'cd ./Source/AppFramework && ./gradlew zipDoc appFramework:aP'
            }

            stage 'Notify Bitbucket'
            sh 'echo \"Check the build status in bitbucket!\"'
            //Build stuff ends

            currentBuild.result = 'SUCCESS'
        } catch(err) {
            currentBuild.result = 'FAILED'
        }
        step([$class: 'StashNotifier'])
        step([$class: 'Mailer', notifyEveryUnstableBuild: true, recipients: MailRecipient, sendToIndividuals: true])
   }
}