package com.philips.cdp.di.iap.integration;

import com.philips.platform.appinfra.AppInfraInterface;

/**
 * Created by 310164421 on 8/24/2016.
 */
public class MockIAPDependencies extends IAPDependencies {
    public MockIAPDependencies(AppInfraInterface appInfra) {
        super(appInfra);
    }

    public MockIAPDependencies() {
        super(null);
    }
}
