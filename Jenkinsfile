#!/usr/bin/env groovy
BranchName = env.BRANCH_NAME
JENKINS_ENV = env.JENKINS_ENV

properties([
    [$class: 'ParametersDefinitionProperty', parameterDefinitions: [
        [$class: 'BooleanParameterDefinition', defaultValue: false, description: 'Force PSRA build ', name : 'PSRAbuild'],
        [$class: 'BooleanParameterDefinition', defaultValue: false, description: 'LeakCanary build ', name : 'LeakCanarybuild']
    ]],
    [$class: 'BuildDiscarderProperty', strategy: [$class: 'LogRotator', numToKeepStr: '24']]
])

def MailRecipient = 'DL_CDP2_Callisto@philips.com'
def errors = []

timestamps {
    try {
        node ('android && device') {
            stage ('Checkout') {
                checkout([$class: 'GitSCM', branches: [[name: '*/'+BranchName]], doGenerateSubmoduleConfigurations: false, extensions: [[$class: 'LocalBranch' , localBranch: "**"], [$class: 'WipeWorkspace'], [$class: 'PruneStaleBranch'], [$class: 'CloneOption', depth: 1, noTags: false, reference: '', shallow: true], [$class: 'SubmoduleOption', disableSubmodules: false, parentCredentials: true, recursiveSubmodules: true, reference: '', trackingSubmodules: false]], submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'd866c69b-16f0-4fce-823a-2a42bbf90a3d', url: 'ssh://tfsemea1.ta.philips.com:22/tfs/TPC_Region24/CDP2/_git/opa-android']]])

                sh """#!/bin/bash -le
                    echo "---------------------- Printing Environment --------------------------"
                    env | sort
                    echo "----------------------- End of Environment ---------------------------"
                """
            }

            stage ('unit test') {
                sh '''#!/bin/bash -l
                    set -e
                    chmod -R 755 . 
                    #do not use -PenvCode=${JENKINS_ENV} since the option 'opa' is hardcoded in the archive
                    ./gradlew :AppInfra:cC :uid:createDebugCoverageReport :productselection:cC :registrationApi:cC :registrationApi:test :product-registration-lib:test :product-registration-lib:jacocoTestReport :securedblibrary:cC :digitalCareUApp:cC :digitalCareUApp:testRelease :digitalCare:cC :digitalCare:testRelease :IconFont:test :uAppFwLib:test :devicepairingUApp:test :iap:test :dataServices:testReleaseUnitTest :dataServicesUApp:testReleaseUnitTest :MyAccount:cC :MyAccount:testRelease :MyAccountUApp:cC :MyAccountUApp:testRelease :commlib-api:generateJavadocPublicApi :commlib-ble:generateJavadocPublicApi :commlib-lan:generateJavadocPublicApi :commlib-cloud:generateJavadocPublicApi :commlib:test :commlib-testutils:testReleaseUnitTest :commlib-ble:testReleaseUnitTest :commlib-lan:testReleaseUnitTest :commlib-cloud:testReleaseUnitTest :commlib-api:testReleaseUnitTest :shinelib:generateJavadoc :shinelib:test
                '''
            }

            stage ('reporting') {
                junit allowEmptyResults: false, testResults: 'Source/Library/ail/Source/Library/*/build/outputs/androidTest-results/*/*.xml'
                junit allowEmptyResults: false, testResults: 'Source/Library/pse/Source/Library/**/build/outputs/androidTest-results/*/*.xml'
                junit allowEmptyResults: false, testResults: 'Source/Library/sdb/Source/Library/**/build/outputs/androidTest-results/*/*.xml'
                junit allowEmptyResults: true, testResults: 'Source/Library/dcc/Source/DemoApp/launchDigitalCare/build/reports/lint-results.xml'
                junit allowEmptyResults: true, testResults: 'Source/Library/dcc/Source/DemoUApp/DemoUApp/build/reports/lint-results.xml'
                junit allowEmptyResults: true, testResults: 'Source/Library/dcc/Source/Library/digitalCare/build/test-results/**/*.xml'
                def cucumber_path = 'Source/Library/cml/Source/Library/build/cucumber-reports'
                def cucumber_filename = 'Source/Library/cml/cucumber-report-android-commlib.json'
                junit allowEmptyResults: false, testResults: 'Source/Library/icf/Source/Library/**/build/test-results/**/*.xml'
                junit allowEmptyResults: true,  testResults: 'Source/Library/prg/Source/Library/*/build/test-results/**/*.xml'
                junit allowEmptyResults: false, testResults: 'Source/Library/ufw/Source/Library/*/build/test-results/*/*.xml'
                junit allowEmptyResults: true,  testResults: 'Source/Library/dpr/Source/DemoApp/*/build/test-results/*/*.xml'
                junit allowEmptyResults: true,  testResults: 'Source/Library/dpr/Source/DemoUApp/*/build/test-results/*/*.xml'
                junit allowEmptyResults: true,  testResults: 'Source/Library/bll/**/testReleaseUnitTest/*.xml'
                junit allowEmptyResults: true,  testResults: 'Source/Library/cml/**/testReleaseUnitTest/*.xml'
                junit allowEmptyResults: false, testResults: 'Source/Library/iap/Source/Library/*/build/test-results/**/*.xml'
                junit allowEmptyResults: false, testResults: 'Source/Library/dsc/Source/Library/*/build/test-results/**/*.xml'
                junit allowEmptyResults: true,  testResults: 'Source/Library/mya/Source/DemoUApp/DemoUApp/build/test-results/**/*.xml'
                junit allowEmptyResults: true,  testResults: 'Source/Library/mya/Source/Library/ConsentAccessToolkit/build/test-results/**/*.xml'
                junit allowEmptyResults: true,  testResults: 'Source/Library/mya/Source/Library/ConsentWidgets/build/test-results/**/*.xml'
                junit allowEmptyResults: true,  testResults: 'Source/Library/mya/Source/Library/MyAccount/build/test-results/**/*.xml'
                publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'Source/Library/icf/Source/Library/IconFont/icf/build/reports/tests/testDebugUnitTest', reportFiles: 'index.html', reportName: 'icf unit test debug'])
                publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'Source/Library/icf/Source/Library/IconFont/icf/build/reports/tests/testReleaseUnitTest', reportFiles: 'index.html', reportName: 'icf unit test release'])
                publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'Source/Library/ufw/Source/Library/uAppFwLib/build/reports/tests/testDebugUnitTest', reportFiles: 'index.html', reportName: 'ufw unit test debug']) 
                publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'Source/Library/ufw/Source/Library/uAppFwLib/build/reports/tests/testReleaseUnitTest', reportFiles: 'index.html', reportName: 'ufw unit test release']) 
                publishHTML([allowMissing: true,  alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'Source/Library/dpr/Source/DemoApp/app/build/reports/tests/testDebugUnitTest', reportFiles: 'index.html', reportName: 'unit test debug'])
                publishHTML([allowMissing: true,  alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'Source/Library/dpr/Source/DemoApp/app/build/reports/tests/testReleaseUnitTest', reportFiles: 'index.html', reportName: 'unit test release'])
                publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'Source/Library/iap/Source/Library/iap/build/reports/tests/testReleaseUnitTest', reportFiles: 'index.html', reportName: 'iap unit test release'])
                publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'Source/Library/dsc/Source/Library/dataServices/build/reports/tests/testReleaseUnitTest', reportFiles: 'index.html', reportName: 'dsc unit test release'])
                publishHTML([allowMissing: true, alwaysLinkToLastBuild: false, keepAll:  true, reportDir: 'Source/Library/mya/Source/DemoUApp/DemoUApp/build/reports/tests/testReleaseUnitTest', reportFiles: 'index.html', reportName: 'mya DemoUApp - release test'])
                publishHTML([allowMissing: true, alwaysLinkToLastBuild: false, keepAll:  true, reportDir: 'Source/Library/mya/Source/Library/ConsentAccessToolkit/build/reports/tests/testReleaseUnitTest', reportFiles: 'index.html', reportName: 'mya ConsentAccessToolkit - release test'])
                publishHTML([allowMissing: true, alwaysLinkToLastBuild: false, keepAll:  true, reportDir: 'Source/Library/mya/Source/Library/ConsentWidgets/build/reports/tests/testReleaseUnitTest', reportFiles: 'index.html', reportName: 'mya ConsentWidgets - release test'])
                publishHTML([allowMissing: true, alwaysLinkToLastBuild: false, keepAll:  true, reportDir: 'Source/Library/mya/Source/Library/MyAccount/build/reports/tests/testReleaseUnitTest', reportFiles: 'index.html', reportName: 'mya MyAccount - release test'])
