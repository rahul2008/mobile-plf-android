/* following two lines are mandatory for the platform CI pipeline integration */
JENKINS_ENV = env.JENKINS_ENV
properties([[$class: 'ParametersDefinitionProperty', parameterDefinitions: [[$class: 'StringParameterDefinition', defaultValue: '', description: 'triggerBy', name: 'triggerBy']]]])

node('Android') {
    stage('Checkout') {
        checkout scm
    }

    Pipeline = load "Source/common/jenkins/Pipeline.groovy"
    Slack = load "Source/common/jenkins/Slack.groovy"

    Slack.notify('#conartists') {

        stage('Build') {
            sh 'cd ./Source/DICommClient && ./gradlew --refresh-dependencies -PenvCode=${JENKINS_ENV} assembleRelease'
        }

        stage('Unit test') {
            sh 'rm -rf ./Source/DICommClient/dicommClientLib/build/test-results/debug'
            sh 'cd ./Source/DICommClient && ./gradlew -PenvCode=${JENKINS_ENV} testDebugUnitTest || true'
            step([$class: 'JUnitResultArchiver', testResults: 'Source/DICommClient/dicommClientLib/build/test-results/debug/*.xml'])
        }

//  Disabled after checking with Joost. We do not build the app anymore.
//        stage('Archive App') {
//            step([$class: 'ArtifactArchiver', artifacts: 'Source/DICommClientSample/sampleApp/build/outputs/apk/*.apk', excludes: null, fingerprint: true, onlyIfSuccessful: true])
//        }

        if (env.BRANCH_NAME == "develop" || env.BRANCH_NAME =~ "release" || env.BRANCH_NAME == "master") {
            stage('Publish') {
                sh 'cd ./Source/DICommClient && ./gradlew -PenvCode=${JENKINS_ENV} zipDocuments artifactoryPublish'
            }
        }

        stage('save dependencies list') {
            sh 'cd ./Source/DICommClient && ./gradlew -PenvCode=${JENKINS_ENV} saveResDep'
            archiveArtifacts '**/dependencies.lock'
        }

        Pipeline.trigger(env.triggerBy, env.BRANCH_NAME, "CommLib", "cml")
    }
}