
package com.philips.platform.udidemo;

import android.app.Application;
import android.content.Context;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.uid.thememanager.UIDHelper;
import com.squareup.leakcanary.LeakCanary;

public class UDIDemoApplication extends Application {
    private static UDIDemoApplication mUdiApplication = null;

    private AppInfraInterface mAppInfraInterface;

    /**
     * @return instance of this class
     */
    public synchronized static UDIDemoApplication getInstance() {
        return mUdiApplication;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        UIDHelper.injectCalligraphyFonts();
    }


    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
        mUdiApplication = this;
        mAppInfraInterface = new AppInfra.Builder().build(this);

    }


    public AppInfraInterface getAppInfra() {
        return mAppInfraInterface;
    }

}