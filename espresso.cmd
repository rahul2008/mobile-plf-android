subst Z: .
Z:
dir
cd Source/CatalogApp
dir
call gradlew clean
call gradlew assembleDebug
subst Z: /d