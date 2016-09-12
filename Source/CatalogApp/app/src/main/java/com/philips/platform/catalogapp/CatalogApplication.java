package com.philips.platform.catalogapp;

import android.app.Application;

import com.philips.platform.uit.thememanager.UITHelper;

public class CatalogApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        UITHelper.injectCalligraphyFonts();
    }
}
