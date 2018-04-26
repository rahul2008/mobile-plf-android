package com.philips.platform.appinfra.logging.model;


import com.philips.platform.appinfra.AppInfra;

public class DeviceDataProvider {
    private AppInfra appInfra;

    public DeviceDataProvider(AppInfra appInfra){
        this.appInfra = appInfra;
    }
    public String getNetworkType() {
        return appInfra.getRestClient().getNetworkReachabilityStatus().toString();
    }



}
