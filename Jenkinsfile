#!/usr/bin/env groovy  

BranchName = env.BRANCH_NAME
JENKINS_ENV = env.JENKINS_ENV
JENKINS_ENV = env.JENKINS_ENV

properties([
    [$class: 'ParametersDefinitionProperty', parameterDefinitions: [[$class: 'StringParameterDefinition', defaultValue: '', description: 'triggerBy', name : 'triggerBy']]],
    [$class: 'BuildDiscarderProperty', strategy: [$class: 'LogRotator', numToKeepStr: '10']]
])

def errors = []

node('Android') {
    timestamps {
        try {
            stage('Checkout') {
             def jobBaseName = "${env.JOB_BASE_NAME}".replace('%2F', '/')
                if (env.BRANCH_NAME != jobBaseName)
                { 
                   echo "ERROR: Branches DON'T MATCH"
                   echo "Branchname  = " + env.BRANCH_NAME
                   echo "jobBaseName = " + jobBaseName
                   exit 1
                }

            checkout([$class: 'GitSCM', branches: [[name: env.BRANCH_NAME]], doGenerateSubmoduleConfigurations: false, extensions: [[$class: 'CloneOption', noTags: false, reference: '', shallow: true, timeout: 30],[$class: 'WipeWorkspace'], [$class: 'PruneStaleBranch'], [$class: 'LocalBranch', localBranch: "**"]], recursiveSubmodules: true, submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'd866c69b-16f0-4fce-823a-2a42bbf90a3d', url: 'ssh://tfsemea1.ta.philips.com:22/tfs/TPC_Region24/CDP2/_git/ews-android-easywifisetupuapp']]])
            }

            stage ('Unit Test') {
                    sh '''#!/bin/bash -l
                    cd ./Source/Library 
                    ./gradlew :ews:test
                            '''
                step([$class: 'JUnitResultArchiver', testResults: 'Source/Library/ews/build/test-results/*/*.xml'])
            }

            stage ('Jacoco') {
                    sh '''#!/bin/bash -l
                    cd ./Source/Library 
                    ./gradlew jacocoTestReport
                    '''

                step([$class: 'JacocoPublisher', execPattern: '**/*.exec', sourcePattern: '**/src/main/java', exclusionPattern: '**/R.class, **/R$*.class, */BuildConfig.class, **/Manifest*.*, **/*_Factory.class, **/*_*Factory.class , **/Dagger*.class, **/databinding/**/*.class, **/*Activity*.*, **/*Fragment*.*, **/*Service*.*, **/*ContentProvider*.*'])

                publishHTML(target: [keepAll: true, alwaysLinkToLastBuild: false, reportDir:  './Source/Library/ews/build/reports/jacoco/jacoco' + 'TestReleaseUnit'+'TestReport/html', reportFiles:'index.html', reportName: 'Overall code coverage'])

            }

            stage('Build Debug') {
                   sh """#!/bin/bash -l
                                chmod -R 755 . 
                                cd ./Source/DemoUApp 
                                ./gradlew --refresh-dependencies -PbuildNumber=${env.BUILD_NUMBER} clean assembleDebug lint
                            """
            }

            stage('Build Release') {
                    sh """#!/bin/bash -l
                                chmod -R 755 . 
                                cd ./Source/DemoUApp
                                ./gradlew  -PbuildNumber=${env.BUILD_NUMBER} assembleRelease 
                            """
            }

            stage ('Save dependencies list') {
            sh """#!/bin/bash -l
            	        chmod -R 775 . 
            	        cd ../Library 
                        ./gradlew -PbuildNumber=${env.BUILD_NUMBER} :ews:saveResDep :ews:saveAllResolvedDependencies :ews:saveAllResolvedDependenciesGradleFormat
                    """
            }
                
            stage('Archive results') {
                echo "stage Archive results"
                 androidLint canComputeNew: false, canRunOnFailed: true, defaultEncoding: '', healthy: '', pattern: '', shouldDetectModules: true, unHealthy: '', unstableTotalHigh: ''

                archiveArtifacts artifacts: 'Source/DemoUApp/app/build/outputs/apk/*.apk', fingerprint: true, onlyIfSuccessful: true
                archiveArtifacts artifacts: 'Source/Library/ews/build/outputs/aar/*.aar', fingerprint: true, onlyIfSuccessful: true
                archiveArtifacts '**/*dependencies*.lock'
            }

            boolean publishing = (env.BRANCH_NAME.startsWith("develop") || env.BRANCH_NAME.startsWith("release/platform_") || env.BRANCH_NAME.startsWith("master"))
                if (publishing) {
                    stage('Publish Library on Artifactory') {
                    echo "Publish Library on Artifactory"
                     sh """#!/bin/bash -l
                            chmod -R 755 . 
                            cd ./Source/Library 
                            ./gradlew artifactoryPublish
                        """
            }

                stage("Publish demouapp") {
                    echo "Publish demouapp on Artifactory"
                     sh """#!/bin/bash -l
                            chmod -R 755 . 
                            cd ./Source/DemoUApp 
                            ./gradlew artifactoryPublish
                        """
                }
            }
        if (env.triggerBy != "ppc" && (BranchName =~ /master|develop|release\/platform_.*/)) {
                stage ('callIntegrationPipeline') {
                    if (BranchName =~ "/") {
                        BranchName = BranchName.replaceAll('/','%2F')
                        echo "BranchName changed to ${BranchName}"
                    }
                    build job: "Platform-Infrastructure/ppc/ppc_android/${BranchName}", parameters: [[$class: 'StringParameterValue', name: 'componentName', value: 'ews'],[$class: 'StringParameterValue', name: 'libraryName', value: '']], wait: false
                }            
            }
        } catch(err) {
            errors << "errors found: ${err}"
        } finally {
            if (errors.size() > 0) {
                stage ('error reporting') {
                    currentBuild.result = 'FAILURE'
                    for (int i = 0; i < errors.size(); i++) {
                        echo errors[i]; 
                    }
                }                
            }
  
            stage('Clean up workspace') {
                step([$class: 'WsCleanup', deleteDirs: true, notFailBuild: true])
            }
        }
    } // end timestamps
} // end node ('android')

node('master') {
    stage('Cleaning workspace') {
        def wrk = pwd() + "@script/"
        dir("${wrk}") {
            deleteDir()
        }
    }
}