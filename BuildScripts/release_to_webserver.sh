#!/bin/sh

echo Running script: `basename "$0"`

SSHSERVER="philipsuikit@sesvm-webserver.htce.nl.philips.com"
SERVERPATH=www/android
WWW="${SSHSERVER}:${SERVERPATH}"
BUILDSCRIPTDIR=`dirname "$0"`
WORKSPACE="${BUILDSCRIPTDIR}/../"

#todo: take from git branch/tag name instead
VERSION_NUMBER="`cat ${WORKSPACE}/VERSION`"
STAGINGDIR="${WORKSPACE}/Staging"
RELEASEDIR="${STAGINGDIR}/${VERSION_NUMBER}"
ARCHIVEDIR="${STAGINGDIR}/Archive"

#create release and archive dirs
mkdir -p "${RELEASEDIR}" || fatal "Release dir already exists"
mkdir -p "${ARCHIVEDIR}" || fatal "Release dir already exists"

#copy release notes
cp "${WORKSPACE}/Documentation/ReleaseNotes.md" "${RELEASEDIR}" || fatal "Failed to copy release notes to release dir"
cp "${WORKSPACE}/Documentation/ReleaseNotes.md" "${ARCHIVEDIR}" || fatal "Failed to copy release notes to archive dir"

#todo: copy documentation
cp -R "${WORKSPACE}/Documents/External/" "${RELEASEDIR}"
cp -R "${WORKSPACE}/Documents/External/" "${ARCHIVEDIR}"
#todo: copy javadoc

#copy CatalogApp to archive dir
cp -R "${WORKSPACE}/Source/CatalogApp" "${ARCHIVEDIR}" || fatal "Failed to copy CatalogApp to staging area"

#remove stuff that shouldn't be archived
rm -R "${ARCHIVEDIR}/CatalogApp/build"
rm -R "${ARCHIVEDIR}/CatalogApp/app/build"
rm -R "${ARCHIVEDIR}/CatalogApp/.idea"
rm -R "${ARCHIVEDIR}/CatalogApp/.gradle"
rm "${ARCHIVEDIR}/CatalogApp/local.properties"
find . -name .DS_Store | xargs rm

#replace local lib reference with artifactory one
#update CatalogApp/settings.gradle to remove local lib project ref
perl -pi -e "s/include \':uikitLib\'//" "${ARCHIVEDIR}/CatalogApp/settings.gradle"
perl -pi -e "s/project\(\'\:uikitLib\'\)\.projectDir \= new File\(\'\.\.\/UiKit\/uikitLib\'\)//" "${ARCHIVEDIR}/CatalogApp/settings.gradle"

#update CatalogApp/build.gradle to include maven url
perl ${BUILDSCRIPTDIR}/insert_maven_url.pl "${ARCHIVEDIR}/CatalogApp/build.gradle"

#replace compile project(':uikitLib') in app/build.gradle with maven dependency
perl -pi -e "s/compile project\(\':uikitLib\'\)/compile \'com.philips.cdp:uikitLib:${VERSION_NUMBER}\'/" "${ARCHIVEDIR}/CatalogApp/app/build.gradle"

#zip/tar the release artifacts
tar -cvzf "${RELEASEDIR}/PhilipsUIKit-Android-${VERSION_NUMBER}.tar.gz" -C "${ARCHIVEDIR}" . || fatal "Failed to create release archive"

scp -r -q "${RELEASEDIR}" "${WWW}" || fatal "Failed to upload release"
ssh "${SSHSERVER}" rm -f "${SERVERPATH}/latest" || fatal "Failed to remove symlink"
ssh "${SSHSERVER}" "cd ${SERVERPATH}; ln -s ${VERSION_NUMBER} latest" || fatal "Failed to create symlink"

#cleanup our staging directory
rm -Rf "${STAGINGDIR}"
