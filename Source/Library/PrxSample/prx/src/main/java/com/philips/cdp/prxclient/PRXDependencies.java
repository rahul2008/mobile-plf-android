package com.philips.cdp.prxclient;

import com.philips.platform.appinfra.AppInfraInterface;

/**
 * Created by 310243577 on 12/16/2016.
 */

public class PRXDependencies {

    protected AppInfraInterface mAppInfraInterface;

    public PRXDependencies(AppInfraInterface appInfra) {
        this.mAppInfraInterface = appInfra;
    }

    public AppInfraInterface getAppInfra() {
        return this.mAppInfraInterface;
    }
}
