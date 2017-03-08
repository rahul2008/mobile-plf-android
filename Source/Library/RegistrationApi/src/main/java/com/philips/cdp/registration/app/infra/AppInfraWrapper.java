package com.philips.cdp.registration.app.infra;


import com.philips.platform.appinfra.AppInfraInterface;

public class AppInfraWrapper {

    private final AppInfraInterface appInfra;

    public AppInfraWrapper(AppInfraInterface appInfra) {
        this.appInfra = appInfra;
    }
}
