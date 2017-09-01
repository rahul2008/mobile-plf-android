/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.aildemolaunch;

import android.app.Application;
import android.util.Log;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.tagging.ApplicationLifeCycleHandler;
import com.philips.platform.philipsdevtools.ServiceDiscoveryManagerCSV;
import com.squareup.leakcanary.LeakCanary;

import static android.content.ContentValues.TAG;

public class AppInfraApplication extends Application {

    private AppInfraInterface gAppInfra;

    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
        AppInfra.Builder builder = new AppInfra.Builder();
        ServiceDiscoveryManagerCSV sdmCSV = new ServiceDiscoveryManagerCSV();
        builder.setServiceDiscovery(sdmCSV);
        gAppInfra = builder.build(getApplicationContext());
        initServiceDiscovery((AppInfra) gAppInfra, sdmCSV);
        ApplicationLifeCycleHandler handler = new ApplicationLifeCycleHandler((AppInfra) gAppInfra);
        registerActivityLifecycleCallbacks(handler);
        registerComponentCallbacks(handler);
    }

    public AppInfraInterface getAppInfra() {
        return gAppInfra;
    }

    private void initServiceDiscovery(AppInfra mAppInfra, ServiceDiscoveryManagerCSV sdmCSV) {
        sdmCSV.init(mAppInfra);
        sdmCSV.refresh(new ServiceDiscoveryInterface.OnRefreshListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "success Response from Service Discovery CSV :");
            }

            @Override
            public void onError(ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES errorvalues, String s) {
                Log.d(TAG, "Error Response from Service Discovery CSV :" + s);
            }
        });
    }

}