//                publishHTML([allowMissing: false, alwaysLinkToLastBuild: true,  keepAll: true, reportDir: 'Source/Library/bll/Documents/External/shinelib-api', reportFiles: 'index.html', reportName: 'Bluelib Public API'])
//                publishHTML([allowMissing: false, alwaysLinkToLastBuild: true,  keepAll: true, reportDir: 'Source/Library/bll/Documents/External/shinelib-plugin-api', reportFiles: 'index.html', reportName: 'Bluelib Plugin API'])
                step([$class: 'JacocoPublisher', execPattern: 'Source/Library/bll/**/*.exec', classPattern: 'Source/Library/bll/**/classes', sourcePattern: 'Source/Library/bll/**/src/main/java', exclusionPattern: 'Source/Library/bll/**/R.class,Source/Library/bll/**/R$*.class,Source/Library/bll/**/BuildConfig.class,Source/Library/bll/**/Manifest*.*,Source/Library/bll/**/*Activity*.*,Source/Library/bll/**/*Fragment*.*'])
                step([$class: 'JacocoPublisher', execPattern: 'Source/Library/cml/**/*.exec', classPattern: 'Source/Library/cml/**/classes', sourcePattern: 'Source/Library/cml/**/src/main/java', exclusionPattern: 'Source/Library/cml/**/R.class,Source/Library/cml/**/R$*.class,Source/Library/cml/**/BuildConfig.class,Source/Library/cml/**/Manifest*.*,Source/Library/cml/**/*Activity*.*,Source/Library/cml/**/*Fragment*.*,Source/Library/cml/**/*Test*.*'])
                for (lib in ["commlib-lan"]) {
                    publishHTML([allowMissing: false, alwaysLinkToLastBuild: true, keepAll: true, reportDir: "Source/Library/cml/Documents/External/$lib-api", reportFiles: 'index.html', reportName: "$lib API documentation"])
                }

                if (fileExists("$cucumber_path/report.json")) {
                    step([$class: 'CucumberReportPublisher', jsonReportDirectory: cucumber_path, fileIncludePattern: '*.json'])
                } else {
                    echo 'No Cucumber result found, nothing to publish'
                }                        
                publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'Source/Library/ail/Source/Library/AppInfra/build/reports/androidTests/connected', reportFiles: 'index.html', reportName: 'ail connected tests']) 
                publishHTML([allowMissing: true,  alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'Source/Library/ail/Source/Library/AppInfra/build/reports/coverage/debug', reportFiles: 'index.html', reportName: 'ail coverage tests']) 
                publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'Source/Library/uid/Source/UIKit/uid/build/reports/androidTests/connected', reportFiles: 'index.html', reportName: 'uid Unit Tests'])
                publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'Source/Library/uid/Source/UIKit/uid/build/reports/coverage/debug', reportFiles: 'index.html', reportName: 'uid Code Coverage'])
                publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'Source/Library/prg/Source/Library/product-registration-lib/build/reports/jacoco/jacocoTestReport/html', reportFiles: 'index.html', reportName: 'prg jacocoTestReport']) 
                publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'Source/Library/prg/Source/Library/product-registration-lib/build/reports/tests/testDebugUnitTest', reportFiles: 'index.html', reportName: 'prg unit test debug']) 
                publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'Source/Library/prg/Source/Library/product-registration-lib/build/reports/tests/testReleaseUnitTest', reportFiles: 'index.html', reportName: 'prg unit test release']) 
                publishHTML([allowMissing: true,  alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'Source/Library/pse/Source/Library/productselection/build/reports/coverage/debug', reportFiles: 'index.html', reportName: 'pse coverage debug']) 
                publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'Source/Library/pse/Source/Library/productselection/build/reports/androidTests/connected', reportFiles: 'index.html', reportName: 'pse connected tests']) 
                publishHTML([allowMissing: true,  alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'Source/Library/sdb/Source/Library/securedblibrary/build/reports/coverage/debug', reportFiles: 'index.html', reportName: 'sdb coverage debug']) 
                publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'Source/Library/sdb/Source/Library/securedblibrary/build/reports/androidTests/connected', reportFiles: 'index.html', reportName: 'sdb connected tests']) 
                publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'Source/Library/dcc/Source/Library/digitalCare/build/reports/tests/testReleaseUnitTest', reportFiles: 'index.html', reportName: 'dcc unit test release'])
            }

            stage('Cleaning workspace') {
                step([$class: 'WsCleanup', deleteDirs: true, notFailBuild: true])
            }
        }

        node ('android && docker && localdisk') {
            stage ('Checkout 3') {
                checkout([$class: 'GitSCM', branches: [[name: '*/'+BranchName]], doGenerateSubmoduleConfigurations: false, extensions: [[$class: 'LocalBranch' , localBranch: "**"], [$class: 'WipeWorkspace'], [$class: 'PruneStaleBranch'], [$class: 'CloneOption', depth: 1, noTags: false, reference: '', shallow: true], [$class: 'SubmoduleOption', disableSubmodules: false, parentCredentials: true, recursiveSubmodules: true, reference: '', trackingSubmodules: false]], submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'd866c69b-16f0-4fce-823a-2a42bbf90a3d', url: 'ssh://tfsemea1.ta.philips.com:22/tfs/TPC_Region24/CDP2/_git/opa-android']]])

                sh """#!/bin/bash -le
                    echo "---------------------- Printing Environment --------------------------"
                    env | sort
                    echo "----------------------- End of Environment ---------------------------"
                """
            }

            stage ('build') {
                sh '''#!/bin/bash -l
                    set -e
                    chmod -R 755 . 
                    #do not use -PenvCode=${JENKINS_ENV} since the option 'opa' is hardcoded in the archive
                    ./gradlew --refresh-dependencies clean
                    ./gradlew assembleRelease
                '''
            }

            stage ('lint') {
                sh '''#!/bin/bash -l
                    set -e
                    #chmod -R 755 . 
                    #do not use -PenvCode=${JENKINS_ENV} since the option 'opa' is hardcoded in the archive
                    ./gradlew :AppInfra:lint :uikitLib:lint :IconFont:lint :registrationApi:lint :iap:lint :digitalCare:lint :productselection:lint :product-registration-lib:lint :dataServices:lintRelease :devicepairingUApp:lint :cloudcontroller-api:lintDebug :commlib:lintDebug :bluelib:lintDebug
                    #prx:lint and rap:lintRelease are not working and we are keeping it as known issues
                '''
            }

            if (params.PSRAbuild && (BranchName =~ /master|release\/platform_.*/))  {
                stage ('build PSRA') {
                    sh '''#!/bin/bash -l
                        chmod -R 775 .
                        cd ./Source/AppFramework
                        ./gradlew -PenvCode=${JENKINS_ENV} assemblePsraRelease
                    '''
                }
            } else {
                if (params.PSRAbuild) {
                    echo "PSRA build is not supported for Branch: ${BranchName}"
                }

                if (params.LeakCanarybuild && (BranchName =~ /master|release\/platform_.*/))  {
                    stage ('build LeakCanary') {
                        sh '''#!/bin/bash -l
                            chmod -R 775 .
                            cd ./Source/AppFramework
                            ./gradlew -PenvCode=${JENKINS_ENV} assembleLeakCanary
                       '''
                    }
                } else {
                    if (params.LeakCanarybuild) {
                        echo "Leak Canary build is not supported for Branch: ${BranchName}"
                    }
                }
            }

            if (BranchName =~ /master|develop|release\/platform_.*/) {
                stage ('publish') {
                    echo "Publish to artifactory"
                    sh '''#!/bin/bash -l
                        set -e
                        ./gradlew artifactoryPublish :referenceApp:printArtifactoryApkPath
                    '''
                }
            }

            if (params.LeakCanarybuild) {
                stage('publishing leakcanaryapps') {
                    boolean MasterBranch = (BranchName ==~ /master.*/)
                    boolean ReleaseBranch = (BranchName ==~ /release\/platform_.*/)
                    boolean DevelopBranch = (BranchName ==~ /develop.*/)

                    def shellcommand = '''#!/bin/bash -l
                        export BASE_PATH=`pwd`
                        echo $BASE_PATH
                        TIMESTAMP=`date -u +%Y%m%d%H%M%S`
                        TIMESTAMPEXTENSION=".$TIMESTAMP"
                        
                        cd $BASE_PATH/Source/Library/rap/Source/AppFramework/appFramework/build/outputs/apk
                        PUBLISH_APK=false
                        APK_NAME="RefApp_LeakCanary_"${TIMESTAMP}".apk"
                        ARTIFACTORY_URL="http://artifactory-ehv.ta.philips.com:8082/artifactory"
                        ARTIFACTORY_REPO="unknown"
                        
                        if [ '''+MasterBranch+''' = true ]
                        then
                            PUBLISH_APK=true
//                            ARTIFACTORY_REPO="platform-pkgs-android-release"
                            ARTIFACTORY_REPO="platform-pkgs-opa-android-release"
                        elif [ '''+ReleaseBranch+''' = true ]
                        then
                            PUBLISH_APK=true
//                            ARTIFACTORY_REPO="platform-pkgs-android-stage"
                            ARTIFACTORY_REPO="platform-pkgs-opa-android-stage"
                        elif [ '''+DevelopBranch+''' = true ]
                        then
                            PUBLISH_APK=true
//                            ARTIFACTORY_REPO="platform-pkgs-android-snapshot"
                            ARTIFACTORY_REPO="platform-pkgs-opa-android-snapshot"
                        else
                            echo "Not published as build is not on a master, develop or release branch" . $BranchName
                        fi
                        
                        if [ $PUBLISH_APK = true ]
                        then
                            mv referenceApp-leakCanary.apk $APK_NAME
                            curl -L -u readerwriter:APBcfHoo7JSz282DWUzMVJfUsah -X PUT $ARTIFACTORY_URL/$ARTIFACTORY_REPO/com/philips/cdp/referenceApp/LeakCanary/ -T $APK_NAME
                            echo "$ARTIFACTORY_URL/$ARTIFACTORY_REPO/com/philips/cdp/referenceApp/LeakCanary/$APK_NAME" > $BASE_PATH/Source/Library/rap/Source/AppFramework/apkname.txt
                        fi
                        
                        if [ $? != 0 ]
                        then
                            exit 1
                        else
                            cd $BASE_PATH
                        fi
                    '''
                    sh shellcommand
                }
            }

