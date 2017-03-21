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
if %error% equ 0 goto no_errors
echo Gradle error code: %error%
exit /b %error%
:no_errors