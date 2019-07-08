package com.ecs.demouapp.integration;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.uappframework.uappinput.UappDependencies;

/**
 * Created by philips on 6/16/17.
 */

public class EcsDemoUAppDependencies extends UappDependencies {

    private final AppInfraInterface appInfra;

    @Override
    public AppInfraInterface getAppInfra() {
        return appInfra;
    }

    public EcsDemoUAppDependencies(AppInfraInterface appInfra) {
        super(appInfra);
        this.appInfra = appInfra;

    }
}
