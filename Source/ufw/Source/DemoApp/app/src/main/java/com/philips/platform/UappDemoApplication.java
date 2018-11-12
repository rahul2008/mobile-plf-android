/* Copyright (c) 2016 Koninklijke Philips N.V.
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform;

import android.app.Application;

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
        super.onCreate();
        appInfra = new AppInfra.Builder().build(getApplicationContext());

    }

    public AppInfraInterface getAppInfra() {
        return appInfra;
    }

}
