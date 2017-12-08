package com.philips.platform.ccdemouapp;

import android.app.Application;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInterface;

/**
 * Created by sampath.kumar on 3/22/2017.
 */

public class CCDemoApplication extends Application {

    public static AppInfraInterface gAppInfra;

    @Override
    public void onCreate() {
        super.onCreate();

        gAppInfra = new AppInfra.Builder().build(getApplicationContext());
    }
}
