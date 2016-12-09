#!/bin/bash

COMPONENT_NAME="registrationApi"
echo "COMPONENT_NAME: ${COMPONENT_NAME}"
if [ "$COMPONENT_NAME" == "" ] ; then
    exit 0
fi
CURRENT_TAG=`git tag -l --points-at HEAD`
echo "Current tag: ${CURRENT_TAG}"
if [ "$CURRENT_TAG" == "" ] ; then
    exit 0
fi
OUTPUT=`curl -u admin:password -X GET http://maartens-mini.ddns.htc.nl.philips.com:8081/artifactory/api/search/artifact?name=${COMPONENT_NAME}*${CURRENT_TAG}&repos=libs-stage-local-android`
echo $OUTPUT | grep -q ".pom"
if [ $? -eq 0 ]; then
    echo "Removing the already uploaded version.(Needs improvement. This is a dirty hack."
    curl -uadmin:password -XDELETE http://maartens-mini.ddns.htc.nl.philips.com:8081/artifactory/libs-stage-local-android/com/philips/cdp/${COMPONENT_NAME}/${CURRENT_TAG}
fi

COMPONENT_NAME="RegistrationCoppaSampleApp"
echo "COMPONENT_NAME: ${COMPONENT_NAME}"
if [ "$COMPONENT_NAME" == "" ] ; then
    exit 0
fi
CURRENT_TAG=`git tag -l --points-at HEAD`
echo "Current tag: ${CURRENT_TAG}"
if [ "$CURRENT_TAG" == "" ] ; then
    exit 0
fi
OUTPUT=`curl -u admin:password -X GET http://maartens-mini.ddns.htc.nl.philips.com:8081/artifactory/api/search/artifact?name=${COMPONENT_NAME}*${CURRENT_TAG}&repos=libs-stage-local-android`
echo $OUTPUT | grep -q ".pom"
if [ $? -eq 0 ]; then
    echo "Removing the already uploaded version.(Needs improvement. This is a dirty hack."
    curl -uadmin:password -XDELETE http://maartens-mini.ddns.htc.nl.philips.com:8081/artifactory/libs-stage-local-android/com/philips/cdp/${COMPONENT_NAME}/${CURRENT_TAG}
fi

COMPONENT_NAME="hsdp"
echo "COMPONENT_NAME: ${COMPONENT_NAME}"
if [ "$COMPONENT_NAME" == "" ] ; then
    exit 0
fi
CURRENT_TAG=`git tag -l --points-at HEAD`
echo "Current tag: ${CURRENT_TAG}"
if [ "$CURRENT_TAG" == "" ] ; then
    exit 0
fi
OUTPUT=`curl -u admin:password -X GET http://maartens-mini.ddns.htc.nl.philips.com:8081/artifactory/api/search/artifact?name=${COMPONENT_NAME}*${CURRENT_TAG}&repos=libs-stage-local-android`
echo $OUTPUT | grep -q ".pom"
if [ $? -eq 0 ]; then
    echo "Removing the already uploaded version.(Needs improvement. This is a dirty hack."
    curl -uadmin:password -XDELETE http://maartens-mini.ddns.htc.nl.philips.com:8081/artifactory/libs-stage-local-android/com/philips/cdp/${COMPONENT_NAME}/${CURRENT_TAG}
fi

COMPONENT_NAME="jump"
echo "COMPONENT_NAME: ${COMPONENT_NAME}"
if [ "$COMPONENT_NAME" == "" ] ; then
    exit 0
fi
CURRENT_TAG=`git tag -l --points-at HEAD`
echo "Current tag: ${CURRENT_TAG}"
if [ "$CURRENT_TAG" == "" ] ; then
    exit 0
fi
OUTPUT=`curl -u admin:password -X GET http://maartens-mini.ddns.htc.nl.philips.com:8081/artifactory/api/search/artifact?name=${COMPONENT_NAME}*${CURRENT_TAG}&repos=libs-stage-local-android`
echo $OUTPUT | grep -q ".pom"
if [ $? -eq 0 ]; then
    echo "Removing the already uploaded version.(Needs improvement. This is a dirty hack."
    curl -uadmin:password -XDELETE http://maartens-mini.ddns.htc.nl.philips.com:8081/artifactory/libs-stage-local-android/com/philips/cdp/${COMPONENT_NAME}/${CURRENT_TAG}
fi

COMPONENT_NAME="coppa"
echo "COMPONENT_NAME: ${COMPONENT_NAME}"
if [ "$COMPONENT_NAME" == "" ] ; then
    exit 0
fi
CURRENT_TAG=`git tag -l --points-at HEAD`
echo "Current tag: ${CURRENT_TAG}"
if [ "$CURRENT_TAG" == "" ] ; then
    exit 0
fi
OUTPUT=`curl -u admin:password -X GET http://maartens-mini.ddns.htc.nl.philips.com:8081/artifactory/api/search/artifact?name=${COMPONENT_NAME}*${CURRENT_TAG}&repos=libs-stage-local-android`
echo $OUTPUT | grep -q ".pom"
if [ $? -eq 0 ]; then
    echo "Removing the already uploaded version.(Needs improvement. This is a dirty hack."
    curl -uadmin:password -XDELETE http://maartens-mini.ddns.htc.nl.philips.com:8081/artifactory/libs-stage-local-android/com/philips/cdp/${COMPONENT_NAME}/${CURRENT_TAG}
fi