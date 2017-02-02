@echo off
subst Z: .
Z:
cd Source/CatalogApp
echo Calling Gradle clean...
call gradle clean
echo Calling Gradle uid:createDebugCoverageReport///
call gradle uid:createDebugCoverageReport
subst Z: /d