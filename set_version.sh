#!/bin/bash

VERSION_NUMBER="$1"

echo "Script executed from: ${PWD}"

VERSIONS_FILE='./Source/AppFramework/scripts/versions.gradle'
OLD_VERSION_NAME='versionName *: * "[^"]*"'
NEW_VERSION_NAME='versionName    : "'$VERSION_NUMBER'"'
if [ -f "$VERSIONS_FILE" ]
then
	echo "File $VERSIONS_FILE found."
else
	echo "File $VERSIONS_FILE not found."
fi

printf "$NEW_VERSION_NAME\n"

echo sed -i "s/""$OLD_VERSION_NAME""/""$NEW_VERSION_NAME""/" $VERSIONS_FILE