package com.philips.cdp.prodreg.launcher;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.uappframework.uappinput.UappDependencies;

/**
 * It is used to pass AppInfraInterface from proposition to Product Registration component
 * @since 1.0.0
 */
public class PRDependencies extends UappDependencies {

    private static final long serialVersionUID = -6635233525340545677L;

    /**
     * Create PRDependencies instance from AppInfraInterface object
     * @param appInfra - to pass the instance of AppInfraInterface
     * @since 1.0.0
     */
    public PRDependencies(final AppInfraInterface appInfra) {
        super(appInfra);
    }
}
