@echo off
subst Z: .
Z:
cd Source/CatalogApp
echo Calling Gradle uid:createDebugCoverageReport
call gradlew uid:createDebugCoverageReport
echo Killing ADB
adb kill-server
subst Z: /d