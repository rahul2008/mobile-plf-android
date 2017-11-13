#!/bin/bash

adb=$1/platform-tools/adb
package_name=$2
apk_name=$3

devices=$($adb devices | grep -v 'List of devices' | cut -f1 | grep -c '.')

if (($devices > 0))
then
    echo "Setting permissions"

	package=$($adb shell pm list packages | grep "package\:$package_name" | grep -vc "package\:$package_name\.")

	if (($package > 0))
	then
	   echo "Package already installed";
	else
		echo "Installing app"
		$adb install build/outputs/apk/$apk_name.apk
		echo "App installed successful"
	fi

	echo "Setting permissions"
	$adb shell pm grant $package_name android.permission.ACCESS_COARSE_LOCATION
	$adb shell pm grant $package_name android.permission.WRITE_EXTERNAL_STORAGE
	echo "Permissions set successful"
fi