@echo off
exec tskill "java"
exec tskill "adb"
exec tskill "aapt"

subst Z: .
Z:
cd Source/CatalogApp
echo Calling Gradle clean...
call gradlew clean
echo Calling Gradle uid:createDebugCoverageReport///
call gradlew uid:createDebugCoverageReport
subst Z: /d