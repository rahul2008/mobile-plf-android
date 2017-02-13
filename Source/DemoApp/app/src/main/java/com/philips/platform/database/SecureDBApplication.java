/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.database;

import android.app.Application;
import android.content.SharedPreferences;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;


public class SecureDBApplication extends Application {

    static AppInfraInterface appInfra;


    @Override
    public void onCreate() {
        super.onCreate();
        appInfra = new AppInfra.Builder().build(getApplicationContext());

    }

    public static AppInfraInterface getAppInfraInterface()
    {
       return appInfra;
    }

}
