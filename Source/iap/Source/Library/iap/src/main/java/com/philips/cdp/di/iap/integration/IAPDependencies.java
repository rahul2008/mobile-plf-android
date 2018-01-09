/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.integration;

import com.philips.cdp.di.iap.container.CartModelContainer;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.uappframework.uappinput.UappDependencies;

/**
 * It is used to get AppInfraInterface from proposition
 */
public class IAPDependencies extends UappDependencies {

    /**
     * Create IAPDependencies instance from AppInfraInterface object
     * @param appInfra - AppInfraInterface appInfra
     * @since 1.0.0
     */
    public IAPDependencies(AppInfraInterface appInfra) {
        super(appInfra);
        CartModelContainer.getInstance().setAppInfraInstance(appInfra);
    }


}
