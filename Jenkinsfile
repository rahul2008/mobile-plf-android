node('Android') {
    stage('Checkout') {
        checkout([$class: 'GitSCM', branches: [[name: env.BRANCH_NAME]], doGenerateSubmoduleConfigurations: false, extensions: [[$class: 'RelativeTargetDirectory', relativeTargetDir: 'android-commlib-all']], submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'd866c69b-16f0-4fce-823a-2a42bbf90a3d', url: 'ssh://git@atlas.natlab.research.philips.com:7999/com/android-commlib-all.git']]])
        checkout([$class: 'GitSCM', branches: [[name: 'feature/COM-141-commlib-ble']], doGenerateSubmoduleConfigurations: false, extensions: [[$class: 'RelativeTargetDirectory', relativeTargetDir: 'dicomm-android']], submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'd866c69b-16f0-4fce-823a-2a42bbf90a3d', url: 'ssh://git@atlas.natlab.research.philips.com:7999/com/dicomm-android.git']]])
        checkout([$class: 'GitSCM', branches: [[name: 'feature/COM-141-commlib-ble']], doGenerateSubmoduleConfigurations: false, extensions: [[$class: 'RelativeTargetDirectory', relativeTargetDir: 'android-shinelib']], submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'd866c69b-16f0-4fce-823a-2a42bbf90a3d', url: 'ssh://git@atlas.natlab.research.philips.com:7999/ehshn/android-shinelib.git']]])
        sh 'ls -al'
    }

    stage('Build BlueLib') {
        sh 'cd android-shinelib/Source/ShineLib && ./gradlew assembleDebug'
    }

    stage('Build CommLib') {
        sh 'cd dicomm-android/Source/DICommClientSample && ./gradlew assembleDebug'
    }

    stage('Build CommLib BlueLib Glue') {
        sh 'cd android-commlib-all/Source/commlib-all-parent && ./gradlew assembleCompat'
    }

    stage('Tests') {
        sh 'rm -rf android-commlib-all/Source/commlib-all-parent/commlib-all/build/test-results'
        sh 'cd android-commlib-all/Source/commlib-all-parent && ./gradlew testCompat || true'
        sh 'cd /home/jenkins/.gradle/caches/modules-2/files-2.1/info.cukes/cucumber-java/1.2.2/ && find .'
        sh 'ls -al /home/jenkins/.gradle/caches/modules-2/files-2.1/info.cukes/cucumber-java/1.2.2/8413605441d44bedffab012d1539f1015f856bb/'
        step([$class: 'JUnitResultArchiver', testResults: '**/build/test-results/*/*.xml'])
    }
}
