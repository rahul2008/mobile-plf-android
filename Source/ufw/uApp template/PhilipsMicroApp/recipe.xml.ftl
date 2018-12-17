<?xml version="1.0"?>
<recipe>
    <#include "../common/recipe_manifest.xml.ftl" />

<#if useFragment>
    <#include "recipe_fragment.xml.ftl" />
<#else>
    <#include "../common/recipe_simple.xml.ftl" />
</#if>

<#if hasAppBar>
    <#include "../common/recipe_app_bar.xml.ftl" />
</#if>


	
		
  <instantiate from="root/src/app_package/SimpleActivity.java.ftl"
                   to="${escapeXmlAttribute(srcOut)}/${activityClass}.java" />

  <instantiate from="root/res/layout/helloworld.xml.ftl"
                   to="${escapeXmlAttribute(resOut)}/layout/${layoutName}.xml" />
				   
	<instantiate from="root/src/app_package/Interface.java.ftl"
                   to="${escapeXmlString(appTitle)}/src/main/java/${packageName}/${escapeXmlString(appTitle)}uAppInterface.java" />
	<instantiate from="root/src/app_package/Dependencies.java.ftl"
                   to="${escapeXmlString(appTitle)}/src/main/java/${packageName}/${escapeXmlString(appTitle)}uAppDependencies.java" />
	<instantiate from="root/src/app_package/Settings.java.ftl"
                   to="${escapeXmlString(appTitle)}/src/main/java/${packageName}/${escapeXmlString(appTitle)}uAppSettings.java" />
	<instantiate from="root/src/app_package/LaunchInput.java.ftl"
                   to="${escapeXmlString(appTitle)}/src/main/java/${packageName}/${escapeXmlString(appTitle)}uAppLaunchInput.java" />

  <instantiate from="root/src/app_package/DemoActivity.java.ftl"
                   to="${escapeXmlString(appTitle)}/src/main/java/${packageName}/${escapeXmlString(appTitle)}Activity.java" />

  <instantiate from="root/AndroidManifest.xml.ftl"
                   to="${escapeXmlAttribute(appTitle)}/src/main/AndroidManifest.xml" />

  <instantiate from="root/res/layout/sample_app.xml.ftl"
                   to="${escapeXmlAttribute(appTitle)}/src/main/res/layout/demoactivity.xml" />

	<merge from="root/build.gradle.ftl"
            to="${escapeXmlString(appTitle)}/build.gradle" />
  <merge from="root/settings.gradle.ftl"
         to="settings.gradle" />
	<merge from="root/app_build.gradle.ftl"
         to="app/build.gradle" />		
  <merge from="root/root_build.gradle.ftl"
         to="build.gradle" />      
    <open file="${escapeXmlAttribute(srcOut)}/${activityClass}.java" />
	
<#if useFragment>
    <open file="${escapeXmlAttribute(resOut)}/layout/${fragmentLayoutName}.xml" />
<#else>
    <open file="${escapeXmlAttribute(resOut)}/layout/${simpleLayoutName}.xml" />
</#if>

</recipe>
