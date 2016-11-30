/*
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 *
 */
package com.philips.platform.catalogapp;

import android.app.Application;

import com.philips.platform.uid.thememanager.UIDHelper;

public class CatalogApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        UIDHelper.injectCalligraphyFonts();
    }
}
