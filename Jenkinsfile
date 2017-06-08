/* following line is mandatory for the platform CI pipeline integration */
properties([[$class: 'ParametersDefinitionProperty', parameterDefinitions: [[$class: 'StringParameterDefinition', defaultValue: '', description: 'triggerBy', name: 'triggerBy']]]])

node('Android') {
    stage('Checkout') {
        checkout scm
    }

    Slack = load "Source/common/jenkins/Slack.groovy"
    Pipeline = load "Source/common/jenkins/Pipeline.groovy"

    Slack.notify('#conartists') {
        stage('Build') {
            sh 'cd ./Source/cloudcontroller && ./gradlew --refresh-dependencies -PenvCode=${JENKINS_ENV} assembleRelease'
        }

        stage('Unit test') {
            sh 'rm -rf ./Source/cloudcontroller/build/test-results/debug'
            sh 'cd ./Source/cloudcontroller && ./gradlew -PenvCode=${JENKINS_ENV} test || true'
            step([$class: 'JUnitResultArchiver', testResults: '**/testDebugUnitTest/*/*.xml'])
        }

        stage('Lint') {
            sh 'cd ./Source/cloudcontroller && ./gradlew -PenvCode=${JENKINS_ENV} lintDebug || true'
            step([$class: 'LintPublisher', healthy: '0', unHealthy: '20', unstableTotalAll: '20'])
        }

        stage('Archive artifacts') {
            step([$class: 'ArtifactArchiver', artifacts: 'Source/cloudcontroller/build/outputs/aar/*.aar', excludes: null, fingerprint: true, onlyIfSuccessful: true])
        }

        if (env.BRANCH_NAME == "develop" || env.BRANCH_NAME == "master" || env.BRANCH_NAME =~ "release") {
            stage('Publish') {
                sh 'cd ./Source/cloudcontroller && ./gradlew -PenvCode=${JENKINS_ENV} zipDocuments artifactoryPublish'
            }
        }

        stage('Save Dependencies') {
            sh 'cd ./Source/cloudcontroller && ./gradlew -PenvCode=${JENKINS_ENV} saveResDep'
            archiveArtifacts '**/dependencies.lock'
        }
    }
    Pipeline.trigger(env.triggerBy, env.BRANCH_NAME, "CloudController", "ccc")
}
