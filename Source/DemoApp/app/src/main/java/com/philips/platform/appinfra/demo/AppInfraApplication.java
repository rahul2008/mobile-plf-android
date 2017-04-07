/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.demo;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import com.crittercism.app.Crittercism;
import com.crittercism.app.CrittercismConfig;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;
import com.philips.platform.appinfra.tagging.AppTagging;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.appinfra.tagging.ApplicationLifeCycleHandler;
import com.squareup.leakcanary.LeakCanary;

import java.util.Map;

/**
 * Created by deepakpanigrahi on 5/18/16.
 */
public class AppInfraApplication extends Application {
    public static AppTaggingInterface mAIAppTaggingInterface;
    public static AppInfraInterface gAppInfra;
    private AppInfra mAppInfra;
    private static final String CRITTERCISM_APP_ID = "cba7f25561b444e5b0aa29639669532d00555300";


    //SecurDb
    public static String DATABASE_PASSWORD_KEY = "philips@321";
    static SecureStorageInterface mSecureStorage = null;
    SharedPreferences sharedPreferences = null;
    SharedPreferences.Editor editor;


    @Override
    public void onCreate() {
        super.onCreate();
        Crittercism.initialize(getApplicationContext(), CRITTERCISM_APP_ID);
        Crittercism.didCrashOnLastLoad();
        CrittercismConfig config = new CrittercismConfig();
        config.setLogcatReportingEnabled(true);

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
        mAIAppTaggingInterface.registerReceiver(rec);
        mAIAppTaggingInterface.trackVideoEnd("track - demo APP");
        mAIAppTaggingInterface.setPreviousPage("SomePreviousPage");
        ApplicationLifeCycleHandler handler = new ApplicationLifeCycleHandler(mAppInfra);
        registerActivityLifecycleCallbacks(handler);
        registerComponentCallbacks(handler);
    }


    private BroadcastReceiver rec = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("AppInfra APP", "BroadcastReceiver() {...}.onReceive()");
            Map textExtra = (Map) intent.getSerializableExtra(AppTagging.DATA_EXTRA);
            Crittercism.leaveBreadcrumb(textExtra.toString());
            Toast.makeText(getApplicationContext(),
                    textExtra.toString(), Toast.LENGTH_LONG).show();
        }
    };

    @Override
    public void onTerminate() {
        super.onTerminate();
        mAIAppTaggingInterface.unregisterReceiver(rec);
    }
}
