/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.demo;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.util.Log;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.appinfra.tagging.ApplicationLifeCycleHandler;
import com.squareup.leakcanary.LeakCanary;

import java.net.URL;

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

        //https://developer.android.com/reference/android/os/StrictMode.html
        // to monitor penaltyLog() log output in logcat for ANR or any other performance issue
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()   // or .detectAll() for all detectable problems
                .penaltyLog()
                .build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .detectLeakedClosableObjects()
                .penaltyLog()
                .penaltyDeath()
                .build());


        LeakCanary.install(this);

        gAppInfra = new AppInfra.Builder().build(getApplicationContext());
        gAppInfra.getTime().refreshTime();
        mAppInfra = (AppInfra)gAppInfra;
        mAIAppTaggingInterface = gAppInfra.getTagging().createInstanceForComponent("Component name", "Component ID");
        mAIAppTaggingInterface.setPreviousPage("SomePreviousPage");
        ApplicationLifeCycleHandler handler = new ApplicationLifeCycleHandler(mAppInfra);
        registerActivityLifecycleCallbacks(handler);
        registerComponentCallbacks(handler);
    }
}
