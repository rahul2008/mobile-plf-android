#!/bin/bash

COMPONENT_NAME="registrationApi"
CURRENT_TAG=`git tag -l --points-at HEAD`
OUTPUT=`curl -u admin:password -X GET http://maartens-mini.ddns.htc.nl.philips.com:8081/artifactory/api/search/artifact?name=${COMPONENT_NAME}*${CURRENT_TAG}&repos=libs-stage-local-android`
echo $OUTPUT | grep -q ".pom"
if [ $? -eq 0 ]; then
    echo "Removing the already uploaded version.(Needs improvement. This is a dirty hack."
    curl -uadmin:password -XDELETE http://maartens-mini.ddns.htc.nl.philips.com:8081/artifactory/libs-stage-local-android/com/philips/cdp/${COMPONENT_NAME}/${CURRENT_TAG}
fi

COMPONENT_NAME="RegistrationCoppaSampleApp"
CURRENT_TAG=`git tag -l --points-at HEAD`
OUTPUT=`curl -u admin:password -X GET http://maartens-mini.ddns.htc.nl.philips.com:8081/artifactory/api/search/artifact?name=${COMPONENT_NAME}*${CURRENT_TAG}&repos=libs-stage-local-android`
echo $OUTPUT | grep -q ".pom"
if [ $? -eq 0 ]; then
    echo "Removing the already uploaded version.(Needs improvement. This is a dirty hack."
    curl -uadmin:password -XDELETE http://maartens-mini.ddns.htc.nl.philips.com:8081/artifactory/libs-stage-local-android/com/philips/cdp/${COMPONENT_NAME}/${CURRENT_TAG}
fi

COMPONENT_NAME="hsdp"
CURRENT_TAG=`git tag -l --points-at HEAD`
OUTPUT=`curl -u admin:password -X GET http://maartens-mini.ddns.htc.nl.philips.com:8081/artifactory/api/search/artifact?name=${COMPONENT_NAME}*${CURRENT_TAG}&repos=libs-stage-local-android`
echo $OUTPUT | grep -q ".pom"
if [ $? -eq 0 ]; then
    echo "Removing the already uploaded version.(Needs improvement. This is a dirty hack."
    curl -uadmin:password -XDELETE http://maartens-mini.ddns.htc.nl.philips.com:8081/artifactory/libs-stage-local-android/com/philips/cdp/${COMPONENT_NAME}/${CURRENT_TAG}
fi

COMPONENT_NAME="jump"
CURRENT_TAG=`git tag -l --points-at HEAD`
OUTPUT=`curl -u admin:password -X GET http://maartens-mini.ddns.htc.nl.philips.com:8081/artifactory/api/search/artifact?name=${COMPONENT_NAME}*${CURRENT_TAG}&repos=libs-stage-local-android`
echo $OUTPUT | grep -q ".pom"
if [ $? -eq 0 ]; then
    echo "Removing the already uploaded version.(Needs improvement. This is a dirty hack."
    curl -uadmin:password -XDELETE http://maartens-mini.ddns.htc.nl.philips.com:8081/artifactory/libs-stage-local-android/com/philips/cdp/${COMPONENT_NAME}/${CURRENT_TAG}
fi

COMPONENT_NAME="coppa"
CURRENT_TAG=`git tag -l --points-at HEAD`
OUTPUT=`curl -u admin:password -X GET http://maartens-mini.ddns.htc.nl.philips.com:8081/artifactory/api/search/artifact?name=${COMPONENT_NAME}*${CURRENT_TAG}&repos=libs-stage-local-android`
echo $OUTPUT | grep -q ".pom"
if [ $? -eq 0 ]; then
    echo "Removing the already uploaded version.(Needs improvement. This is a dirty hack."
    curl -uadmin:password -XDELETE http://maartens-mini.ddns.htc.nl.philips.com:8081/artifactory/libs-stage-local-android/com/philips/cdp/${COMPONENT_NAME}/${CURRENT_TAG}
fi