package com.pim.demouapp;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.uappframework.uappinput.UappDependencies;

/**
 * Created by philips on 6/16/17.
 */

public class PimDemoUAppDependencies extends UappDependencies {

    private final AppInfraInterface appInfra;

    @Override
    public AppInfraInterface getAppInfra() {
        return appInfra;
    }

    public PimDemoUAppDependencies(AppInfraInterface appInfra) {
        super(appInfra);
        this.appInfra = appInfra;

    }
}
