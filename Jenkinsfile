#!/usr/bin/env groovy
// please look at: https://jenkins.io/doc/book/pipeline/syntax/
BranchName = env.BRANCH_NAME
 String cron_string = BRANCH_NAME == "develop" ? "H H(20-22) * * *" : ""

def MailRecipient = 'DL_CDP2_Callisto@philips.com'
def committerName = ""

pipeline {
    agent {
        node {
            label 'android && device'
        }
    }
    parameters {
        booleanParam(name: 'Nightly', defaultValue: false, description: 'Nightly build')
        choice(choices: 'Normal\nPSRA\nLeakCanary', description: 'What type of build to build?', name: 'buildType')
    }
    triggers {
        cron(cron_string)
    }
    environment {
        TRIGGER_BY_TIMER = 'false'
    }
    options {
        timestamps()
        buildDiscarder(logRotator(numToKeepStr: '24'))
    }
    stages {
        stage('Build+test') {
            steps {
                script {
                    committerName = sh (script: "git show -s --format='%an' HEAD", returnStdout: true).trim()
                    echo "Submitter: " + committerName

                    def causes = currentBuild.rawBuild.getCauses()
                    for(cause in causes) {
                        if (cause.class.toString().contains("TimerTriggerCause")) {
                            echo "This job was caused by job timer trigger"
                            TRIGGER_BY_TIMER = 'true'
                        } else {
                            echo "Nothing to do, not triggered by timer"
                        }
                    }
                }
                echo "TRIGGER_BY_TIMER : " + TRIGGER_BY_TIMER
                checkout scm
                BuildAndUnitTest()
            }
        }

        stage('Publish tests') {
            steps {
                PublishUnitTestsresults()
            }
        }

        stage('Lint+Jacoco') {
            when {
                expression { return TRIGGER_BY_TIMER=='true' }
            }
            steps {
                BuildLint()
                PublishLintJacocoresults()
            }
        }

        stage('PSRAbuild') {
            when {
                expression { return params.buildType == 'PSRA' }
                anyOf { branch 'master'; branch 'develop'; branch 'release/platform_.*' }
            }
            steps {
                sh '''#!/bin/bash -l
                    chmod -R 775 .
                    ./gradlew referenceApp:assemblePsraRelease
                '''
            }
        }

        stage('LeakCanarybuild') {
            when {
                expression { return params.buildType == 'LeakCanary' }
                anyOf { branch 'master'; branch 'develop'; branch 'release/platform_.*' } 
            }
            steps {
                sh '''#!/bin/bash -l
                    chmod -R 775 .
                    ./gradlew referenceApp:assembleLeakCanary
                '''
                DeployingLeakCanaryArtifacts()
            }
        }

        stage('Publish to artifactory') {
            when {
                anyOf { branch 'master'; branch 'develop'; branch 'release/platform_.*' }
            }
            steps {
                sh '''#!/bin/bash -l
                    set -e
                    ./gradlew saveResDep zipDocuments artifactoryPublish :referenceApp:printArtifactoryApkPath
                '''
            }
        }

        stage('Trigger E2E Test') {
            when {
                not { expression { return params.buildType == 'LeakCanary' } }
                anyOf { branch 'master'; branch 'develop'; branch 'release/platform_.*' }
            }
            steps {
                script {
                    APK_NAME = readFile("apkname.txt").trim()
                    echo "APK_NAME = ${APK_NAME}"

                    def jobBranchName = BranchName.replace('/', '_')
                    echo "jobBranchName = ${jobBranchName}"
                    sh """#!/bin/bash -le
                        curl -X POST http://310256016:61a84d6f3e9343128dff5736ef68259e@cdp2-jenkins.htce.nl.philips.com:8080/job/Platform-Infrastructure/job/E2E_Tests/job/E2E_Android_${jobBranchName}/buildWithParameters?APKPATH=$APK_NAME
                    """
                }
            }
        }

        stage('LeakCanary E2E Test') {
            when {
                expression { return params.buildType == 'LeakCanary' }
                anyOf { branch 'master'; branch 'develop'; branch 'release/platform_.*' }
            }
            steps {
                script {
                    APK_NAME = readFile("apkname.txt").trim()
                    echo "APK_NAME = ${APK_NAME}"

                    def jobBranchName = BranchName.replace('/', '_')
                    echo "jobBranchName = ${jobBranchName}"
                    sh """#!/bin/bash -le
                        curl -X POST http://310256016:61a84d6f3e9343128dff5736ef68259e@cdp2-jenkins.htce.nl.philips.com:8080/job/Platform-Infrastructure/job/E2E_Tests/job/Reliability/job/LeakCanary_Android_develop/buildWithParameters?APKPATH=$APK_NAME
                    """
                }
            }
        }

    }
    post {
        always{
            script {
                currentBuild.description = "Submitter: " + committerName
            }
            deleteDir()
            step([$class: 'Mailer', notifyEveryUnstableBuild: true, recipients: MailRecipient, sendToIndividuals: true])
        }
    }
}

