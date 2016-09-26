/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.store;

import com.philips.cdp.di.iap.integration.IAPDependencies;
import com.philips.cdp.di.iap.utils.IAPLog;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;

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
        AppConfigurationInterface.AppConfigurationError configError =
                new AppConfigurationInterface.AppConfigurationError();

        mHostPort = "acc.occ.shop.philips.com";
        mProposition = "Tuscany2016";

        if (configError.getErrorCode() != null) {
            IAPLog.e(IAPLog.LOG, "VerticalAppConfig ==loadConfigurationFromAsset " +
                    configError.getErrorCode().toString());
        }
    }
}
