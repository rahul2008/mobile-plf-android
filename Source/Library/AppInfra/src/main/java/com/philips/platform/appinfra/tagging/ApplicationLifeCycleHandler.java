package com.philips.platform.appinfra.tagging;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks2;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by 310200764 on 9/1/2015.
 */

public class ApplicationLifeCycleHandler implements Application.ActivityLifecycleCallbacks, ComponentCallbacks2 {

    private static final String TAG = ApplicationLifeCycleHandler.class.getSimpleName();
    public static boolean isInBackground = true;

    AppTaggingInterface mAppTaggingInterface;

    public ApplicationLifeCycleHandler(AppTaggingInterface appTaggingInterface) {
        mAppTaggingInterface = appTaggingInterface;
    }


    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
    }

    @Override
    public void onActivityStarted(Activity activity) {
    }

    @Override
    public void onActivityResumed(Activity activity) {

        if (isInBackground) {

        mAppTaggingInterface.trackPageWithInfo("ApplicationLifeCycleHandler", "AppState", "ForeGround");
        Log.i("onResumed", "Resumed");

            isInBackground = false;
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {

        Log.i("onPaused", "Paused");
    }

    @Override
    public void onActivityStopped(Activity activity) {
        Log.i("onStopped", "Stopped");
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
        Log.i("onStopped", "Stopped");
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        Log.i("LifeDestroyed", "Destroyed");
    }

    @Override
    public void onConfigurationChanged(Configuration configuration) {
    }

    @Override
    public void onLowMemory() {
    }

    @Override
    public void onTrimMemory(int i) {
        if (i == ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) {
            Log.i("AppBackground", "AppisInBackground "+i+"  "+ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN);
            mAppTaggingInterface.trackPageWithInfo("ApplicationLifeCycleHandler", "AppState", "Background");
            isInBackground = true;
        }
    }
}