#!/bin/sh
rm -rf ../../../mya-other-localization
git clone --quiet "ssh://tfsemea1.ta.philips.com:22/tfs/TPC_Region24/CDP2/_git/mya-other-localization" ../../../mya-other-localization > /dev/null
cd ../../../mya-other-localization/ConsentLocalization/Android
echo '\n*** Start generating localization files *** \n'
chmod 755 ./010_parse_csv_to_strings.rb
./010_parse_csv_to_strings.rb
echo '\n*** Start moving localization files to desired location ***\n'
chmod 755 ./020_copy_output_to_project.rb
./020_copy_output_to_project.rb
echo '\n*** Completed moving localization files to desired location ***\n'
echo '\n*** Completed generating localization files ***\n'