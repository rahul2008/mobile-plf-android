package com.philips.platform.ccdemouapp;

import android.app.Application;

import com.philips.platform.appinfra.AppInfra;
//import com.philips.platform.appinfra.devtools.servicediscovery.ServiceDiscoveryManagerCSV;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.cdp.digitalcare.ServiceDiscoveryManagerCSV;

/**
 * Created by sampath.kumar on 3/22/2017.
 */

public class CCDemoApplication extends Application {

    public static AppInfra gAppInfra;
    private ServiceDiscoveryManagerCSV sdmCSV;


    @Override
    public void onCreate() {
        super.onCreate();

        AppInfra.Builder builder = new AppInfra.Builder();
        gAppInfra = builder.build(getApplicationContext());
        initServiceDiscovery(builder);
        gAppInfra = builder.build(getApplicationContext());
    }

    private void initServiceDiscovery(AppInfra.Builder builder) {
        sdmCSV =  new ServiceDiscoveryManagerCSV();
        sdmCSV.init(gAppInfra);
        builder.setServiceDiscovery(sdmCSV);
        sdmCSV.refresh(new ServiceDiscoveryInterface.OnRefreshListener() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(ERRORVALUES errorvalues, String s) {

            }
        });
    }
}
