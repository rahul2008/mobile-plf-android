cd Source/CatalogApp
subst Z: .
Z:
dir
call gradlew clean
call gradlew assembleDebug
subst Z: /d