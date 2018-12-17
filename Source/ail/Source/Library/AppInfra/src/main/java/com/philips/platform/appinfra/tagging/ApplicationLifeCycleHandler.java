package com.philips.platform.appinfra.tagging;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks2;
import android.content.res.Configuration;
import android.os.Bundle;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraLogEventID;
import com.philips.platform.appinfra.logging.LoggingInterface;

/**
 * A Wrapper class for Application Life Cycle.
 */

public class ApplicationLifeCycleHandler implements Application.ActivityLifecycleCallbacks, ComponentCallbacks2 {

    private static final String TAG = ApplicationLifeCycleHandler.class.getSimpleName();
    public static boolean isInBackground = true;

    private AppInfra mAppInfra;
    private AppTaggingInterface mAppTaggingInterface;

    public ApplicationLifeCycleHandler(AppInfra appInfra) {
        mAppInfra = appInfra;
        mAppTaggingInterface = mAppInfra.getTagging();
    }


    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
        mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.DEBUG,
                AppInfraLogEventID.AI_TAGGING,"ApplicationLifeCycleHandler Created");

    }

    @Override
    public void onActivityStarted(Activity activity) {
        mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.DEBUG,
                AppInfraLogEventID.AI_TAGGING,"ApplicationLifeCycleHandler Started");

    }

    @Override
    public void onActivityResumed(Activity activity) {

        if (isInBackground) {

            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.DEBUG,
                    AppInfraLogEventID.AI_TAGGING,"ApplicationLifeCycleHandler Resumed");
        mAppTaggingInterface.trackActionWithInfo("sendData", "appStatus", "ForeGround");
            isInBackground = false;
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {
        mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.DEBUG,
                AppInfraLogEventID.AI_TAGGING,"ApplicationLifeCycleHandler Paused");

    }

    @Override
    public void onActivityStopped(Activity activity) {
        mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.DEBUG,
                AppInfraLogEventID.AI_TAGGING, "ApplicationLifeCycleHandler Stopped");

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
        mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.DEBUG,
                AppInfraLogEventID.AI_TAGGING, "ApplicationLifeCycleHandler SaveInstanceState");
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.DEBUG,
                AppInfraLogEventID.AI_TAGGING, "ApplicationLifeCycleHandler Destroyed");

    }

    @Override
    public void onConfigurationChanged(Configuration configuration) {
        mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.DEBUG,
                AppInfraLogEventID.AI_TAGGING, "ApplicationLifeCycleHandler ConfigurationChanged");
//        Intent i = mAppInfra.getAppInfraContext().getApplicationContext().getPackageManager().getLaunchIntentForPackage(mAppInfra.getAppInfraContext().getPackageName());
//        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
//        mAppInfra.getAppInfraContext().startActivity(i);
    }

    @Override
    public void onLowMemory() {
        mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.DEBUG,
                AppInfraLogEventID.AI_TAGGING,"ApplicationLifeCycleHandler onLowMemory");
    }

    @Override
    public void onTrimMemory(int i) {
        if (i == ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) {
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.DEBUG,
                    AppInfraLogEventID.AI_TAGGING,"ApplicationLifeCycleHandler Background");

            mAppTaggingInterface.trackActionWithInfo("sendData","appStatus", "Background");
            isInBackground = true;
        }
    }
}