node('Android') {
    stage('Checkout') {
        checkout([$class: 'GitSCM', branches: [[name: env.BRANCH_NAME]], doGenerateSubmoduleConfigurations: false, extensions: [[$class: 'RelativeTargetDirectory', relativeTargetDir: 'android-commlib-all']], submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'd866c69b-16f0-4fce-823a-2a42bbf90a3d', url: 'ssh://git@atlas.natlab.research.philips.com:7999/com/android-commlib-all.git']]])
        checkout([$class: 'GitSCM', branches: [[name: 'feature/COM-141-commlib-ble']], doGenerateSubmoduleConfigurations: false, extensions: [[$class: 'RelativeTargetDirectory', relativeTargetDir: 'dicomm-android']], submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'd866c69b-16f0-4fce-823a-2a42bbf90a3d', url: 'ssh://git@atlas.natlab.research.philips.com:7999/com/dicomm-android.git']]])
        checkout([$class: 'GitSCM', branches: [[name: 'feature/COM-141-commlib-ble']], doGenerateSubmoduleConfigurations: false, extensions: [[$class: 'RelativeTargetDirectory', relativeTargetDir: 'android-shinelib']], submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'd866c69b-16f0-4fce-823a-2a42bbf90a3d', url: 'ssh://git@atlas.natlab.research.philips.com:7999/ehshn/android-shinelib.git']]])
        sh 'ls -al'
    }

    stage('Build BlueLib') {
        sh 'cd android-shinelib/Source/ShineLib'
        sh './gradlew assembleDebug'
        sh 'cd -'
    }

    stage('Build CommLib') {
        sh 'cd dicomm-android/Source/DICommClientSample'
        sh './gradlew assembleDebug'
        sh 'cd -'
    }

    stage('Build CommLib BlueLib Glue') {
        sh 'cd android-commlib-all/Source/commlib-all-parent'
        sh './gradlew assembleCompat'
        sh 'cd -'
    }

    stage('Tests') {
        sh 'cd android-commlib-all/Source/commlib-all-parent'
        sh 'rm -rf ./commlib-all/build/test-results'
        sh './gradlew testCompat || true'
        sh 'cd -'
        step([$class: 'JUnitResultArchiver', testResults: 'android-commlib-all/Source/commlib-all-parent/*/build/test-results/*/*.xml'])
    }

}
