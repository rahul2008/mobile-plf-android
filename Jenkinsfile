#!/usr/bin/env groovy																											

BranchName = env.BRANCH_NAME

properties([
    [$class: 'ParametersDefinitionProperty', parameterDefinitions: [[$class: 'StringParameterDefinition', defaultValue: '', description: 'triggerBy', name : 'triggerBy']]],
    [$class: 'BuildDiscarderProperty', strategy: [$class: 'LogRotator', numToKeepStr: '50']]
])

def MailRecipient = 'pascal.van.kempen@philips.com,ambati.muralikrishna@philips.com,ramesh.r.m@philips.com'

node_ext = “build_t”
if (env.triggerBy == “ppc”) {
  node_ext = “build_p”
}

node ('androidppc '&& node_ext) {
	timestamps {
		stage ('Checkout') {
			checkout([$class: 'GitSCM', branches: [[name: '*/'+BranchName]], doGenerateSubmoduleConfigurations: false, extensions: [[$class: 'WipeWorkspace'], [$class: 'PruneStaleBranch'], [$class: 'LocalBranch']], submoduleCfg: [], userRemoteConfigs: [[credentialsId: '4edede71-63a0-455e-a9dd-d250f8955958', url: 'ssh://git@atlas.natlab.research.philips.com:7999/maf/app-framework_android.git']]])
			step([$class: 'StashNotifier'])
		}

		try {
			stage ('build') {
				sh './check_and_delete_artifact.sh'
                sh 'cd ./Source/AppFramework && ./gradlew clean assembleDebug cC assembleRelease zipDoc tgzTask appFramework:aP'
				currentBuild.result = 'SUCCESS'												
			}
        }

        catch(err) {
            currentBuild.result = 'FAILURE'
            error ("Someone just broke the build")
        }   
        
        try {			
            if (env.triggerBy != "ppc" && !(BranchName =~ "eature")) {
            	stage ('callIntegrationPipeline') {
                    if (BranchName =~ "/") {
                        BranchName = BranchName.replaceAll('/','%2F')
                        echo "BranchName changed to ${BranchName}"
                    }
            		build job: "Platform-Infrastructure/ppc/ppc_android/${BranchName}", parameters: [[$class: 'StringParameterValue', name: 'componentName', value: 'afw'],[$class: 'StringParameterValue', name: 'libraryName', value: '']]
                    currentBuild.result = 'SUCCESS'
            	}            
            }
            
		} //end try
		
		catch(err) {
            currentBuild.result = 'UNSTABLE'
        }

        stage('informing') {
        	step([$class: 'StashNotifier'])
        	step([$class: 'Mailer', notifyEveryUnstableBuild: true, recipients: MailRecipient, sendToIndividuals: true])
        }

	} // end timestamps
} // end node ('android')