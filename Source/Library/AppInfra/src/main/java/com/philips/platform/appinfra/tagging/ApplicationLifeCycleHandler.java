package com.philips.platform.appinfra.tagging;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks2;
import android.content.res.Configuration;
import android.os.Bundle;

import com.adobe.mobile.Config;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInterface;

/**
 * Created by 310200764 on 9/1/2015.
 */

public class ApplicationLifeCycleHandler implements Application.ActivityLifecycleCallbacks, ComponentCallbacks2 {

    private AppInfra mAppInfra;
    private static final String TAG = ApplicationLifeCycleHandler.class.getSimpleName();
    public static boolean isInBackground = true;
    public void Init(AppInfra appInfra){
        mAppInfra = appInfra;
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
            mAppInfra.getTagging().collectLifecycleInfo(activity);
            isInBackground = false;
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {
    }

    @Override
    public void onActivityStopped(Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
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
            mAppInfra.getTagging().pauseLifecycleInfo();
            isInBackground = true;
        }
    }
}