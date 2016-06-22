/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra;

import android.app.Application;
import android.content.Context;


/**
 * Created by deepakpanigrahi on 5/18/16.
 */
public class AppInfraLibraryApplication extends Application {
    private static Context context;


    public static final String TAG = AppInfraLibraryApplication.class
            .getSimpleName();

    private static AppInfraLibraryApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }
    public AppInfraLibraryApplication(){
        mInstance = this;
    }

    public static synchronized AppInfraLibraryApplication getInstance() {
        return mInstance;
    }




}