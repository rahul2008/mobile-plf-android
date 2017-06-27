/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform;

import android.app.Application;
import android.support.multidex.MultiDex;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInterface;

/**
 * Application class is used for initialization
 */
public class UappDemoApplication extends Application {
    public AppInfraInterface appInfra;

    @SuppressWarnings("deprecation")
    @Override
    public void onCreate() {
        MultiDex.install(this);
        super.onCreate();
        appInfra = new AppInfra.Builder().build(getApplicationContext());

    }

    public AppInfraInterface getAppInfra() {
        return appInfra;
    }

}
