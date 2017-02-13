node('Android') {
    stage('Checkout') {
        sh 'rm -rf *'
        checkout([$class: 'GitSCM', branches: [[name: env.BRANCH_NAME]], doGenerateSubmoduleConfigurations: false, extensions: [[$class: 'RelativeTargetDirectory', relativeTargetDir: 'android-commlib-all'], [$class: 'SubmoduleOption', disableSubmodules: false, parentCredentials: true, recursiveSubmodules: true, reference: '', trackingSubmodules: false], [$class: 'WipeWorkspace'], [$class: 'PruneStaleBranch'], [$class: 'LocalBranch']], submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'd866c69b-16f0-4fce-823a-2a42bbf90a3d', url: 'ssh://git@bitbucket.atlas.philips.com:7999/com/android-commlib-all.git']]])
    }

    def Slack = load "android-commlib-all/Source/common/jenkins/Slack.groovy"

    Slack.notify('#conartists') {
        if (env.BRANCH_NAME == "develop" || env.BRANCH_NAME =~ "release" || env.BRANCH_NAME == "master") {
            stage('Build against binaries') {
                sh 'cd android-commlib-all/Source/commlib-all-parent && ./gradlew assemble'
            }
        } else {
            stage('Checkout local BlueLib') {
                try {
                    checkout([$class: 'GitSCM', branches: [[name: env.BRANCH_NAME]], doGenerateSubmoduleConfigurations: false, extensions: [[$class: 'RelativeTargetDirectory', relativeTargetDir: 'android-shinelib']], submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'd866c69b-16f0-4fce-823a-2a42bbf90a3d', url: 'ssh://git@bitbucket.atlas.philips.com:7999/ehshn/android-shinelib.git']]])
                } catch (Exception e) {
                    checkout([$class: 'GitSCM', branches: [[name: 'develop']], doGenerateSubmoduleConfigurations: false, extensions: [[$class: 'RelativeTargetDirectory', relativeTargetDir: 'android-shinelib']], submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'd866c69b-16f0-4fce-823a-2a42bbf90a3d', url: 'ssh://git@bitbucket.atlas.philips.com:7999/ehshn/android-shinelib.git']]])
                }
            }

            stage('Checkout local CommLib') {
                try {
                    checkout([$class: 'GitSCM', branches: [[name: env.BRANCH_NAME]], doGenerateSubmoduleConfigurations: false, extensions: [[$class: 'RelativeTargetDirectory', relativeTargetDir: 'dicomm-android']], submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'd866c69b-16f0-4fce-823a-2a42bbf90a3d', url: 'ssh://git@bitbucket.atlas.philips.com:7999/com/dicomm-android.git']]])
                } catch (Exception e) {
                    checkout([$class: 'GitSCM', branches: [[name: 'develop']], doGenerateSubmoduleConfigurations: false, extensions: [[$class: 'RelativeTargetDirectory', relativeTargetDir: 'dicomm-android']], submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'd866c69b-16f0-4fce-823a-2a42bbf90a3d', url: 'ssh://git@bitbucket.atlas.philips.com:7999/com/dicomm-android.git']]])
                }
            }

            stage('Build against local libs') {
                sh 'cd android-commlib-all/Source/commlib-all-parent && ./gradlew assemble'
            }
        }

        stage('Tests') {
            sh 'rm -rf android-commlib-all/Source/commlib-all-parent/commlib-all/build/test-results'
            sh 'cd android-commlib-all/Source/commlib-all-parent && ./gradlew testDebug'

            // step([$class: 'JUnitResultArchiver', testResults: '**/build/test-results/*/*.json'])
            step([$class: 'CucumberReportPublisher', jsonReportDirectory: 'android-commlib-all/Source/commlib-all-parent/build/cucumber-reports', fileIncludePattern: '*.json'])
        }

        stage('Archive App') {
            step([$class: 'ArtifactArchiver', artifacts: 'android-commlib-all/Source/commlib-all-parent/commlib-all-example/build/outputs/apk/*.apk', excludes: null, fingerprint: true, onlyIfSuccessful: true])
        }

        if (env.BRANCH_NAME == "develop" || env.BRANCH_NAME =~ "release" || env.BRANCH_NAME == "master") {
            stage('Publish') {
                sh 'rm -rf ./android_shinelib ./dicomm_android'
                sh 'cd android-commlib-all/Source/commlib-all-parent && ./gradlew zipDoc artifactoryPublish'
            }
        }

        /* next if-then + stage is mandatory for the platform CI pipeline integration */
        if (env.triggerBy != "ppc" && (env.BRANCH_NAME =~ /master|develop|release.*/)) {
            def platform = "android"
            def project = "CommLibBle"
            def project_tla = "cml"
            stage('Trigger Integration Pipeline') {
                build job: "Platform-Infrastructure/ppc/ppc_${platform}/${env.BRANCH_NAME}", propagate: false, parameters: [[$class: 'StringParameterValue', name: 'componentName', value: project_tla], [$class: 'StringParameterValue', name: 'libraryName', value: project]]
            }
        }

    }
}
