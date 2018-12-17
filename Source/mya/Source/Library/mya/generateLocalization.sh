#!/bin/sh

echo '*** Checking out Localization repo ***'
cd "$(dirname "$0")"

rm -rf ../../../../mya-other-localization
git clone --quiet "ssh://tfsemea1.ta.philips.com:22/tfs/TPC_Region24/CDP2/_git/mya-other-localization" ../../../../mya-other-localization > /dev/null
cd ../../../../mya-other-localization/Localization/Android
echo '*** Start generating localization files ***'
chmod 755 ./android_parse_csv_to_strings.rb
./android_parse_csv_to_strings.rb
echo '*** Start moving localization files to desired location ***'
chmod 755 ./020_copy_output_to_project.rb
./020_copy_output_to_project.rb
echo '*** Completed moving localization files to desired location ***'
echo '*** Completed generating localization files ***'