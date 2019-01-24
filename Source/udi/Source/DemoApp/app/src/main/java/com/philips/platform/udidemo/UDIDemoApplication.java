
package com.philips.platform.udidemo;

import android.app.Application;

import com.philips.platform.appinfra.AppInfra;

public class UDIDemoApplication extends Application {
    private AppInfra mAppInfra;

    @Override
    public void onCreate() {
        super.onCreate();

        mAppInfra = new AppInfra.Builder().build(getApplicationContext());
    }

    public AppInfra getAppInfra() {
        return mAppInfra;
    }
}