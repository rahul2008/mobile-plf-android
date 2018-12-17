allprojects {
    repositories {


        maven { url 'http://maartens-mini.ddns.htc.nl.philips.com:8081/artifactory/libs-release-local-android' }
        maven { url 'http://maartens-mini.ddns.htc.nl.philips.com:8081/artifactory/jcenter' }
        maven { url 'http://maartens-mini.ddns.htc.nl.philips.com:8081/artifactory/ext-release-local'}
        maven { url 'http://maartens-mini.ddns.htc.nl.philips.com:8081/artifactory/libs-release-local-android' }
        maven { url 'http://maartens-mini.ddns.htc.nl.philips.com:8081/artifactory/libs-stage-local-android'}
        maven {
            url 'http://maartens-mini.ddns.htc.nl.philips.com:8081/artifactory/libs-snapshot-local-android'
        }
        maven { url "https://oss.sonatype.org/content/repositories/snapshots" }
    }
}