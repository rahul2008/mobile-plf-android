package com.philips.cdp.digitalcare;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.uappframework.uappinput.UappDependencies;

/**
 * class to get the dependency modules
 * Created by sampath.kumar on 8/17/2016.
 */
public class CcDependencies extends UappDependencies {

    /**
     * gets the dependency module
     * @param appInfra
     */
    public CcDependencies(AppInfraInterface appInfra) {
        super(appInfra);
    }
}
