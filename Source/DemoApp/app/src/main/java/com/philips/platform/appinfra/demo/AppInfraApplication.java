/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.demo;

import android.app.Application;
import android.content.SharedPreferences;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.appinfra.tagging.ApplicationLifeCycleHandler;
import com.squareup.leakcanary.LeakCanary;

/**
 * Created by deepakpanigrahi on 5/18/16.
 */
public class AppInfraApplication extends Application {
    public static AppTaggingInterface mAIAppTaggingInterface;
    public static AppInfraInterface gAppInfra;
    private AppInfra mAppInfra;


    //SecurDb
    public static String DATABASE_PASSWORD_KEY = "philips@321";
    static SecureStorageInterface mSecureStorage = null;
    SharedPreferences sharedPreferences = null;
    SharedPreferences.Editor editor;


    @Override
    public void onCreate() {
        super.onCreate();

        LeakCanary.install(this);

        gAppInfra = new AppInfra.Builder().build(getApplicationContext());
        mAppInfra = (AppInfra)gAppInfra;
        mAIAppTaggingInterface = gAppInfra.getTagging().createInstanceForComponent("Component name", "Component ID");
        mAIAppTaggingInterface.setPreviousPage("SomePreviousPage");
       /* gAppInfra.getServiceDiscovery().getServiceUrlWithLanguagePreference("userreg.janrain.api", new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
            @Override
            public void onSuccess(URL url) {
                Log.i("SUCCESS ***", "" + url);
            }

            @Override
            public void onError(ERRORVALUES error, String message) {
                Log.i("ERRORVALUES ***", "" + message);
            }
        });*/

        ApplicationLifeCycleHandler handler = new ApplicationLifeCycleHandler(mAppInfra);
        registerActivityLifecycleCallbacks(handler);
        registerComponentCallbacks(handler);

        sharedPreferences = getSharedPreferences("com.appinfra", MODE_PRIVATE);

        if (sharedPreferences.getBoolean("firstRun", true)) {
            mSecureStorage = gAppInfra.getSecureStorage();
            SecureStorageInterface.SecureStorageError sse = new SecureStorageInterface.SecureStorageError(); // to get error code if any
            mSecureStorage.createKey(SecureStorageInterface.KeyTypes.AES, DATABASE_PASSWORD_KEY, sse);

            editor = sharedPreferences.edit();
            editor.putBoolean("firstRun", false);
            editor.commit();

        }

    }
}
