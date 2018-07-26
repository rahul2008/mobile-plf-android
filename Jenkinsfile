#!/usr/bin/env groovy
// please look at: https://jenkins.io/doc/book/pipeline/syntax/
BranchName = env.BRANCH_NAME
String param_string_cron = BranchName == "develop" ? "H H(20-21) * * * %buildType=PSRA \nH H(21-22) * * * %buildType=TICS" : ""

def MailRecipient = 'DL_CDP2_Callisto@philips.com'
def nodes = '27.0.2 && device'
if (BranchName == "develop") {
    nodes = nodes + " && TICS"
}

pipeline {
    agent {
        node {
            label nodes
        }
    }
    parameters {
        choice(choices: 'Normal\nPSRA\nLeakCanary\nTICS', description: 'What type of build to build?', name: 'buildType')
    }
    triggers {
        parameterizedCron(param_string_cron)
    }
    environment {
        EPOCH_TIME = sh(script: 'date +%s', returnStdout: true).trim()
    }
    options {
        timestamps()
        buildDiscarder(logRotator(numToKeepStr: '24'))
    }
    stages {
        stage('Build+test') {
            steps {
                echo "Node labels: ${nodes}"
                sh 'printenv'

                timeout(time: 1, unit: 'HOURS') {
                    InitialiseBuild()
                    BuildAndUnitTest()
                }
            }
        }

        stage('Publish tests') {
            steps {
                PublishUnitTestsresults()
            }
        }

        stage('Lint+Jacoco') {
            when {
                expression { return params.buildType == 'TICS' }
            }
            steps {
                BuildLint()
                PublishLintJacocoresults()
            }
        }

        stage('PSRAbuild') {
            when {
                allOf {
                    expression { return params.buildType == 'PSRA' }
                }
            }
            steps {
                    echo 'referenceApp:assemblePsraRelease'
            }
        }

        stage('LeakCanarybuild') {
            when {
                allOf {
                    expression { return params.buildType == 'LeakCanary' }
                    anyOf { branch 'master'; branch 'develop'; branch 'release/platform_*' }
                }
            }
            steps {
                    echo 'referenceApp:assembleLeakCanary'
            }
        }

        stage('Publish to artifactory') {
            when {
                anyOf { branch 'master'; branch 'develop*'; branch 'release/platform_*' }
            }
            steps {
                sh '''#!/bin/bash -l
                    set -e
                    ./gradlew saveResDep saveAllResolvedDependenciesGradleFormat zipDocuments artifactoryPublish :referenceApp:printArtifactoryApkPath :AppInfra:zipcClogs :securedblibrary:zipcClogs :registrationApi:zipcClogs :jump:zipcClogs :hsdp:zipcClogs :productselection:zipcClogs :digitalCareUApp:zipcClogs :digitalCare:zipcClogs :mya:zipcClogs
                '''
                archiveArtifacts 'Source/rap/Source/AppFramework/appFramework/*dependencies*.lock'
                DeployingConnectedTestsLogs()
            }
        }

        stage('Publish PSRA apk') {
            when {
                allOf {
                    expression { return params.buildType == 'PSRA' }
                }
            }
            steps {
                script {
                    APK_NAME = readFile("apkname.txt").trim()
                    echo "APK_NAME = ${APK_NAME}"
                    APK_INFO = APK_NAME.split("referenceApp-")
                    APK_PATH = APK_INFO[0]
                    PSRA_APK_NAME = APK_INFO[0] + "referenceApp-" + APK_INFO[1].replace(".apk", "_PSRA.apk")
                    echo "PSRA_APK_NAME: ${PSRA_APK_NAME}"
                    sh """#!/bin/bash -le
                        curl -L -u readerwriter:APBcfHoo7JSz282DWUzMVJfUsah -X PUT $PSRA_APK_NAME -T Source/rap/Source/AppFramework/appFramework/build/outputs/apk/psraRelease/referenceApp-psraRelease.apk
                    """
                }
            }
        }

        stage('TICS') {
           when {
               expression { return params.buildType == 'TICS' }
          }
            steps {
                script {
                    echo "Running TICS..."
                    sh """#!/bin/bash -le 
                        ./gradlew clean jacocoTestReport
                        /mnt/tics/Wrapper/TICSMaintenance -project OPA-Android -branchname develop -branchdir .
                        /mnt/tics/Wrapper/TICSQServer -project OPA-Android -nosanity
                    """
                }
            }
        }

        stage('Trigger E2E Test') {
            when {
                allOf {
                    not { expression { return params.buildType == 'LeakCanary' } }
                    anyOf { branch 'master'; branch 'develop'; branch 'release/platform_*' }
                }
            }
            steps {
                script {
                    APK_NAME = readFile("apkname.txt").trim()
                    echo "APK_NAME = ${APK_NAME}"

                    def jobBranchName = "release_platform_1802.0.0"
                    if (BranchName =~ /develop.*/) {
                       jobBranchName = "develop"
                    }
                    echo "BranchName changed to ${jobBranchName}"

                    sh """#!/bin/bash -le
                        curl -X POST http://310256016:61a84d6f3e9343128dff5736ef68259e@cdp2-jenkins.htce.nl.philips.com:8080/job/Platform-Infrastructure/job/E2E_Tests/job/E2E_Android_${jobBranchName}/buildWithParameters?APKPATH=$APK_NAME
                    """
                }
            }
        }

        stage('LeakCanary E2E Test') {
            when {
                allOf {
                    expression { return params.buildType == 'LeakCanary' }
                    anyOf { branch 'master'; branch 'develop'; branch 'release/platform_*' }
                }
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
            deleteDir()
            step([$class: 'Mailer', notifyEveryUnstableBuild: true, recipients: MailRecipient, sendToIndividuals: true])
        }
    }
}

def InitialiseBuild() {
    committerName = sh (script: "git show -s --format='%an' HEAD", returnStdout: true).trim()
    currentBuild.description = "Submitter: " + committerName + ";Node: ${env.NODE_NAME}"
    echo currentBuild.description
}

def BuildAndUnitTest() {
    echo '9999IntegrationTests: Starting'
    sh '''#!/bin/bash -l
        set -e
        chmod -R 755 .
        ./gradlew --refresh-dependencies --full-stacktrace assembleRelease \
            :commlib-integration-tests:testReleaseUnitTest \
    '''
    echo '9999IntegrationTests: Before Archiving'
    archiveArtifacts 'Source/rap/Source/AppFramework/appFramework/build/outputs/apk/release/*.apk'
    echo '9999IntegrationTests: Ending'
}

def BuildLint() {
    sh '''#!/bin/bash -l
        set -e
        #do not use -PenvCode=${JENKINS_ENV} since the option 'opa' is hardcoded in the archive
        ./gradlew :IconFont:lint \
         :AppInfra:lint \
         :uikitLib:lint \
         :securedblibrary:lint \
         :registrationApi:lint \
         :productselection:lint \
         :telehealth:lintRelease \
         :bluelib:lintDebug \
         :product-registration-lib:lint \
         :iap:lint \
         :digitalCare:lint \
         :cloudcontroller-api:lintDebug \
         :commlib:lintDebug \
         :mya:lint \
         :catk:lint \
         :csw:lint \
         :pif:lint \
         :dataServices:lintRelease \
         :devicepairingUApp:lint \
         :ews-android:lint \
         :ewsUApp:lint \
         :pushnotification:lintRelease \
         :themesettings:lintRelease
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

def DeployingConnectedTestsLogs() {
    boolean MasterBranch = (BranchName ==~ /master.*/)
    boolean ReleaseBranch = (BranchName ==~ /release\/platform_.*/)
    boolean DevelopBranch = (BranchName ==~ /develop.*/)

    def shellcommand = '''#!/bin/bash -l
        export BASE_PATH=`pwd`
        echo $BASE_PATH

        cd $BASE_PATH

        ARTIFACTORY_URL="http://artifactory-ehv.ta.philips.com:8082/artifactory"
        ARTIFACTORY_REPO="unknown"

        if [ '''+MasterBranch+''' = true ]
        then
            ARTIFACTORY_REPO="platform-logs-release-local"
        elif [ '''+ReleaseBranch+''' = true ]
        then
            ARTIFACTORY_REPO="platform-logs-release-local"
        elif [ '''+DevelopBranch+''' = true ]
        then
            ARTIFACTORY_REPO="platform-logs-snapshot-local"
        else
            echo "Not published as build is not on a master, develop or release branch" . $BranchName
        fi

        find . -name *logs.zip | while read LOGS;
        do
            curl -L -u readerwriter:APBcfHoo7JSz282DWUzMVJfUsah -X PUT $ARTIFACTORY_URL/$ARTIFACTORY_REPO/logs/ -T $LOGS
        done

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

    def cucumber_path = 'Source/cml/Source/Library/build/cucumber-reports'
    def cucumber_filename = 'Source/cml/cucumber-report-android-commlib.json'

    if (fileExists("$cucumber_path/report.json")) {
        step([$class: 'CucumberReportPublisher', jsonReportDirectory: cucumber_path, fileIncludePattern: '*.json'])
    } else {
        echo 'No Cucumber result found, nothing to publish'
    }
}

def PublishLintJacocoresults() {
    step([$class: 'JacocoPublisher', execPattern: 'Source/bll/**/*.exec', classPattern: 'Source/bll/**/classes', sourcePattern: 'Source/bll/**/src/main/java', exclusionPattern: 'Source/bll/**/R.class,Source/bll/**/R$*.class,Source/bll/**/BuildConfig.class,Source/bll/**/Manifest*.*,Source/bll/**/*Activity*.*,Source/bll/**/*Fragment*.*'])
    step([$class: 'JacocoPublisher', execPattern: 'Source/ews/**/*.exec', sourcePattern: 'Source/ews/**/src/main/java', exclusionPattern: 'Source/ews/**/R.class, Source/ews/**/R$*.class, Source/ews/*/BuildConfig.class, Source/ews/**/Manifest*.*, Source/ews/**/*_Factory.class, Source/ews/**/*_*Factory.class , Source/ews/**/Dagger*.class, Source/ews/**/databinding/**/*.class, Source/ews/**/*Activity*.*, Source/ews/**/*Fragment*.*, Source/ews/**/*Service*.*, Source/ews/**/*ContentProvider*.*'])
    step([$class: 'JacocoPublisher', execPattern: 'Source/cml/**/*.exec', classPattern: 'Source/cml/**/classes', sourcePattern: 'Source/cml/**/src/main/java', exclusionPattern: 'Source/cml/**/R.class,Source/cml/**/R$*.class,Source/cml/**/BuildConfig.class,Source/cml/**/Manifest*.*,Source/cml/**/*Activity*.*,Source/cml/**/*Fragment*.*,Source/cml/**/*Test*.*'])
    androidLint canComputeNew: false, canRunOnFailed: true, defaultEncoding: '', healthy: '', pattern: '', shouldDetectModules: true, unHealthy: '', unstableTotalHigh: ''
}
