/* following line is mandatory for the platform CI pipeline integration */
properties([[$class: 'ParametersDefinitionProperty', parameterDefinitions: [[$class: 'StringParameterDefinition', defaultValue: '', description: 'triggerBy', name: 'triggerBy']]]])

node('Android') {
    stage('Checkout') {
        checkout scm
    }

    Slack = load "Source/common/jenkins/Slack.groovy"
    Pipeline = load "Source/common/jenkins/Pipeline.groovy"
    def gradle = 'cd ./Source/cloudcontroller && ./gradlew -PenvCode=${JENKINS_ENV}'

    Slack.notify('#conartists') {
        stage('Build') {
            sh 'find . -path "**build/test-results" -exec rm -r "{}" \\;'
            sh "$gradle --refresh-dependencies assembleRelease"
        }

        stage('Test') {
            sh "$gradle check || true"
            step([$class: 'JUnitResultArchiver', testResults: '**/testDebugUnitTest/*.xml'])
            step([$class: 'LintPublisher', healthy: '0', unHealthy: '20', unstableTotalAll: '20'])
        }

        stage('Archive artifacts') {
            step([$class: 'ArtifactArchiver', artifacts: 'Source/cloudcontroller/build/outputs/aar/*.aar', excludes: null, fingerprint: true, onlyIfSuccessful: true])
        }

        if (env.BRANCH_NAME == "develop" || env.BRANCH_NAME == "master" || env.BRANCH_NAME =~ "release") {
            stage('Publish') {
                sh "$gradle zipDocuments artifactoryPublish"
            }
        }

        stage('Save Dependencies') {
            sh "$gradle saveResDep"
            archiveArtifacts '**/dependencies.lock'
        }
    }
    Pipeline.trigger(env.triggerBy, env.BRANCH_NAME, "CloudController", "ccc")
}
