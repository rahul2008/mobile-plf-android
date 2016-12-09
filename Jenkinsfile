def getConfig() {
  return ( env.BRANCH_NAME == 'master' ) ? 'Release' : 'Debug'
}

node('Android && 25.0.0 && Ubuntu') {
  def CONFIG = getConfig()
  sh "echo \"The branch is ${env.BRANCH_NAME} -> Using Config ${CONFIG}\""

  stage('Checkout') {
    checkout scm
  }

  stage('Build Catalog app') {
    sh '''#!/bin/bash -xl
      cd Source/CatalogApp
      ./gradlew clean assembleDebug
    '''
  }

  stage('Archive Apps') {
    step([$class: 'ArtifactArchiver', artifacts: 'Source/CatalogApp/app/build/outputs/apk/*debug.apk', excludes: null, fingerprint: true, onlyIfSuccessful: true])
  }

  if(env.BRANCH_NAME == "develop") {
    stage('Publish') {
      sh '''#!/bin/bash -xl
        cd Source/CatalogApp/app/build/outputs/apk
        curl -F "status=2" -F "notify=1" -F "ipa=@app-debug.apk" -H "X-hockeyApptoken: b9d6e2f453894b4fbcb161b33a94f6c8" https://rink.hockeyapp.net/api/2/apps/ecacf68949f344a686bed78d47449973/app_versions/upload
      '''
    }
  }
}