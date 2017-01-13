node('Android') {
    stage('Checkout') {
        sh 'rm -rf *'
        checkout([$class: 'GitSCM', branches: [[name: env.BRANCH_NAME]], doGenerateSubmoduleConfigurations: false, extensions: [[$class: 'RelativeTargetDirectory', relativeTargetDir: 'android-commlib-all'], [$class: 'SubmoduleOption', disableSubmodules: false, parentCredentials: true, recursiveSubmodules: true, reference: '', trackingSubmodules: false]], submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'd866c69b-16f0-4fce-823a-2a42bbf90a3d', url: 'ssh://git@bitbucket.atlas.philips.com:7999/com/android-commlib-all.git']]])
    }

    def Slack = load "android-commlib-all/jenkinsfile-common/Slack.groovy"

    Slack.notify('#conartists') {
        if (env.BRANCH_NAME == "develop" || env.BRANCH_NAME == "master") {
            stage('Build against binaries') {
                sh 'cd android-commlib-all/Source/commlib-all-parent && ./gradlew assemble'
            }
        } else {
            stage('Build local BlueLib') {
                checkout([$class: 'GitSCM', branches: [[name: env.BRANCH_NAME]], doGenerateSubmoduleConfigurations: false, extensions: [[$class: 'RelativeTargetDirectory', relativeTargetDir: 'android-shinelib']], submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'd866c69b-16f0-4fce-823a-2a42bbf90a3d', url: 'ssh://git@bitbucket.atlas.philips.com:7999/ehshn/android-shinelib.git']]])
                sh 'cd android-shinelib/Source/ShineLib && ./gradlew assembleDebug'
            }

            stage('Build local CommLib') {
                checkout([$class: 'GitSCM', branches: [[name: env.BRANCH_NAME]], doGenerateSubmoduleConfigurations: false, extensions: [[$class: 'RelativeTargetDirectory', relativeTargetDir: 'dicomm-android']], submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'd866c69b-16f0-4fce-823a-2a42bbf90a3d', url: 'ssh://git@bitbucket.atlas.philips.com:7999/com/dicomm-android.git']]])
                sh 'cd dicomm-android/Source/DICommClientSample && ./gradlew assembleDebug'
            }

            stage('Build against local libs') {
                sh 'cd android-commlib-all/Source/commlib-all-parent && ./gradlew assemble'
            }
        }

        stage('Tests') {
            sh 'rm -rf android-commlib-all/Source/commlib-all-parent/commlib-all/build/test-results'
            sh 'cd android-commlib-all/Source/commlib-all-parent && ./gradlew testDebug'

            // step([$class: 'JUnitResultArchiver', testResults: '**/build/test-results/*/*.json'])
            // step([$class: 'CucumberReportPublisher', jsonReportDirectory: 'build/cucumber-reports', fileIncludePattern: '*.json'])
        }

        stage('Archive App') {
            step([$class: 'ArtifactArchiver', artifacts: 'android-commlib-all/Source/commlib-all-parent/commlib-all-example/build/outputs/apk/*.apk', excludes: null, fingerprint: true, onlyIfSuccessful: true])
        }

        if (env.BRANCH_NAME == "develop" || env.BRANCH_NAME == "master") {
            stage('Publish') {
                sh 'cd android-commlib-all/Source/commlib-all-parent && ./gradlew artifactoryPublish'
            }
        }
    }
}
