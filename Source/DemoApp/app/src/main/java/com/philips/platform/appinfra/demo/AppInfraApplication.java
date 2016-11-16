/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.demo;

import android.app.Application;
import android.util.Log;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.appinfra.tagging.ApplicationLifeCycleHandler;
import com.squareup.leakcanary.LeakCanary;

import java.net.URL;

/**
 * Created by deepakpanigrahi on 5/18/16.
 */
public class AppInfraApplication extends Application {
    public static AppTaggingInterface mAIAppTaggingInterface;
    public static AppInfraInterface gAppInfra;
    private AppInfra mAppInfra;

    @Override
    public void onCreate() {
        super.onCreate();

        LeakCanary.install(this);

        gAppInfra = new AppInfra.Builder().build(getApplicationContext());
        mAppInfra = (AppInfra)gAppInfra;
        mAIAppTaggingInterface = gAppInfra.getTagging().createInstanceForComponent("Component name", "Component ID");
        mAIAppTaggingInterface.setPreviousPage("SomePreviousPage");
        gAppInfra.getServiceDiscovery().getServiceUrlWithLanguagePreference("userreg.janrain.api", new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
            @Override
            public void onSuccess(URL url) {
                Log.i("SUCCESS ***", "" + url);
            }

            @Override
            public void onError(ERRORVALUES error, String message) {
                Log.i("ERRORVALUES ***", "" + message);
            }
        });

        ApplicationLifeCycleHandler handler = new ApplicationLifeCycleHandler(mAppInfra);
        registerActivityLifecycleCallbacks(handler);
        registerComponentCallbacks(handler);

    }

}
