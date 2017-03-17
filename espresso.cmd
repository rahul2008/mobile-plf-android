@echo off
subst Z: .
Z:
cd Source/CatalogApp
echo Calling Gradle uid:createDebugCoverageReport
call gradlew uid:createDebugCoverageReport
set error=%errorlevel%
echo Killing ADB
adb kill-server
subst Z: /d
if %error% neq 0 exit /b %error%