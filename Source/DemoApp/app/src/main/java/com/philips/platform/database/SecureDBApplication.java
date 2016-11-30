/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.database;

import android.app.Application;

import com.crittercism.app.Crittercism;
import com.facebook.stetho.Stetho;

public class SecureDBApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
        Crittercism.initialize(getApplicationContext(),"513a43d30f754f219b006e795cd7a20600555300");
    }
}
