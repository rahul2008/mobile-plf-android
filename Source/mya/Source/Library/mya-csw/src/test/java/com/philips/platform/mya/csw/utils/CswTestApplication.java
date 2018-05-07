package com.philips.platform.mya.csw.utils;

import android.app.Application;

import com.philips.platform.mya.csw.R;

public class CswTestApplication extends Application
{
    @Override
    public void onCreate() {
        super.onCreate();
        setTheme(R.style.AppTheme);
    }
}
