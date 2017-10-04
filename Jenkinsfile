
node('Android') {
def errors = []
    timestamps {
        try {
            stage('Checkout') {
                echo "stage Checkout"
                sh 'rm -rf *'
                checkout([$class: 'GitSCM', branches: [[name: env.BRANCH_NAME]], doGenerateSubmoduleConfigurations: false, extensions: [[$class: 'RelativeTargetDirectory', relativeTargetDir: 'android-commlib-all'], [$class: 'SubmoduleOption', disableSubmodules: false, parentCredentials: true, recursiveSubmodules: true, reference: '', trackingSubmodules: false], [$class: 'WipeWorkspace'], [$class: 'PruneStaleBranch'], [$class: 'LocalBranch']], submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'd866c69b-16f0-4fce-823a-2a42bbf90a3d', url: 'ssh://tfsemea1.ta.philips.com:22/tfs/TPC_Region24/CDP2/_git/ews-android-easywifisetupuapp']]])
            }

            stage('Build') {
                echo "stage Build"
                def libgradle = "cd Source/Library/ && ./gradlew -u :ews:assembleDebug"
                def demoapp = "cd Source/Library/ && ./gradlew -u :demoapplication:assembleDebug"

            }
                
            
            stage('Archive results') {
                echo "stage Archive results"
                archiveArtifacts artifacts: Source/Library/demoapplication/build/outputs/apk/*.apk', fingerprint: true, onlyIfSuccessful: true
                archiveArtifacts artifacts: 'Source/Library/ews/build/outputs/aar/*.aar', fingerprint: true, onlyIfSuccessful: true
            }

        } catch(err) {
            errors << "errors found: ${err}"
        } finally {
            stage('Clean up workspace') {
                step([$class: 'WsCleanup', deleteDirs: true, notFailBuild: true])
            }
        }
    } 
}

node('master') {
    stage('Cleaning workspace') {
        def wrk = pwd() + "@script/"
        dir("${wrk}") {
            deleteDir()
        }
    }
}