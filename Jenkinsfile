def getConfig() {
  return ( env.BRANCH_NAME == 'master' ) ? 'Release' : 'Debug'
}

node ('Android && 25.0.0 && Ubuntu'){
  def CONFIG = getConfig()
  sh "echo \"The branch is ${env.BRANCH_NAME} -> Using Config ${CONFIG}\""

  stage 'Checkout'
  checkout scm

  stage 'Build Catalog app'
  sh '''
    cd Source/CatalogApp
    ./gradlew assembleDebug

  '''

stage 'Archive Apps'
step([$class: 'ArtifactArchiver', artifacts: 'Source/CatalogApp/app/build/outputs/apk/*debug.apk', excludes: null, fingerprint: true, onlyIfSuccessful: true])

}