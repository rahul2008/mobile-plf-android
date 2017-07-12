def trigger(String triggerBy, String branch, String project, String project_tla) {
    if (triggerBy != "ppc" && (branch == "develop" || branch =~ "release" || branch =~ "master")) {
        def platform = "android"
        if (branch =~ "/") {
            branch = branch.replaceAll('/', '%2F')
            echo "BranchName changed to ${branch}"
        }
        stage('Trigger Pipeline') {
            build job: "Platform-Infrastructure/ppc/ppc_${platform}/${branch}", wait: false, parameters: [[$class: 'StringParameterValue', name: 'componentName', value: project_tla], [$class: 'StringParameterValue', name: 'libraryName', value: project]]
        }
    }
}

return this
