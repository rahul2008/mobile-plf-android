call tskill  "java"
call tskill  "aapt"
call tskill  "adb"
@echo off
subst Z: .
Z:
cd Source/CatalogApp
echo Calling Gradle clean...
call gradlew clean
echo Calling Gradle uid:createDebugCoverageReport///
call gradlew uid:createDebugCoverageReport
subst Z: /d