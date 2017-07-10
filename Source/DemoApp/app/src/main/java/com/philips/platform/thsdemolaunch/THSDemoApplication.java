package com.philips.platform.thsdemolaunch;

import android.app.Application;

import com.philips.platform.ths.utility.AmwellLog;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.uid.thememanager.UIDHelper;

public class THSDemoApplication extends Application {
    public AppInfraInterface appInfra;
    public static LoggingInterface loggingInterface;

    /***
     * Initializar app infra
     *
     */

    public AppInfraInterface getAppInfra() {
        return appInfra;
    }

    @Override
    public void onCreate(){
        super.onCreate();
        UIDHelper.injectCalligraphyFonts();
        AmwellLog.enableLogging(true);
    }
    public void initializeAppInfra(AppInitializationCallback.AppInfraInitializationCallback appInfraInitializationCallback) {
        appInfra = new AppInfra.Builder().build(getApplicationContext());
        loggingInterface = appInfra.getLogging();
        appInfraInitializationCallback.onAppInfraInitialization();
    }
}