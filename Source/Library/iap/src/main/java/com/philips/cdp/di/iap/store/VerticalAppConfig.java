/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.store;

import com.philips.cdp.di.iap.integration.IAPSettings;

public class VerticalAppConfig {
    private IAPSettings mIAPSettings;

    public VerticalAppConfig(IAPSettings iapSettings) {
        mIAPSettings = iapSettings;
    }

    public String getHostPort() {
        return mIAPSettings.getHostPort();
    }

    public String getProposition() {
        return mIAPSettings.getProposition();
    }
}
