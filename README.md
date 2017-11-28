#Introduction 
MYA - My Account Android

#Update Localized Resources
1. Open terminal at this location: mya-android-my-account/Source/Library
2. ./ConsentWidgets/generateLocalization.sh 

or

2. ./gradlew ConsentWidgets:updateResources

This will checkout mya-other-localization repo, run the android export script, and copy
all generated translations to ConsentWidgets/src/main/res folder.

NOTE: We did not add this task to Jenkins configuration, because it would mean that
local and remote can get out-of-sync. E.g. when add