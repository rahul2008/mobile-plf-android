@echo off
subst Z: .
Z:
cd Source/CatalogApp
echo Calling Gradle clean...
call gradlew clean
echo Calling Gradle createDebugCoverageReport///
call gradlew createDebugCoverageReport
subst Z: /d