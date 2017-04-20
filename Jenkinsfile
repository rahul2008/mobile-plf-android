/* following line is mandatory for the platform CI pipeline integration */
properties([[$class: 'ParametersDefinitionProperty', parameterDefinitions: [[$class: 'StringParameterDefinition', defaultValue: '', description: 'triggerBy', name: 'triggerBy']]]])

node('Android') {
    stage('Checkout') {
        checkout scm
    }

    Pipeline = load "Source/common/jenkins/Pipeline.groovy"
    Slack = load "Source/common/jenkins/Slack.groovy"
    def gradle = 'cd ./Source/DICommClient && ./gradlew -PenvCode=${JENKINS_ENV}'

    Slack.notify('#conartists') {
        stage('Build') {
            sh "$gradle --refresh-dependencies assembleRelease assembleDebug saveResDep"
        }

        stage('Unit Test') {
            sh "rm -rf ./Source/DICommClient/dicommClientLib/build/test-results/debug"
            sh "$gradle testDebugUnitTest || true"
        }

        stage("Gather reports") {
            step([$class: 'JUnitResultArchiver', testResults: 'Source/DICommClient/dicommClientLib/build/test-results/debug/*.xml'])
        }

        stage('Archive artifacts') {
            archiveArtifacts artifacts: 'Source/DICommClient/dicommClientLib/build/outputs/aar/*.aar', fingerprint: true, onlyIfSuccessful: true
            archiveArtifacts artifacts: '**/build/outputs/apk/*.apk', fingerprint: true, onlyIfSuccessful: true
            archiveArtifacts '**/dependencies.lock'
        }

        if (env.BRANCH_NAME == "develop" || env.BRANCH_NAME =~ "release" || env.BRANCH_NAME == "master") {
            stage('Publish') {
                sh "$gradle zipDocuments artifactoryPublish"
            }
        }
    }
    Pipeline.trigger(env.triggerBy, env.BRANCH_NAME, "CommLib", "cml")
}
