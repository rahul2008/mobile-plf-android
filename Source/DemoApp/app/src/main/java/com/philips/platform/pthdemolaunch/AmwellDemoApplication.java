package com.philips.platform.pthdemolaunch;

import android.app.Application;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;

public class AmwellDemoApplication extends Application {
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
    }
    public void initializeAppInfra(AppInitializationCallback.AppInfraInitializationCallback appInfraInitializationCallback) {
        appInfra = new AppInfra.Builder().build(getApplicationContext());
        loggingInterface = appInfra.getLogging();
        appInfraInitializationCallback.onAppInfraInitialization();
    }
}