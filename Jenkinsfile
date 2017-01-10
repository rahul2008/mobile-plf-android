#!/usr/bin/env groovy

def getArchiveConfig() {
  return ( env.BRANCH_NAME == 'master' ) ? 'Release' : 'Debug'
}

node('Android && 25.0.0 && Ubuntu') {
  timestamps {
    def ARCHIVE_CONFIG = getArchiveConfig()
    sh "echo \"The branch is ${env.BRANCH_NAME} -> Using Config ${ARCHIVE_CONFIG}\""

    stage('Checkout') {
      checkout scm
      step([$class: 'StashNotifier'])
    }

    try {
      stage('Build Catalog app') {
        sh '''#!/bin/bash -l
          cd Source/CatalogApp
          ./gradlew clean assembleDebug
        '''
      }

      stage('Archive Apps') {
        step([$class: 'ArtifactArchiver', artifacts: 'Source/CatalogApp/app/build/outputs/apk/*debug.apk', excludes: null, fingerprint: true, onlyIfSuccessful: true])
      }

      if(env.BRANCH_NAME == "develop") {
        stage('Publish') {
          sh '''#!/bin/bash -l
            cd Source/CatalogApp/app/build/outputs/apk
            curl -F "status=2" -F "notify=1" -F "ipa=@app-debug.apk" -H "X-hockeyApptoken: b9d6e2f453894b4fbcb161b33a94f6c8" https://rink.hockeyapp.net/api/2/apps/ecacf68949f344a686bed78d47449973/app_versions/upload
          '''
        }
      }
    
      currentBuild.result = 'SUCCESS'
    } catch(err) {
      currentBuild.result = 'FAILED'
    }

    stage('Send Notifications') {
      step([$class: 'StashNotifier'])
      step([$class: 'Mailer', notifyEveryUnstableBuild: true, recipients: emailextrecipients([[$class: 'CulpritsRecipientProvider'], [$class: 'RequesterRecipientProvider']])])
    }
  }
}