//            stage('E2E test') {
//                if (BranchName =~ /master|develop|release\/platform_.*/) {
//                    APK_NAME = readFile("apkname.txt").trim()
//                    echo "APK_NAME = ${APK_NAME}"
//                    if(params.LeakCanarybuild){
//                        build job: "Platform-Infrastructure/E2E_Tests/Reliability/LeakCanary_Android_develop", parameters: [[$class: 'StringParameterValue', name: 'APKPATH', value:APK_NAME]], wait: false
//                    } else {
//                        def jobBranchName = BranchName.replace('/', '_')
//                        echo "jobBranchName = ${jobBranchName}"
//                        build job: "Platform-Infrastructure/E2E_Tests/E2E_Android_${jobBranchName}", parameters: [[$class: 'StringParameterValue', name: 'APKPATH', value:APK_NAME]], wait: false
//                    }
//                }
//            }

            stage ('reporting') {
               androidLint canComputeNew: false, canRunOnFailed: true, defaultEncoding: '', healthy: '', pattern: '', shouldDetectModules: true, unHealthy: '', unstableTotalHigh: ''
            }
    
            stage('informing') {
                step([$class: 'Mailer', notifyEveryUnstableBuild: true, recipients: MailRecipient, sendToIndividuals: true])
            }
    
            stage('Cleaning workspace') {
               step([$class: 'WsCleanup', deleteDirs: true, notFailBuild: true])
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
    } // end timestamps
} // end node ('android')

node('master') {
    stage('Cleaning workspace') {
        if (BranchName =~ /master|develop|release\/platform_.*/) {
            echo "${BranchName} does not get cleared"
        } else {
            def wrk = pwd() + "@script/"
            dir("${wrk}") {
                deleteDir()
            }
        }
    }
}