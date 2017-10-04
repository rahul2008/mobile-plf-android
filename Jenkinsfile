properties([[$class: 'ParametersDefinitionProperty', parameterDefinitions: [[$class: 'StringParameterDefinition', defaultValue: '', description: 'triggerBy', name: 'triggerBy']]]])

def errors = []

node('Android') {
    timestamps {
        try {

            stage('Checkout') {
              sh 'rm -rf *'
              checkout([$class: 'GitSCM', branches: [[name: env.BRANCH_NAME]], doGenerateSubmoduleConfigurations: false, extensions: [[$class: 'RelativeTargetDirectory', relativeTargetDir: 'android-commlib-all'], [$class: 'SubmoduleOption', disableSubmodules: false, parentCredentials: true, recursiveSubmodules: true, reference: '', trackingSubmodules: false], [$class: 'WipeWorkspace'], [$class: 'PruneStaleBranch'], [$class: 'LocalBranch']], submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'd866c69b-16f0-4fce-823a-2a42bbf90a3d', url: 'ssh://tfsemea1.ta.philips.com:22/tfs/TPC_Region24/CDP2/_git/ews-android-easywifisetupuapp']]])

            }

//            Slack = load 'Source/common/jenkins/Slack.groovy'
//            Pipeline = load 'Source/common/jenkins/Pipeline.groovy'
              def gradle = 'cd Source/Library/demooapplication && ./gradlew assembleDebug'

            //def cucumber_path = 'Source/Library/build/cucumber-reports'
            //def cucumber_filename = 'cucumber-report-android-commlib.json'

//            Slack.notify('#conartists') {
                stage('Build') {
                    sh "$gradle generateJavadocPublicApi saveResDep assembleDebug"
                }

//                stage("Gather reports") {
//                    step([$class: 'JUnitResultArchiver', testResults: '**/testDebugUnitTest/*.xml'])
//                    step([$class: 'LintPublisher', healthy: '0', unHealthy: '50', unstableTotalAll: '50'])
//                    step([$class: 'JacocoPublisher', execPattern: '**/*.exec', classPattern: '**/classes', sourcePattern: '**/src/main/java', exclusionPattern: '**/R.class,**/R$*.class,**/BuildConfig.class,**/Manifest*.*,**/*Activity*.*,**/*Fragment*.*'])
//                    for (lib in ["commlib-api", "commlib-ble", "commlib-lan", "commlib-cloud"]) {
//                        publishHTML([allowMissing: false, alwaysLinkToLastBuild: true, keepAll: true, reportDir: "Documents/External/$lib-api", reportFiles: 'index.html', reportName: "$lib API documentation"])
//                    }
//
//                    if (fileExists("$cucumber_path/report.json")) {
//                        step([$class: 'CucumberReportPublisher', jsonReportDirectory: cucumber_path, fileIncludePattern: '*.json'])
//                    } else {
//                        echo 'No Cucumber result found, nothing to publish'
//                    }
//                }
//
//                boolean publishing = (env.BRANCH_NAME.startsWith("develop") || env.BRANCH_NAME.startsWith("release/platform_") || env.BRANCH_NAME.startsWith("master"))
//                if (publishing) {
//                    for (lib in ["commlib-testutils", "cloudcontroller-api", "cloudcontroller", "commlib-api", "commlib-ble", "commlib-lan", "commlib-cloud", "commlib"]) {
//                        def libgradle = "cd Source/Library/ews/$lib && ./gradlew -u -PenvCode=\${JENKINS_ENV}"
//                        stage("Publish $lib") {
//                            sh "$libgradle assembleRelease saveResDep zipDocuments artifactoryPublish"
//                        }
//                    }
//
//                    stage("Publish demouapp") {
//                        sh "cd Source && ./gradlew -u :commlib-demouapp:generatePomFileForAarPublication :commlib-demouapp:artifactoryPublish"
//                    }
//                }
                
                def libgradle = "cd Source/Library/ews/ && ./gradlew -u :ews:assembleDebug"
                def demoapp = "cd Source/Library/demoapplication/ && ./gradlew -u :demoapplication:assembleDebug"

                stage('Archive results') {
                    archiveArtifacts artifacts: Source/Library/demoapplication/build/outputs/apk/*.apk', fingerprint: true, onlyIfSuccessful: true
                    archiveArtifacts artifacts: 'Source/Library/ews/build/outputs/aar/*.aar', fingerprint: true, onlyIfSuccessful: true
//                    archiveArtifacts '**/dependencies.lock'

//                    sh "mv $cucumber_path/report.json $cucumber_path/$cucumber_filename"
//                    archiveArtifacts artifacts: "$cucumber_path/$cucumber_filename", fingerprint: true, onlyIfSuccessful: true
                }
            //}

//            Pipeline.trigger(env.triggerBy, env.BRANCH_NAME, "CommLib", "cml")

        } catch(err) {
            errors << "errors found: ${err}"
        } finally {
            stage('Clean up workspace') {
                step([$class: 'WsCleanup', deleteDirs: true, notFailBuild: true])
            }
        }
    } // end timestamps
}

node('master') {
    stage('Cleaning workspace') {
        def wrk = pwd() + "@script/"
        dir("${wrk}") {
            deleteDir()
        }
    }
}
