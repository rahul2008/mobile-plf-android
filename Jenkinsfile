/* following line is mandatory for the platform CI pipeline integration */
properties([[$class: 'ParametersDefinitionProperty', parameterDefinitions: [[$class: 'StringParameterDefinition', defaultValue: '', description: 'triggerBy', name: 'triggerBy']]]])

node('Android') {
    stage 'Checkout'
    checkout scm

    stage 'Build'
    sh 'cd ./Source/DICommClientSample && ./gradlew assembleDebug'

    stage 'Unit test'
    sh 'rm -rf ./Source/DICommClient/dicommClientLib/build/test-results/debug'
    sh 'cd ./Source/DICommClientSample && ./gradlew testDebugUnitTest || true'
    step([$class: 'JUnitResultArchiver', testResults: 'Source/DICommClient/dicommClientLib/build/test-results/debug/*.xml'])

    stage 'Archive App'
    step([$class: 'ArtifactArchiver', artifacts: 'Source/DICommClientSample/sampleApp/build/outputs/apk/*.apk', excludes: null, fingerprint: true, onlyIfSuccessful: true])

    if(env.BRANCH_NAME == "develop" || env.BRANCH_NAME == "master"){
        stage 'Publish'
        sh 'cd ./Source/DICommClient && ./gradlew zipDocuments artifactoryPublish'
    }
}

/* next if-then + stage is mandatory for the platform CI pipeline integration */
if (env.triggerBy != "ppc" && (env.BRANCH_NAME == "develop" || env.BRANCH_NAME == "master")) {
    stage('Trigger Integration Pipeline') {
        build job: "Platform-Infrastructure/ppc/ppc_android/${env.BRANCH_NAME}", parameters: [[$class: 'StringParameterValue', name: 'componentName', value: 'COM'], [$class: 'StringParameterValue', name: 'libraryName', value: 'CommLib']]
    }
}