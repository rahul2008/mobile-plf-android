// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        jcenter()
		
<#if mavenUrl != "mavenCentral">
        maven {
            url '${mavenUrl}'
        }
</#if>
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:${gradlePluginVersion}'

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}



task clean(type: Delete) {
    delete rootProject.buildDir
allprojects {
    repositories {
        jcenter()
		  maven { url 'http://maartens-mini.ddns.htc.nl.philips.com:8081/artifactory/libs-release-local-android' }
        maven { url 'http://maartens-mini.ddns.htc.nl.philips.com:8081/artifactory/jcenter' }
        maven { url 'http://maartens-mini.ddns.htc.nl.philips.com:8081/artifactory/ext-release-local'}
        maven { url 'http://maartens-mini.ddns.htc.nl.philips.com:8081/artifactory/libs-release-local-android' }
        maven { url 'http://maartens-mini.ddns.htc.nl.philips.com:8081/artifactory/libs-stage-local-android'}
        maven {
            url 'http://maartens-mini.ddns.htc.nl.philips.com:8081/artifactory/libs-snapshot-local-android'
        }
        
<#if mavenUrl != "mavenCentral">
        maven {
            url '${mavenUrl}'
        }
</#if>
    }
}

}