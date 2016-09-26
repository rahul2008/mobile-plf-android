package com.philips.cdp.di.iap.integration;

/**
 * Created by indrajitkumar on 22/09/16.
 */

public class MockIAPHandler extends IAPHandler {
    MockIAPHandler(IAPDependencies mIAPDependencies, IAPSettings pIapSettings) {
        super(mIAPDependencies, pIapSettings);
    }

    @Override
    void initTaggingLogging() {
    }

}
