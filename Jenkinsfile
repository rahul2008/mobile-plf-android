node('Android') {
    stage 'Checkout'
    sh 'git clean -ffdx'
    checkout scm

    stage 'Build'
    sh 'cd ./Source/ShineLib && ./gradlew assembleDebug'

    stage 'Unit test'
    sh 'rm -rf ./Source/ShineLib/shinelib/build/test-results/debug ./Source/ShineLib/pluginreferenceboard/build/test-results/debug'
    sh 'cd ./Source/ShineLib && ./gradlew testDebugUnitTest || true'
    step([$class: 'JUnitResultArchiver', testResults: 'Source/ShineLib/*/build/test-results/debug/*.xml'])

    stage 'Lint'
    sh 'cd ./Source/ShineLib && ./gradlew lintDebug || true'
    step([$class: 'LintPublisher', healthy: '0', unHealthy: '20', unstableTotalAll: '20'])
}