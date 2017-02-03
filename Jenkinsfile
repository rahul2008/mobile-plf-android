/* following line is mandatory for the platform CI pipeline integration */
properties([[$class: 'ParametersDefinitionProperty', parameterDefinitions: [[$class: 'StringParameterDefinition', defaultValue: '', description: 'triggerBy', name: 'triggerBy']]]])

node('Android') {
    stage('Checkout') {
        checkout scm
    }

    def Slack = load "Source/common/jenkins/Slack.groovy"

    Slack.notify('#conartists') {

        stage('Build') {
            sh 'cd ./Source/DICommClientSample && ./gradlew assembleDebug'
        }

        stage('Unit test') {
            sh 'rm -rf ./Source/DICommClient/dicommClientLib/build/test-results/debug'
            sh 'cd ./Source/DICommClientSample && ./gradlew testDebugUnitTest || true'
            step([$class: 'JUnitResultArchiver', testResults: 'Source/DICommClient/dicommClientLib/build/test-results/debug/*.xml'])
        }

        stage('Archive App') {
            step([$class: 'ArtifactArchiver', artifacts: 'Source/DICommClientSample/sampleApp/build/outputs/apk/*.apk', excludes: null, fingerprint: true, onlyIfSuccessful: true])
        }

        if (env.BRANCH_NAME == "develop" || env.BRANCH_NAME =~ "release" || env.BRANCH_NAME == "master") {
            stage('Publish') {
                sh 'cd ./Source/DICommClient && ./gradlew zipDocuments artifactoryPublish'
            }
        }
    }

    /* next if-then + stage is mandatory for the platform CI pipeline integration */
    if (env.triggerBy != "ppc" && (env.BRANCH_NAME == "develop" || env.BRANCH_NAME =~ "release" || env.BRANCH_NAME == "master")) {
        def platform = "android"
        def project = "CommLib"
        def project_tla = "cml"
        def BranchName = env.BRANCH_NAME
        if (BranchName =~ "/") {
            BranchName = BranchName.replaceAll('/','%2F')
            echo "BranchName changed to ${BranchName}"
        }
        stage('Trigger Integration Pipeline') {
            build job: "Platform-Infrastructure/ppc/ppc_${platform}/${BranchName}", propagate: false, parameters: [[$class: 'StringParameterValue', name: 'componentName', value: project_tla], [$class: 'StringParameterValue', name: 'libraryName', value: project]]
        }
    }

}