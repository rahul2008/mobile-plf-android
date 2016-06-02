package com.philips.cdp.appframework;

import android.app.Application;
import android.support.multidex.MultiDex;

/**
 * Created by 310240027 on 6/2/2016.
 */
public class AppFrameworkApplication extends Application {
    @Override
    public void onCreate() {
        MultiDex.install(this);
        super.onCreate();
    }
}
