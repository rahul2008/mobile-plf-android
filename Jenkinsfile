/* following line is mandatory for the platform CI pipeline integration */
properties([[$class: 'ParametersDefinitionProperty', parameterDefinitions: [[$class: 'StringParameterDefinition', defaultValue: '', description: 'triggerBy', name: 'triggerBy']]]])

node('Android') {
    stage('Checkout') {
        checkout scm
    }

    Pipeline = load "Source/common/jenkins/Pipeline.groovy"
    Slack = load "Source/common/jenkins/Slack.groovy"

    Slack.notify('#conartists') {
        stage('Build') {
            sh 'cd ./Source/ShineLib && ./gradlew --refresh-dependencies -PenvCode=${JENKINS_ENV} assembleRelease assembleDebug'
        }

        stage('Unit test') {
            sh 'rm -rf ./Source/ShineLib/shinelib/build/test-results/debug ./Source/ShineLib/pluginreferenceboard/build/test-results/debug'
            sh 'cd ./Source/ShineLib && ./gradlew -PenvCode=${JENKINS_ENV} test || true'
            step([$class: 'JUnitResultArchiver', testResults: 'Source/ShineLib/*/build/test-results/*/*.xml'])
        }

        stage('Lint') {
            sh 'cd ./Source/ShineLib && ./gradlew -PenvCode=${JENKINS_ENV} lintDebug || true'
            step([$class: 'LintPublisher', healthy: '0', unHealthy: '20', unstableTotalAll: '20'])
        }

        stage('Archive artifacts') {
            step([$class: 'ArtifactArchiver', artifacts: 'Source/ShineLib/bluelibtestapp/build/outputs/apk/*.apk', excludes: null, fingerprint: true, onlyIfSuccessful: true])
            step([$class: 'ArtifactArchiver', artifacts: 'Source/ShineLib/bluelibexampleapp/build/outputs/apk/*.apk', excludes: null, fingerprint: true, onlyIfSuccessful: true])
        }

        stage('Reporting') {
            step([$class: 'JacocoPublisher', execPattern: '**/*.exec', classPattern: '**/classes', sourcePattern: '**/src/main/java', exclusionPattern: '**/R.class,**/R$*.class,**/BuildConfig.class,**/Manifest*.*,**/*Activity*.*,**/*Fragment*.*'])
        }

        if (env.BRANCH_NAME == "develop" || env.BRANCH_NAME =~ "release" || env.BRANCH_NAME == "master") {
            stage('Publish') {
                sh 'cd ./Source/ShineLib && ./gradlew -PenvCode=${JENKINS_ENV} zipDocuments artifactoryPublish'
            }
        }

        stage('Dependencies list') {
            sh 'cd ./Source/ShineLib && ./gradlew -PenvCode=${JENKINS_ENV} saveResDep'
            archiveArtifacts '**/dependencies.lock'
        }
    }
    Pipeline.trigger(env.triggerBy, env.BRANCH_NAME, "BlueLib", "bll")
}
