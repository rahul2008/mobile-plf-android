/* following line is mandatory for the platform CI pipeline integration */

JENKINS_ENV = env.JENKINS_ENV

properties([[$class: 'ParametersDefinitionProperty', parameterDefinitions: [[$class: 'StringParameterDefinition', defaultValue: '', description: 'triggerBy', name: 'triggerBy']]]])

node('Android') {
    stage 'Checkout'
    checkout scm

    stage 'Build'
    sh 'cd ./Source/ShineLib && ./gradlew --refresh-dependencies -PenvCode=${JENKINS_ENV} assembleRelease'

    stage 'Unit test'
    sh 'rm -rf ./Source/ShineLib/shinelib/build/test-results/debug ./Source/ShineLib/pluginreferenceboard/build/test-results/debug'
    sh 'cd ./Source/ShineLib && ./gradlew -PenvCode=${JENKINS_ENV} test || true'
    step([$class: 'JUnitResultArchiver', testResults: 'Source/ShineLib/*/build/test-results/*/*.xml'])

    stage 'Lint'
    sh 'cd ./Source/ShineLib && ./gradlew -PenvCode=${JENKINS_ENV} lintDebug || true'
    step([$class: 'LintPublisher', healthy: '0', unHealthy: '20', unstableTotalAll: '20'])

// Disabling this after discussion with Joost, as app is not built anymore.
//    stage 'Archive Apps'
//    step([$class: 'ArtifactArchiver', artifacts: 'Source/ShineLib/bluelibtestapp/build/outputs/apk/*.apk', excludes: null, fingerprint: true, onlyIfSuccessful: true])
//    step([$class: 'ArtifactArchiver', artifacts: 'Source/ShineLib/bluelibexampleapp/build/outputs/apk/*.apk', excludes: null, fingerprint: true, onlyIfSuccessful: true])

    stage 'Reporting'
    step([$class: 'JacocoPublisher', execPattern: '**/*.exec', classPattern: '**/classes', sourcePattern: '**/src/main/java', exclusionPattern: '**/R.class,**/R$*.class,**/BuildConfig.class,**/Manifest*.*,**/*Activity*.*,**/*Fragment*.*'])

    if (env.BRANCH_NAME == "develop" || env.BRANCH_NAME =~ "release" || env.BRANCH_NAME == "master") {
        stage 'Publish'
        sh 'cd ./Source/ShineLib && ./gradlew -PenvCode=${JENKINS_ENV} zipDocuments artifactoryPublish'
    }

    stage ('save dependencies list') {
    	sh 'cd ./Source/ShineLib && ./gradlew -PenvCode=${JENKINS_ENV} saveResDep'
    }
    archiveArtifacts '**/dependencies.lock'
}

/* next if-then + stage is mandatory for the platform CI pipeline integration */
if (env.triggerBy != "ppc" && (env.BRANCH_NAME == "develop" || env.BRANCH_NAME =~ "release" || env.BRANCH_NAME == "master")) {
    def platform = "android"
    def project = "BlueLib"
    def project_tla = "bll"
    def BranchName = env.BRANCH_NAME
    if (BranchName =~ "/") {
        BranchName = BranchName.replaceAll('/','%2F')
        echo "BranchName changed to ${BranchName}"
    }
    stage('Trigger Integration Pipeline') {
        build job: "Platform-Infrastructure/ppc/ppc_${platform}/${BranchName}", propagate: false, parameters: [[$class: 'StringParameterValue', name: 'componentName', value: project_tla], [$class: 'StringParameterValue', name: 'libraryName', value: project]]
    }
}
