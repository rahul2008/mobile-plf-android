
package com.philips.cdp.digitalcare;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import org.junit.runners.model.InitializationError;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.manifest.AndroidManifest;
import org.robolectric.res.FileFsFile;
import org.robolectric.res.FsFile;


/**
 * More dynamic path resolution.
 *
 * This workaround is only for Mac Users necessary and only if they don't use the $MODULE_DIR$
 * workaround. Follow this issue at https://code.google.com/p/android/issues/detail?id=158015
 */

public class CustomRobolectricRunner extends RobolectricTestRunner {


/*    // Build output location for Android Studio 2.2 and older
    private static final String BUILD_OUTPUT = "build/intermediates";
    // Build output location for Android Studio 2.3 and newer
    private static final String BUILD_OUTPUT_APP_LEVEL = "build/intermediates";

    public CustomRobolectricRunner(Class<?> testClass) throws InitializationError {
        super(testClass);
    }

    @Override
    protected AndroidManifest getAppManifest(Config config) {
        FileFsFile res = FileFsFile.from(BUILD_OUTPUT, "/res/merged", BuildConfig.FLAVOR, BuildConfig.BUILD_TYPE);
        FileFsFile assets = FileFsFile.from(BUILD_OUTPUT, "assets", BuildConfig.FLAVOR, BuildConfig.BUILD_TYPE);
        FileFsFile manifest = FileFsFile.from(BUILD_OUTPUT, "manifests", "aapt", BuildConfig.FLAVOR, BuildConfig.BUILD_TYPE, "AndroidManifest.xml");

        // If above files does not exist in the specified paths, look them at the app folder level. This is needed
        // as Android studio 2.3 changed the working directory.
        if (!manifest.exists()) {
            manifest = FileFsFile.from(BUILD_OUTPUT_APP_LEVEL, "manifests", "aapt", BuildConfig.FLAVOR, BuildConfig.BUILD_TYPE, "AndroidManifest.xml");
        }

        if (!res.exists()) {
            res = FileFsFile.from(BUILD_OUTPUT_APP_LEVEL, "/res/merged", BuildConfig.FLAVOR, BuildConfig.BUILD_TYPE);
        }

        if (!assets.exists()) {
            assets = FileFsFile.from(BUILD_OUTPUT_APP_LEVEL, "assets", BuildConfig.FLAVOR, BuildConfig.BUILD_TYPE);
        }

        AndroidManifest androidManifest = new AndroidManifest(manifest, res, assets) {
            @Override
            public Class getRClass() {
                return R.class;
            }

            public int getTargetSdkVersion() {
                *//**
                 * Lollipop is currently the highest version supported in 3.0.
                 *//*
                return Build.VERSION_CODES.LOLLIPOP;
            }
        };

        return androidManifest;
    }*/


    public CustomRobolectricRunner(Class<?> klass) throws InitializationError {
        super(klass);
    }


    public static void startFragment( Fragment fragment )
    {
        FragmentManager fragmentManager = new FragmentActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add( fragment, null );
        fragmentTransaction.commit();
    }


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

