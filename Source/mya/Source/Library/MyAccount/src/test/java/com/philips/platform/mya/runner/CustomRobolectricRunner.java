/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.mya.runner;

import org.junit.runners.model.InitializationError;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.manifest.AndroidManifest;
import org.robolectric.res.FileFsFile;
import org.robolectric.res.FsFile;

public class CustomRobolectricRunner extends RobolectricTestRunner {

    public CustomRobolectricRunner(Class<?> klass) throws InitializationError {
        super(klass);
    }

    @Override
    protected AndroidManifest getAppManifest(Config config) {
        AndroidManifest appManifest = super.getAppManifest(config);
        FsFile androidManifestFile = appManifest.getAndroidManifestFile();

        if (androidManifestFile.exists()) {
            return appManifest;
        } else {
            String moduleRoot = getModuleRootPath(config);
            androidManifestFile = FileFsFile.from(moduleRoot, appManifest.getAndroidManifestFile().getPath());
            FsFile resDirectory = FileFsFile.from(moduleRoot, appManifest.getAndroidManifestFile().getPath().replace("AndroidManifest.xml", "res"));
            FsFile assetsDirectory = FileFsFile.from(moduleRoot, appManifest.getAndroidManifestFile().getPath().replace("AndroidManifest.xml", "assets"));
            return new AndroidManifest(androidManifestFile, resDirectory, assetsDirectory);
        }

    }

    private String getModuleRootPath(Config config) {
        String moduleRoot = config.constants().getResource("").toString().replace("file:", "").replace("jar:", "");
        return moduleRoot.substring(0, moduleRoot.indexOf("/build"));
    }
}
