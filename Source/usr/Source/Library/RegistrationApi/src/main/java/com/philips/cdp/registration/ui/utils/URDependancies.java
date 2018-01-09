package com.philips.cdp.registration.ui.utils;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.uappframework.uappinput.UappDependencies;

/**
 * It is used to accept AppInfraInterface as a parameter to UR component
 */
public class URDependancies extends UappDependencies {

    /**
     * creates instance of URDependancies with AppInfraInterface object
     * @param appInfra - AppInfraInterface appInfra
     *                 @since 1.0.0
     */
    public URDependancies(AppInfraInterface appInfra) {
        super(appInfra);
    }

}
