package com.philips.cdp.prodreg.launcher;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.uappframework.uappinput.UappDependencies;

/**
 * It is used to pass AppInfraInterface from proposition to Product Registration component
 */
public class PRDependencies extends UappDependencies {

    /**
     * creates instance of PRDependencies with AppInfraInterface object
     * @param appInfra - AppInfraInterface appInfra
     * @since 1.0.0
     */
    public PRDependencies(final AppInfraInterface appInfra) {
        super(appInfra);
    }
}
