/* following two lines are mandatory for the platform CI pipeline integration */
JENKINS_ENV = env.JENKINS_ENV
properties([[$class: 'ParametersDefinitionProperty', parameterDefinitions: [[$class: 'StringParameterDefinition', defaultValue: '', description: 'triggerBy', name: 'triggerBy']]]])

node('Android') {
    stage('Checkout') {
        checkout scm
    }

    Pipeline = load "Source/common/jenkins/Pipeline.groovy"
    Slack = load "Source/common/jenkins/Slack.groovy"
    def gradle = "cd ./Source/DICommClient && ./gradlew -PenvCode=${JENKINS_ENV}"

    Slack.notify('#conartists') {

        stage('Build') {
            sh "${gradle} --refresh-dependencies assembleRelease"
        }

        stage('Unit Test') {
            sh "rm -rf ./Source/DICommClient/dicommClientLib/build/test-results/debug"
            sh "${gradle} testDebugUnitTest || true"
            step([$class: 'JUnitResultArchiver', testResults: 'Source/DICommClient/dicommClientLib/build/test-results/debug/*.xml'])
        }

//  Disabled after checking with Joost. We do not build the app anymore.
//        stage('Archive App') {
//            step([$class: 'ArtifactArchiver', artifacts: 'Source/DICommClientSample/sampleApp/build/outputs/apk/*.apk', excludes: null, fingerprint: true, onlyIfSuccessful: true])
//        }

        if (env.BRANCH_NAME == "develop" || env.BRANCH_NAME =~ "release" || env.BRANCH_NAME == "master") {
            stage('Publish') {
                sh "${gradle} zipDocuments artifactoryPublish"
            }
        }

        stage('Save Dependencies') {
            sh "${gradle} saveResDep"
            archiveArtifacts '**/dependencies.lock'
        }

        Pipeline.trigger(env.triggerBy, env.BRANCH_NAME, "CommLib", "cml")
    }
}