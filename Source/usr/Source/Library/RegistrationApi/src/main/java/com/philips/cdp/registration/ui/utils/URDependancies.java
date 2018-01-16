package com.philips.cdp.registration.ui.utils;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.uappframework.uappinput.UappDependencies;

/**
 * URDependancies handles the dependency required for USR. So right now, UR has one dependency i.e AppInfra. So vertical needs to initialize IAPDependencies and set the app infra object. This app infra object will be responsible for logging, tagging and some configuration.
 * @since 1.0.0
 */
public class URDependancies extends UappDependencies {
    /**
     * Create URDependancies instance from AppInfraInterface object
     * @param appInfra  pass the instance of AppInfraInterface
     * @since 1.0.0
     */
    public URDependancies(AppInfraInterface appInfra) {
        super(appInfra);
    }

}
