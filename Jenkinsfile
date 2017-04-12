#!/usr/bin/env groovy

def triggers = []

@NonCPS
def isJobStartedByTimer() {
    def startedByTimer = false
    try {
        def buildCauses = currentBuild.rawBuild.getCauses()
        for(buildCause in buildCauses) {
            if(buildCause != null) {
                def causeDescription = buildCause.getShortDescription()
                if(causeDescription.contains("Started by timer")) {
                    startedByTimer = true
                }
            }
        }
    } catch(err) {
        echo "Error determining build cause"
    }
    return startedByTimer
}

if (env.BRANCH_NAME == "develop") {
    triggers << cron('H H(18-20) * * *')
}

properties([
    [$class: 'BuildDiscarderProperty', strategy: [$class: 'LogRotator', numToKeepStr: '10']],
    pipelineTriggers(triggers),
])

def getBuildConfig() {
  return ( env.BRANCH_NAME == 'master' ) ? 'Release' : 'Debug'
}

stage('Espresso testing') {
    timestamps {
        node('android && espresso && mobile') {
            checkout([$class: 'GitSCM', branches: [[name: '*/'+env.BRANCH_NAME]], doGenerateSubmoduleConfigurations: false, extensions: [[$class: 'CheckoutOption', timeout: 30], [$class: 'WipeWorkspace'], [$class: 'PruneStaleBranch'], [$class: 'SubmoduleOption', disableSubmodules: false, parentCredentials: false, recursiveSubmodules: true, reference: '', trackingSubmodules: false]], submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'bbd4d9e8-2a6c-4970-b856-4e4cf901e857', url: 'ssh://tfsemea1.ta.philips.com:22/tfs/TPC_Region24/CDP2/_git/uid-android']]])
            bat "espresso.cmd"
           publishHTML(target: [reportDir:'Source/UIKit/uid/build/reports/androidTests/connected', reportFiles: 'index.html', reportName: 'Unit Tests'])
           publishHTML(target: [reportDir:'Source/UIKit/uid/build/reports/coverage/debug', reportFiles: 'index.html', reportName: 'Code Coverage'])
           step([$class: 'ArtifactArchiver', artifacts: 'Source/UIKit/uid/build/reports/coverage/debug/report.xml', excludes: null, fingerprint: true, onlyIfSuccessful: true])
        }
    }
}

node('Android && 25.0.0 && Ubuntu') {
  timestamps {
    def APP_ROOT = "Source/CatalogApp"
    def CONFIG = getBuildConfig()
    def VERSION = ""
    def ANDROID_RELEASE_CANDIDATE = ""
    def ANDROID_VERSION_CODE = ""
    def STARTED_BY_TIMER = isJobStartedByTimer()

    stage('Checkout') {
			checkout([$class: 'GitSCM', branches: [[name: '*/'+env.BRANCH_NAME]], doGenerateSubmoduleConfigurations: false, extensions: [[$class: 'CheckoutOption', timeout: 30], [$class: 'WipeWorkspace'], [$class: 'PruneStaleBranch'], [$class: 'SubmoduleOption', disableSubmodules: false, parentCredentials: false, recursiveSubmodules: true, reference: '', trackingSubmodules: false]], submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'bbd4d9e8-2a6c-4970-b856-4e4cf901e857', url: 'ssh://tfsemea1.ta.philips.com:22/tfs/TPC_Region24/CDP2/_git/uid-android']]])
		}

    try {
      stage('Build') {
          // Make scripts executable
          sh """#!/bin/bash -l
              chmod +x git_version.sh
              chmod +x set_version.sh
              chmod +x ${APP_ROOT}/gradlew
          """

          // Determine version
          if (env.BRANCH_NAME =~ /release\/.*/) {
              VERSION = sh(returnStdout: true, script: './git_version.sh rc').trim()
          } else {
              VERSION = sh(returnStdout: true, script: './git_version.sh snapshot').trim()
          }
          currentBuild.description = VERSION
          ANDROID_RELEASE_CANDIDATE = sh(returnStdout: true, script: "echo ${VERSION} | cut -d. -f4").trim()
          ANDROID_RELEASE_CANDIDATE = ("0000" + ANDROID_RELEASE_CANDIDATE).substring(ANDROID_RELEASE_CANDIDATE.length())
          ANDROID_VERSION_CODE = sh(returnStdout: true, script: "echo ${VERSION} | cut -d- -f1 | sed 's/[^0-9]*//g'").trim()
          ANDROID_VERSION_CODE = (ANDROID_VERSION_CODE + ANDROID_RELEASE_CANDIDATE).toInteger()

          // Print environment
          sh """#!/bin/bash -l
              echo "---------------------- Printing Environment --------------------------"
              env | sort
              echo "----------------------- End of Environment ---------------------------"

              echo "Started by timer: ${STARTED_BY_TIMER}"

              echo "Android Release Candidate: ${ANDROID_RELEASE_CANDIDATE}"
              echo "Android Version Code: ${ANDROID_VERSION_CODE}"

              ./set_version.sh ${VERSION} ${ANDROID_VERSION_CODE}
          """

          // Execute build
          sh """#!/bin/bash -l
              echo \"Building VERSION ${VERSION} for branch ${env.BRANCH_NAME} with CONFIG ${CONFIG}\"
              cd ${APP_ROOT}
              ./gradlew clean assemble${CONFIG}
          """
      }

      stage('Archive Build') {
        // Archive APK files
        step([$class: 'ArtifactArchiver', artifacts: APP_ROOT + '/app/build/outputs/apk/*debug.apk', excludes: null, fingerprint: true, onlyIfSuccessful: true])

        // Archive libraries
        step([$class: 'ArtifactArchiver', artifacts: 'Source/UIKit/uid/build/outputs/aar/uid-debug.aar', excludes: null, fingerprint: true, onlyIfSuccessful: true])
      }

      if(env.BRANCH_NAME == "develop") {
        stage('HockeyApp Upload') {
          sh """#!/bin/bash -l
            cp Documentation/External/ReleaseNotes.md ${APP_ROOT}/app/build/outputs/apk
            cd ${APP_ROOT}/app/build/outputs/apk
            curl -F \"status=2\" -F \"notify=1\" -F \"ipa=@app-debug.apk\" -F \"notes=<ReleaseNotes.md\" -H \"X-hockeyApptoken: b9d6e2f453894b4fbcb161b33a94f6c8\" https://rink.hockeyapp.net/api/2/apps/ecacf68949f344a686bed78d47449973/app_versions/upload
          """
        }
      }

      currentBuild.result = 'SUCCESS'
    } catch(err) {
      currentBuild.result = 'FAILED'
    }

    stage('Send Notifications') {
      step([$class: 'StashNotifier'])
      if(STARTED_BY_TIMER) {
            echo "Started by timer!"
            step([$class: 'Mailer', notifyEveryUnstableBuild: true, recipients: emailextrecipients([[$class: 'CulpritsRecipientProvider'], [$class: 'RequesterRecipientProvider']])])
      }
    }
  }
}
