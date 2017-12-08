/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

/* following line is mandatory for the platform CI pipeline integration */
properties([[$class: 'ParametersDefinitionProperty', parameterDefinitions: [[$class: 'StringParameterDefinition', defaultValue: '', description: 'triggerBy', name: 'triggerBy']]]])

def errors = []

node('Android') {
    timestamps {
        try {
            stage('Checkout') {
                checkout scm
            }

            Pipeline = load "Source/common/jenkins/Pipeline.groovy"
            Slack = load "Source/common/jenkins/Slack.groovy"
            def gradle = 'cd ./Source/ShineLib && ./gradlew -PenvCode=${JENKINS_ENV}'

            Slack.notify('#conartists') {
                stage('Build') {
                    sh "$gradle --refresh-dependencies assembleRelease saveResDep saveAllResolvedDependencies saveAllResolvedDependenciesGradleFormat assembleDebug saveResDep generateJavadoc"
                }

                stage('Unit test') {
                    sh 'find . -path "**build/test-results" -exec rm -r "{}" \\;'
                    sh "$gradle test lintDebug || true"
                }

                // stage('Mutation testing') {
                //     sh "$gradle pitestDebug"
                // }

                stage("Gather reports") {
                    step([$class: 'JUnitResultArchiver', testResults: '**/testDebugUnitTest/*.xml'])
                    step([$class: 'LintPublisher', healthy: '0', unHealthy: '20', unstableTotalAll: '20'])
                    step([$class: 'JacocoPublisher', execPattern: '**/*.exec', classPattern: '**/classes', sourcePattern: '**/src/main/java', exclusionPattern: '**/R.class,**/R$*.class,**/BuildConfig.class,**/Manifest*.*,**/*Activity*.*,**/*Fragment*.*'])
                    //publishHTML([allowMissing: false, alwaysLinkToLastBuild: true, keepAll: true, reportDir: 'Source/ShineLib/build/report/shinelib/pitest/debug/', reportFiles: 'index.html', reportName: 'Pitest'])
                    publishHTML([allowMissing: false, alwaysLinkToLastBuild: true, keepAll: true, reportDir: 'Documents/External/shinelib-api', reportFiles: 'index.html', reportName: 'Bluelib Public API'])
                    publishHTML([allowMissing: false, alwaysLinkToLastBuild: true, keepAll: true, reportDir: 'Documents/External/shinelib-plugin-api', reportFiles: 'index.html', reportName: 'Bluelib Plugin API'])
                }

                stage('Archive artifacts') {
                    archiveArtifacts artifacts: '**/build/outputs/aar/*.aar', fingerprint: true, onlyIfSuccessful: true
                    archiveArtifacts artifacts: '**/build/outputs/apk/*.apk', fingerprint: true, onlyIfSuccessful: true
                    archiveArtifacts '**/*dependencies*.lock'
                }

                boolean publishing = (env.BRANCH_NAME.startsWith("develop") || env.BRANCH_NAME.startsWith("release/platform_") || env.BRANCH_NAME.startsWith("master"))
                if (publishing) {
                    stage('Publish') {
                        sh "$gradle zipDocuments artifactoryPublish"
                    }
                }
            }
            Pipeline.trigger(env.triggerBy, env.BRANCH_NAME, "BlueLib", "bll")

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
}

node('master') {
    stage('Cleaning workspace') {
        def wrk = pwd() + "@script/"
        dir("${wrk}") {
            deleteDir()
        }
    }
}