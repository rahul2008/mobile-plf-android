#!/bin/bash

VERSION_NUMBER="$1"
VERSION_CODE="$2"

VERSIONS_FILE='./Source/CatalogApp/app/build.gradle'
TMP_VERSIONS_FILE=temp.versions.gradle

NEW_VERSION_NAME='versionName "'$VERSION_NUMBER'"'
NEW_VERSION_CODE='versionCode '$VERSION_CODE''

sed "s/versionName.*/""$NEW_VERSION_NAME""/" $VERSIONS_FILE > $TMP_VERSIONS_FILE
sed "s/versionCode.*/""$NEW_VERSION_CODE""/" $TMP_VERSIONS_FILE > $VERSIONS_FILE

cat $VERSIONS_FILE | grep version | grep -v ext

rm $TMP_VERSIONS_FILE