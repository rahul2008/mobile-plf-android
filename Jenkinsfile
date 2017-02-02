#!/usr/bin/env groovy

def getArchiveConfig() {
  return ( env.BRANCH_NAME == 'master' ) ? 'Release' : 'Debug'
}

      stage('Espresso testing') {
        node('android && espresso && mobile') {
            checkout([$class: 'GitSCM', branches: [[name: '*/'+env.BRANCH_NAME]], doGenerateSubmoduleConfigurations: false, extensions: [[$class: 'CheckoutOption', timeout: 30], [$class: 'WipeWorkspace'], [$class: 'PruneStaleBranch'], [$class: 'SubmoduleOption', disableSubmodules: false, parentCredentials: false, recursiveSubmodules: true, reference: '', trackingSubmodules: false]], submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'bbd4d9e8-2a6c-4970-b856-4e4cf901e857', url: 'ssh://tfsemea1.ta.philips.com:22/tfs/TPC_Region24/CDP2/_git/uid-android']]])
		
            bat "cleanup.cmd"
            
            #bat "espresso.cmd"
       
          }
      }

      currentBuild.result = 'SUCCESS'


node('Android && 25.0.0 && Ubuntu') {
  timestamps {
    def ARCHIVE_CONFIG = getArchiveConfig()
    def VERSION = ""
    def ANDROID_RELEASE_CANDIDATE = ""
    def ANDROID_VERSION_CODE = ""

  stage('Checkout') {
			checkout([$class: 'GitSCM', branches: [[name: '*/'+env.BRANCH_NAME]], doGenerateSubmoduleConfigurations: false, extensions: [[$class: 'CheckoutOption', timeout: 30], [$class: 'WipeWorkspace'], [$class: 'PruneStaleBranch'], [$class: 'SubmoduleOption', disableSubmodules: false, parentCredentials: false, recursiveSubmodules: true, reference: '', trackingSubmodules: false]], submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'bbd4d9e8-2a6c-4970-b856-4e4cf901e857', url: 'ssh://tfsemea1.ta.philips.com:22/tfs/TPC_Region24/CDP2/_git/uid-android']]])
		}


    try {
      sh 'chmod +x git_version.sh'
      VERSION = sh(returnStdout: true, script: './git_version.sh snapshot').trim()
      ANDROID_RELEASE_CANDIDATE = sh(returnStdout: true, script: "echo ${VERSION} | cut -d. -f4").trim()
      ANDROID_RELEASE_CANDIDATE = ("00" + ANDROID_RELEASE_CANDIDATE).substring(ANDROID_RELEASE_CANDIDATE.length())
      ANDROID_VERSION_CODE = sh(returnStdout: true, script: "echo ${VERSION} | cut -d- -f1 | sed 's/[^0-9]*//g'").trim()
      ANDROID_VERSION_CODE = (ANDROID_VERSION_CODE + ANDROID_RELEASE_CANDIDATE).toInteger()

      stage('Build Catalog app') {
        sh """#!/bin/bash -l
          echo \"Building VERSION ${VERSION} for branch ${env.BRANCH_NAME} with CONFIG ${ARCHIVE_CONFIG}\"

          echo "---------------------- Printing Environment --------------------------"
          env | sort
          echo "----------------------- End of Environment ---------------------------"

          echo "Android Release Candidate: ${ANDROID_RELEASE_CANDIDATE}"
          echo "Android Version Code: ${ANDROID_VERSION_CODE}"

          chmod +x set_version.sh
          ./set_version.sh ${VERSION} ${ANDROID_VERSION_CODE}

          cd Source/CatalogApp
          ./gradlew clean assembleDebug
        """
      }

      stage('Archive Apps') {
        step([$class: 'ArtifactArchiver', artifacts: 'Source/CatalogApp/app/build/outputs/apk/*debug.apk', excludes: null, fingerprint: true, onlyIfSuccessful: true])
        step([$class: 'ArtifactArchiver', artifacts: 'Source/UIKit/uid/build/outputs/aar/uid-debug.aar', excludes: null, fingerprint: true, onlyIfSuccessful: true])
      }

      if(env.BRANCH_NAME == "develop") {
        stage('Publish') {
          sh '''#!/bin/bash -l
            cp ReleaseNotes.md Source/CatalogApp/app/build/outputs/apk
            cd Source/CatalogApp/app/build/outputs/apk
            curl -F "status=2" -F "notify=1" -F "ipa=@app-debug.apk" -F "notes=<ReleaseNotes.md" -H "X-hockeyApptoken: b9d6e2f453894b4fbcb161b33a94f6c8" https://rink.hockeyapp.net/api/2/apps/ecacf68949f344a686bed78d47449973/app_versions/upload
          '''
        }
      }

    } catch(err) {
      currentBuild.result = 'FAILED'
    }

    stage('Send Notifications') {
      step([$class: 'StashNotifier'])
      step([$class: 'Mailer', notifyEveryUnstableBuild: true, recipients: emailextrecipients([[$class: 'CulpritsRecipientProvider'], [$class: 'RequesterRecipientProvider']])])
    }
  }
}