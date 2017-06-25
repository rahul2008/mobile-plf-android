/*
package com.philips.amwelluapp;

import org.junit.runners.model.InitializationError;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.manifest.AndroidManifest;
import org.robolectric.res.FileFsFile;
import org.robolectric.res.FsFile;

public class LibraryProjectTestRunner extends RobolectricGradleTestRunner {
    public LibraryProjectTestRunner(Class<?> klass) throws InitializationError {
        super(klass);
    }

    @Override
    protected AndroidManifest getAppManifest(Config config) {
        final AndroidManifest appManifest = super.getAppManifest(config);
        FsFile androidManifestFile = appManifest.getAndroidManifestFile();

        //if (androidManifestFile.exists()) {
          //  return appManifest;
        //} else {
            String moduleRoot = getModuleRootPath(config);
            androidManifestFile = FileFsFile.from(moduleRoot, appManifest.getAndroidManifestFile().getPath().replace("full", "aapt"));
            final FsFile resDirectory = FileFsFile.from(moduleRoot, appManifest.getResDirectory().getPath().replace("build\\intermediates\\res\\merged\\debug", "build\\intermediates\\bundles\\debug\\res"));
           // FsFile assetsDirectory = FileFsFile.from(moduleRoot, appManifest.getAssetsDirectory().getPath().replace("build\\intermediates\\assets\\merged\\debug", "build\\intermediates\\bundles\\debug\\assets"));
            return new AndroidManifest(androidManifestFile, resDirectory, appManifest.getAssetsDirectory()){
            */
/*  @Override
              public String getRClassName() throws Exception {
                  return "com.philips.amwelluapp.test";
              }*//*

            };
        //}
    }

    private String getModuleRootPath(Config config) {
        String moduleRoot = config.constants().getResource("").toString().replace("file:", "");
        return moduleRoot.substring(0, moduleRoot.indexOf("/build"));
    }

}
*/
