package com.philips.platform.pim.integration;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.uappframework.uappinput.UappDependencies;

// TODO: Deepthi 15Apr Please change package name for lal public classes as "com.philips.platform.pim" and remove integration folder ASAP
public class PIMDependencies extends UappDependencies {
    /**
     * Constructor for UappDependencies.
     *
     * @param appInfra Requires appInfraInterface object
     *                 // TODO: Deepthi 15Apr version number put a TODO and change version
     * @since 1.0.0
     */
    public PIMDependencies(AppInfraInterface appInfra) {
        super(appInfra);
    }
}
