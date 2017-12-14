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
    node ('mac') {
        try {
            stage ('Checkout') {
                checkout([$class: 'GitSCM', branches: [[name: '*/'+BranchName]], doGenerateSubmoduleConfigurations: false, extensions: [[$class: 'CloneOption', depth: 0, noTags: false, reference: '', shallow: false, timeout: 15], [$class: 'CheckoutOption', timeout: 15], [$class: 'LocalBranch' , localBranch: "**"], [$class: 'WipeWorkspace'], [$class: 'PruneStaleBranch'], [$class: 'SubmoduleOption', disableSubmodules: false, parentCredentials: true, recursiveSubmodules: true, reference: '', trackingSubmodules: false]], submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'd866c69b-16f0-4fce-823a-2a42bbf90a3d', url: 'ssh://tfsemea1.ta.philips.com:22/tfs/TPC_Region24/CDP2/_git/plf-android']]])

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
                    ./gradlew clean :IconFont:test :AppInfra:cC :uid:createDebugCoverageReport :uAppFwLib:test :securedblibrary:cC :registrationApi:cC :registrationApi:test :jump:cC :jump:test :hsdp:cC :hsdp:test :productselection:cC :telehealth:testReleaseUnitTest :bluelib:generateJavadoc :bluelib:testReleaseUnitTest :product-registration-lib:test :product-registration-lib:jacocoTestReport :iap:test :digitalCareUApp:cC :digitalCareUApp:testRelease :digitalCare:cC :digitalCare:testRelease :commlib-api:generateJavadocPublicApi :commlib-ble:generateJavadocPublicApi :commlib-lan:generateJavadocPublicApi :commlib-cloud:generateJavadocPublicApi :commlib:test :commlib-testutils:testReleaseUnitTest :commlib-ble:testReleaseUnitTest :commlib-lan:testReleaseUnitTest :commlib-cloud:testReleaseUnitTest :commlib-api:testReleaseUnitTest :MyAccount:cC :MyAccount:testRelease :MyAccountUApp:cC :MyAccountUApp:testRelease :dataServices:testReleaseUnitTest :dataServicesUApp:testReleaseUnitTest :devicepairingUApp:test :ews-android:test :ews-android:jacocoTestReport :referenceApp:testAppFrameworkHamburgerReleaseUnitTest
                '''
            }

            stage ('reporting') {
                junit allowEmptyResults: false, testResults: 'Source/icf/Source/Library/**/build/test-results/**/*.xml'
                junit allowEmptyResults: false, testResults: 'Source/ail/Source/Library/*/build/outputs/androidTest-results/*/*.xml'
                junit allowEmptyResults: false, testResults: 'Source/ufw/Source/Library/*/build/test-results/*/*.xml'
                junit allowEmptyResults: false, testResults: 'Source/sdb/Source/Library/**/build/outputs/androidTest-results/*/*.xml'
                junit allowEmptyResults: false, testResults: 'Source/usr/Source/Library/**/build/test-results/**/*.xml'
                junit allowEmptyResults: false, testResults: 'Source/usr/Source/Library/**/build/outputs/androidTest-results/*/*.xml'
                junit allowEmptyResults: false, testResults: 'Source/pse/Source/Library/**/build/outputs/androidTest-results/*/*.xml'
                junit allowEmptyResults: false, testResults: 'Source/ths/Source/Library/*/build/test-results/**/*.xml'
                junit allowEmptyResults: true,  testResults: 'Source/bll/**/testReleaseUnitTest/*.xml'
                junit allowEmptyResults: true,  testResults: 'Source/prg/Source/Library/*/build/test-results/**/*.xml'
                junit allowEmptyResults: false, testResults: 'Source/iap/Source/Library/*/build/test-results/**/*.xml'
                junit allowEmptyResults: true,  testResults: 'Source/dcc/Source/DemoApp/launchDigitalCare/build/reports/lint-results.xml'
                junit allowEmptyResults: true,  testResults: 'Source/dcc/Source/DemoUApp/DemoUApp/build/reports/lint-results.xml'
                junit allowEmptyResults: true,  testResults: 'Source/dcc/Source/Library/digitalCare/build/test-results/**/*.xml'
                junit allowEmptyResults: true,  testResults: 'Source/cml/**/testReleaseUnitTest/*.xml'
                junit allowEmptyResults: true,  testResults: 'Source/mya/Source/DemoApp/app/build/test-results/**/*.xml'
                junit allowEmptyResults: true,  testResults: 'Source/mya/Source/DemoUApp/DemoUApp/build/test-results/**/*.xml'
                junit allowEmptyResults: true,  testResults: 'Source/mya/Source/Library/ConsentAccessToolkit/build/test-results/**/*.xml'
                junit allowEmptyResults: true,  testResults: 'Source/mya/Source/Library/ConsentWidgets/build/test-results/**/*.xml'
                junit allowEmptyResults: true,  testResults: 'Source/mya/Source/Library/MyAccount/build/test-results/**/*.xml'
                junit allowEmptyResults: false, testResults: 'Source/dsc/Source/Library/*/build/test-results/**/*.xml'
                junit allowEmptyResults: true,  testResults: 'Source/dpr/Source/DemoApp/*/build/test-results/*/*.xml'
                junit allowEmptyResults: true,  testResults: 'Source/dpr/Source/DemoUApp/*/build/test-results/*/*.xml'
                step([$class: 'JUnitResultArchiver', testResults: 'Source/ews/Source/Library/ews-android/build/test-results/*/*.xml'])
                junit allowEmptyResults: false, testResults: 'Source/rap/Source/AppFramework/*/build/test-results/*/*.xml' 

                publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'Source/icf/Source/Library/IconFont/icf/build/reports/tests/testDebugUnitTest', reportFiles: 'index.html', reportName: 'icf unit test debug'])
                publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'Source/icf/Source/Library/IconFont/icf/build/reports/tests/testReleaseUnitTest', reportFiles: 'index.html', reportName: 'icf unit test release'])
                publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'Source/ail/Source/Library/AppInfra/build/reports/androidTests/connected', reportFiles: 'index.html', reportName: 'ail connected tests'])
                publishHTML([allowMissing: true,  alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'Source/ail/Source/Library/AppInfra/build/reports/coverage/debug', reportFiles: 'index.html', reportName: 'ail coverage tests'])
                publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'Source/uid/Source/UIKit/uid/build/reports/androidTests/connected', reportFiles: 'index.html', reportName: 'uid Unit Tests'])
                publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'Source/uid/Source/UIKit/uid/build/reports/coverage/debug', reportFiles: 'index.html', reportName: 'uid Code Coverage'])
                publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'Source/ufw/Source/Library/uAppFwLib/build/reports/tests/testDebugUnitTest', reportFiles: 'index.html', reportName: 'ufw unit test debug'])
                publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'Source/ufw/Source/Library/uAppFwLib/build/reports/tests/testReleaseUnitTest', reportFiles: 'index.html', reportName: 'ufw unit test release'])
                publishHTML([allowMissing: true,  alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'Source/sdb/Source/Library/securedblibrary/build/reports/coverage/debug', reportFiles: 'index.html', reportName: 'sdb coverage debug'])
                publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'Source/sdb/Source/Library/securedblibrary/build/reports/androidTests/connected', reportFiles: 'index.html', reportName: 'sdb connected tests'])
                publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'Source/usr/Source/Library/RegistrationApi/build/reports/tests/testDebugUnitTest', reportFiles: 'index.html', reportName: 'usr unit test debug'])
                publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'Source/usr/Source/Library/RegistrationApi/build/reports/tests/testReleaseUnitTest', reportFiles: 'index.html', reportName: 'usr unit test release'])
                publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'Source/usr/Source/Library/RegistrationApi/build/reports/androidTests/connected', reportFiles: 'index.html', reportName: 'usr connected tests RegistrationApi'])
                publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'Source/usr/Source/Library/jump/build/reports/androidTests/connected', reportFiles: 'index.html', reportName: 'usr connected tests Jump'])
                publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'Source/usr/Source/Library/hsdp/build/reports/androidTests/connected', reportFiles: 'index.html', reportName: 'usr connected tests hsdp'])
                publishHTML([allowMissing: true,  alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'Source/pse/Source/Library/productselection/build/reports/coverage/debug', reportFiles: 'index.html', reportName: 'pse coverage debug'])
                publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'Source/pse/Source/Library/productselection/build/reports/androidTests/connected', reportFiles: 'index.html', reportName: 'pse connected tests'])
                publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'Source/ths/Source/Library/thsuapp/build/reports/tests/testReleaseUnitTest', reportFiles: 'index.html', reportName: 'ths unit test release'])
                publishHTML([allowMissing: false, alwaysLinkToLastBuild: true,  keepAll: true, reportDir: 'Source/bll/Documents/External/bluelib-api', reportFiles: 'index.html', reportName: 'bll Bluelib Public API'])
                publishHTML([allowMissing: false, alwaysLinkToLastBuild: true,  keepAll: true, reportDir: 'Source/bll/Documents/External/bluelib-plugin-api', reportFiles: 'index.html', reportName: 'bll Bluelib Plugin API'])
                publishHTML([allowMissing: false, alwaysLinkToLastBuild: true,  keepAll: true, reportDir: 'Source/bll/Source/ShineLib/shinelib/build/reports/tests/testReleaseUnitTest', reportFiles: 'index.html', reportName: 'bll unit test release'])
                step([$class: 'JacocoPublisher', execPattern: 'Source/bll/**/*.exec', classPattern: 'Source/bll/**/classes', sourcePattern: 'Source/bll/**/src/main/java', exclusionPattern: 'Source/bll/**/R.class,Source/bll/**/R$*.class,Source/bll/**/BuildConfig.class,Source/bll/**/Manifest*.*,Source/bll/**/*Activity*.*,Source/bll/**/*Fragment*.*'])
                step([$class: 'JacocoPublisher', execPattern: 'Source/ews/**/*.exec', sourcePattern: 'Source/ews/**/src/main/java', exclusionPattern: 'Source/ews/**/R.class, Source/ews/**/R$*.class, Source/ews/*/BuildConfig.class, Source/ews/**/Manifest*.*, Source/ews/**/*_Factory.class, Source/ews/**/*_*Factory.class , Source/ews/**/Dagger*.class, Source/ews/**/databinding/**/*.class, Source/ews/**/*Activity*.*, Source/ews/**/*Fragment*.*, Source/ews/**/*Service*.*, Source/ews/**/*ContentProvider*.*'])
                publishHTML(target: [keepAll: true, alwaysLinkToLastBuild: false, reportDir:  'Source/ews/Source/Library/ews-android/build/reports/jacoco/jacoco' + 'TestReleaseUnit'+'TestReport/html', reportFiles:'index.html', reportName: 'ews Overall code coverage'])
                publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'Source/prg/Source/Library/product-registration-lib/build/reports/jacoco/jacocoTestReport/html', reportFiles: 'index.html', reportName: 'prg jacocoTestReport']) 
                publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'Source/prg/Source/Library/product-registration-lib/build/reports/tests/testDebugUnitTest', reportFiles: 'index.html', reportName: 'prg unit test debug']) 
                publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'Source/prg/Source/Library/product-registration-lib/build/reports/tests/testReleaseUnitTest', reportFiles: 'index.html', reportName: 'prg unit test release'])
                publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'Source/iap/Source/Library/iap/build/reports/tests/testDebugUnitTest', reportFiles: 'index.html', reportName: 'iap unit test debug'])
                publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'Source/iap/Source/Library/iap/build/reports/tests/testReleaseUnitTest', reportFiles: 'index.html', reportName: 'iap unit test release'])
                publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'Source/dcc/Source/Library/digitalCare/build/reports/tests/testReleaseUnitTest', reportFiles: 'index.html', reportName: 'dcc unit test release'])

                def cucumber_path = 'Source/cml/Source/Library/build/cucumber-reports'
                def cucumber_filename = 'Source/cml/cucumber-report-android-commlib.json'
                step([$class: 'JacocoPublisher', execPattern: 'Source/cml/**/*.exec', classPattern: 'Source/cml/**/classes', sourcePattern: 'Source/cml/**/src/main/java', exclusionPattern: 'Source/cml/**/R.class,Source/cml/**/R$*.class,Source/cml/**/BuildConfig.class,Source/cml/**/Manifest*.*,Source/cml/**/*Activity*.*,Source/cml/**/*Fragment*.*,Source/cml/**/*Test*.*'])
                for (lib in ["commlib-api", "commlib-ble", "commlib-lan", "commlib-cloud"]) {
                    publishHTML([allowMissing: false, alwaysLinkToLastBuild: true, keepAll: true, reportDir: "Source/cml/Source/Library/${lib}/build/reports/tests/testReleaseUnitTest", reportFiles: 'index.html', reportName: "cml $lib unit test release"])
                    publishHTML([allowMissing: false, alwaysLinkToLastBuild: true, keepAll: true, reportDir: "Source/cml/Documents/External/$lib-api", reportFiles: 'index.html', reportName: "cml $lib API documentation"])
                }

                if (fileExists("$cucumber_path/report.json")) {
                    step([$class: 'CucumberReportPublisher', jsonReportDirectory: cucumber_path, fileIncludePattern: '*.json'])
                } else {
                    echo 'No Cucumber result found, nothing to publish'
                }

                publishHTML([allowMissing: true, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'Source/mya/Source/DemoApp/app/build/reports/tests/testReleaseUnitTest', reportFiles: 'index.html', reportName: 'mya DemoApp - release test'])
                publishHTML([allowMissing: true, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'Source/mya/Source/DemoUApp/DemoUApp/build/reports/tests/testReleaseUnitTest', reportFiles: 'index.html', reportName: 'mya DemoUApp - release test'])
                publishHTML([allowMissing: true, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'Source/mya/Source/Library/ConsentAccessToolkit/build/reports/tests/testReleaseUnitTest', reportFiles: 'index.html', reportName: 'mya ConsentAccessToolkit - release test'])
                publishHTML([allowMissing: true, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'Source/mya/Source/Library/ConsentWidgets/build/reports/tests/testReleaseUnitTest', reportFiles: 'index.html', reportName: 'mya ConsentWidgets - release test'])
                publishHTML([allowMissing: true, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'Source/mya/Source/Library/MyAccount/build/reports/tests/testReleaseUnitTest', reportFiles: 'index.html', reportName: 'mya MyAccount - release test'])
                // DexCount
                publishHTML([allowMissing: true,  alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'Source/mya/Source/DemoApp/app/build/outputs/dexcount/deviceDebugChart', reportFiles: 'index.html', reportName: 'mya DexCount'])
                publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'Source/dsc/Source/Library/dataServices/build/reports/tests/testReleaseUnitTest', reportFiles: 'index.html', reportName: 'dsc unit test release'])
                publishHTML([allowMissing: true,  alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'Source/dpr/Source/DemoApp/app/build/reports/tests/testDebugUnitTest', reportFiles: 'index.html', reportName: 'dpr unit test debug'])
                publishHTML([allowMissing: true,  alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'Source/dpr/Source/DemoApp/app/build/reports/tests/testReleaseUnitTest', reportFiles: 'index.html', reportName: 'dpr unit test release'])
                publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'Source/rap/Source/AppFramework/appFramework/build/reports/tests/testAppFrameworkHamburgerReleaseUnitTest', reportFiles: 'index.html', reportName: 'rap AppFramework Hamburger Release UnitTest'])  
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

            stage('Cleaning workspace') {
               step([$class: 'WsCleanup', deleteDirs: true, notFailBuild: true])
            }

            stage('informing') {
                step([$class: 'Mailer', notifyEveryUnstableBuild: true, recipients: MailRecipient, sendToIndividuals: true])
            }
        }
    } //node

    node ('mac') {
        try {
            stage ('Checkout') {
                checkout([$class: 'GitSCM', branches: [[name: '*/'+BranchName]], doGenerateSubmoduleConfigurations: false, extensions: [[$class: 'CloneOption', depth: 0, noTags: false, reference: '', shallow: false, timeout: 15], [$class: 'CheckoutOption', timeout: 15], [$class: 'LocalBranch' , localBranch: "**"], [$class: 'WipeWorkspace'], [$class: 'PruneStaleBranch'], [$class: 'SubmoduleOption', disableSubmodules: false, parentCredentials: true, recursiveSubmodules: true, reference: '', trackingSubmodules: false]], submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'd866c69b-16f0-4fce-823a-2a42bbf90a3d', url: 'ssh://tfsemea1.ta.philips.com:22/tfs/TPC_Region24/CDP2/_git/plf-android']]])

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
                    ./gradlew --refresh-dependencies clean assembleRelease
                '''
            }

            stage ('lint') {
/*                 sh '''#!/bin/bash -l
                    set -e
                    #chmod -R 755 .
                    #do not use -PenvCode=${JENKINS_ENV} since the option 'opa' is hardcoded in the archive
                    ./gradlew :IconFont:lint :AppInfra:lint :uikitLib:lint :securedblibrary:lint :registrationApi:lint :productselection:lint :telehealth:lintRelease :bluelib:lintDebug :product-registration-lib:lint :iap:lint :digitalCare:lint :cloudcontroller-api:lintDebug :commlib:lintDebug :MyAccount:lint :MyAccountUApp:lint :dataServices:lintRelease :devicepairingUApp:lint :ews-android:lint :ewsUApp:lint :pushnotification:lintRelease :themesettings:lintRelease
                    #prx:lint and rap:lintRelease are not working and we are keeping it as known issues
                ''' */
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
                        #./gradlew artifactoryPublish
                        #:referenceApp:printArtifactoryApkPath
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

                        cd $BASE_PATH/Source/rap/Source/AppFramework/appFramework/build/outputs/apk
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
                            echo "$ARTIFACTORY_URL/$ARTIFACTORY_REPO/com/philips/cdp/referenceApp/LeakCanary/$APK_NAME" > $BASE_PATH/Source/rap/Source/AppFramework/apkname.txt
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
           //    androidLint canComputeNew: false, canRunOnFailed: true, defaultEncoding: '', healthy: '', pattern: '', shouldDetectModules: true, unHealthy: '', unstableTotalHigh: ''
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

            stage('Cleaning workspace') {
               step([$class: 'WsCleanup', deleteDirs: true, notFailBuild: true])
            }

            stage('informing') {
                step([$class: 'Mailer', notifyEveryUnstableBuild: true, recipients: MailRecipient, sendToIndividuals: true])
            }
        }
    } // end node ('android')
}//timestamp

node('master') {
    stage('Cleaning workspace') {
//Disabled the skipping of clean-up for now.
//        if (BranchName =~ /master|develop|release\/platform_.*/) {
//            echo "${BranchName} does not get cleared"
//        } else {
            def wrk = pwd() + "@script/"
            dir("${wrk}") {
                deleteDir()
            }
//        }
    }
}