def BuildAndUnitTest() {
    sh '''#!/bin/bash -l
        set -e
        chmod -R 755 .
         ./gradlew --refresh-dependencies assembleRelease :uid:cC :AppInfra:cC :uAppFwLib:test :securedblibrary:cC :registrationApi:cC :registrationApi:test :jump:cC :jump:test :hsdp:cC :hsdp:test :productselection:cC :telehealth:testReleaseUnitTest :bluelib:generateJavadoc :bluelib:testReleaseUnitTest :product-registration-lib:test :iap:test :digitalCareUApp:cC :digitalCareUApp:testRelease :digitalCare:cC :digitalCare:testRelease :commlib-api:generateJavadocPublicApi :commlib-ble:generateJavadocPublicApi :commlib-lan:generateJavadocPublicApi :commlib-cloud:generateJavadocPublicApi :commlib:test :commlib-testutils:testReleaseUnitTest :commlib-ble:testReleaseUnitTest :commlib-lan:testReleaseUnitTest :commlib-cloud:testReleaseUnitTest :commlib-api:testReleaseUnitTest :mya:cC :mya:testRelease :MyAccountUApp:cC :MyAccountUApp:testRelease :mya-catk:testReleaseUnitTest :mya-csw:testReleaseUnitTest :mya-chi:testReleaseUnitTest :mya-mch:testReleaseUnitTest :dataServices:testReleaseUnitTest :dataServicesUApp:testReleaseUnitTest :devicepairingUApp:test :ews-android:test :referenceApp:testAppFrameworkHamburgerReleaseUnitTest
    '''
}

def BuildLint() {
    sh '''#!/bin/bash -l
        set -e
        #do not use -PenvCode=${JENKINS_ENV} since the option 'opa' is hardcoded in the archive
        ./gradlew :IconFont:lint :AppInfra:lint :uikitLib:lint :securedblibrary:lint :registrationApi:lint :productselection:lint :telehealth:lintRelease :bluelib:lintDebug :product-registration-lib:lint :iap:lint :digitalCare:lint :cloudcontroller-api:lintDebug :commlib:lintDebug :mya:lint :MyAccountUApp:lint :mya-catk:lint :mya-csw:lint :mya-chi:lint :mya-mch:lint :dataServices:lintRelease :devicepairingUApp:lint :ews-android:lint :ewsUApp:lint :pushnotification:lintRelease :themesettings:lintRelease
        #prx:lint and rap:lintRelease are not working and we are keeping it as known issues
    '''
}

