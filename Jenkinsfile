node('Android') {
    stage('Checkout') {
        checkout([$class: 'GitSCM', branches: [[name: env.BRANCH_NAME]], doGenerateSubmoduleConfigurations: false, extensions: [[$class: 'RelativeTargetDirectory', relativeTargetDir: 'android-commlib-all']], submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'd866c69b-16f0-4fce-823a-2a42bbf90a3d', url: 'ssh://git@atlas.natlab.research.philips.com:7999/com/android-commlib-all.git']]])
        checkout([$class: 'GitSCM', branches: [[name: 'feature/COM-141-commlib-ble']], doGenerateSubmoduleConfigurations: false, extensions: [[$class: 'RelativeTargetDirectory', relativeTargetDir: 'dicomm-android']], submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'd866c69b-16f0-4fce-823a-2a42bbf90a3d', url: 'ssh://git@atlas.natlab.research.philips.com:7999/com/dicomm-android.git']]])
        checkout([$class: 'GitSCM', branches: [[name: 'feature/COM-141-commlib-ble']], doGenerateSubmoduleConfigurations: false, extensions: [[$class: 'RelativeTargetDirectory', relativeTargetDir: 'android-shinelib']], submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'd866c69b-16f0-4fce-823a-2a42bbf90a3d', url: 'ssh://git@atlas.natlab.research.philips.com:7999/ehshn/android-shinelib.git']]])
    }

    stage('Build BlueLib') {
        sh 'cd android-shinelib/Source/ShineLib && ./gradlew assembleDebug'
    }

    stage('Build CommLib') {
        sh 'cd dicomm-android/Source/DICommClientSample && ./gradlew assembleDebug'
    }

    stage('Build CommLib BlueLib Glue') {
        sh 'cd android-commlib-all/Source/commlib-all-parent && ./gradlew assembleLocalDev'
    }

    stage('Tests') {
        sh 'rm -rf android-commlib-all/Source/commlib-all-parent/commlib-all/build/test-results'
        sh 'cd android-commlib-all/Source/commlib-all-parent && ./gradlew testLocalDev'

        // TODO: Re-activate JUnit result
        //step([$class: 'JUnitResultArchiver', testResults: '**/build/test-results/*/*.xml'])
    }
}
