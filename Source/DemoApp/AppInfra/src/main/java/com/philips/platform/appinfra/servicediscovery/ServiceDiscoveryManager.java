package com.philips.platform.appinfra.servicediscovery;

import android.content.Context;

import com.philips.platform.appinfra.AppInfra;

/**
 * Created by 310238655 on 6/1/2016.
 */
public class ServiceDiscoveryManager implements ServiceDiscoveryInterface {

    AppInfra mAppInfra;
    Context context;

    public ServiceDiscoveryManager(AppInfra aAppInfra) {
        mAppInfra = aAppInfra;
        context = mAppInfra.getAppInfraContext();
        // Class shall not presume appInfra to be completely initialized at this point.
        // At any call after the constructor, appInfra can be presumed to be complete.

    }

    @Override
    public void getservice() {
        new RequestManager(context).execute("https://tst.philips.com/api/v1/discovery/b2c/12345?locale=en&country=IN");
    }
}