def DeployingLeakCanaryArtifacts() {
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
            ARTIFACTORY_REPO="platform-pkgs-opa-android-release"
        elif [ '''+ReleaseBranch+''' = true ]
        then
            PUBLISH_APK=true
            ARTIFACTORY_REPO="platform-pkgs-opa-android-stage"
        elif [ '''+DevelopBranch+''' = true ]
        then
            PUBLISH_APK=true
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

def PublishUnitTestsresults() {
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
    junit allowEmptyResults: true,  testResults: 'Source/mya/Source/Library/*/build/test-results/**/*.xml'
    junit allowEmptyResults: false, testResults: 'Source/dsc/Source/Library/*/build/test-results/**/*.xml'
    junit allowEmptyResults: true,  testResults: 'Source/dpr/Source/DemoApp/*/build/test-results/*/*.xml'
    junit allowEmptyResults: true,  testResults: 'Source/dpr/Source/DemoUApp/*/build/test-results/*/*.xml'
    step([$class: 'JUnitResultArchiver', testResults: 'Source/ews/Source/Library/ews-android/build/test-results/*/*.xml'])
    junit allowEmptyResults: false, testResults: 'Source/rap/Source/AppFramework/*/build/test-results/*/*.xml' 

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
    publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'Source/prg/Source/Library/product-registration-lib/build/reports/tests/testDebugUnitTest', reportFiles: 'index.html', reportName: 'prg unit test debug']) 
    publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'Source/prg/Source/Library/product-registration-lib/build/reports/tests/testReleaseUnitTest', reportFiles: 'index.html', reportName: 'prg unit test release'])
    publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'Source/iap/Source/Library/iap/build/reports/tests/testDebugUnitTest', reportFiles: 'index.html', reportName: 'iap unit test debug'])
    publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'Source/iap/Source/Library/iap/build/reports/tests/testReleaseUnitTest', reportFiles: 'index.html', reportName: 'iap unit test release'])
    publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'Source/dcc/Source/Library/digitalCare/build/reports/tests/testReleaseUnitTest', reportFiles: 'index.html', reportName: 'dcc unit test release'])

    def cucumber_path = 'Source/cml/Source/Library/build/cucumber-reports'
    def cucumber_filename = 'Source/cml/cucumber-report-android-commlib.json'
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
    publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'Source/mya/Source/Library/mya-catk/build/reports/tests/testReleaseUnitTest', reportFiles: 'index.html', reportName: 'mya-catk'])
    publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'Source/mya/Source/Library/mya-csw/build/reports/tests/testReleaseUnitTest', reportFiles: 'index.html', reportName: 'mya-csw'])
    publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'Source/mya/Source/Library/mya/build/reports/tests/testReleaseUnitTest', reportFiles: 'index.html', reportName: 'mya-mya'])
    publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'Source/mya/Source/Library/mya-mch/build/reports/tests/testReleaseUnitTest', reportFiles: 'index.html', reportName: 'mya-mch'])
    publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'Source/mya/Source/Library/mya-chi/build/reports/tests/testReleaseUnitTest', reportFiles: 'index.html', reportName: 'mya-chi'])
    publishHTML([allowMissing: true,  alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'Source/mya/Source/DemoApp/app/build/outputs/dexcount/deviceDebugChart', reportFiles: 'index.html', reportName: 'mya DexCount'])
    publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'Source/dsc/Source/Library/dataServices/build/reports/tests/testReleaseUnitTest', reportFiles: 'index.html', reportName: 'dsc unit test release'])
    publishHTML([allowMissing: true,  alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'Source/dpr/Source/DemoApp/app/build/reports/tests/testDebugUnitTest', reportFiles: 'index.html', reportName: 'dpr unit test debug'])
    publishHTML([allowMissing: true,  alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'Source/dpr/Source/DemoApp/app/build/reports/tests/testReleaseUnitTest', reportFiles: 'index.html', reportName: 'dpr unit test release'])
    publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'Source/rap/Source/AppFramework/appFramework/build/reports/tests/testAppFrameworkHamburgerReleaseUnitTest', reportFiles: 'index.html', reportName: 'rap AppFramework Hamburger Release UnitTest'])  
}

def PublishLintJacocoresults() {
    step([$class: 'JacocoPublisher', execPattern: 'Source/bll/**/*.exec', classPattern: 'Source/bll/**/classes', sourcePattern: 'Source/bll/**/src/main/java', exclusionPattern: 'Source/bll/**/R.class,Source/bll/**/R$*.class,Source/bll/**/BuildConfig.class,Source/bll/**/Manifest*.*,Source/bll/**/*Activity*.*,Source/bll/**/*Fragment*.*'])
    step([$class: 'JacocoPublisher', execPattern: 'Source/ews/**/*.exec', sourcePattern: 'Source/ews/**/src/main/java', exclusionPattern: 'Source/ews/**/R.class, Source/ews/**/R$*.class, Source/ews/*/BuildConfig.class, Source/ews/**/Manifest*.*, Source/ews/**/*_Factory.class, Source/ews/**/*_*Factory.class , Source/ews/**/Dagger*.class, Source/ews/**/databinding/**/*.class, Source/ews/**/*Activity*.*, Source/ews/**/*Fragment*.*, Source/ews/**/*Service*.*, Source/ews/**/*ContentProvider*.*'])
    step([$class: 'JacocoPublisher', execPattern: 'Source/cml/**/*.exec', classPattern: 'Source/cml/**/classes', sourcePattern: 'Source/cml/**/src/main/java', exclusionPattern: 'Source/cml/**/R.class,Source/cml/**/R$*.class,Source/cml/**/BuildConfig.class,Source/cml/**/Manifest*.*,Source/cml/**/*Activity*.*,Source/cml/**/*Fragment*.*,Source/cml/**/*Test*.*'])
    androidLint canComputeNew: false, canRunOnFailed: true, defaultEncoding: '', healthy: '', pattern: '', shouldDetectModules: true, unHealthy: '', unstableTotalHigh: ''
}