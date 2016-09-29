package com.philips.cdp.di.iap.store;

import com.philips.cdp.di.iap.integration.IAPDependencies;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class MockVerticalAppConfig extends VerticalAppConfig {
    private String mHostPort = "acc.occ.shop.philips.com";
    private String mProposition = "Tuscany2016";

    public MockVerticalAppConfig(IAPDependencies iapDependencies) {
        super(iapDependencies);
    }

    @Override
    public String getHostPort() {
        return mHostPort;
    }

    @Override
    public String getProposition() {
        return mProposition;
    }

    @Override
    void loadConfigurationFromAsset(IAPDependencies iapDependencies) {
        //super.loadConfigurationFromAsset(iapDependencies);
    }
}
