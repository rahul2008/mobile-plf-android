package com.philips.platform.csw.utils;

import android.app.Application;

import com.philips.platform.csw.R;

public class CswTestApplication extends Application
{
    @Override
    public void onCreate() {
        super.onCreate();
        setTheme(R.style.AppTheme);
    }
}
