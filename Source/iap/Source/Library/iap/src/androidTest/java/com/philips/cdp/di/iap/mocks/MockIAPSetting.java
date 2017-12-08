/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.mocks;

import android.content.Context;

import com.philips.cdp.di.iap.integration.IAPSettings;

public class MockIAPSetting extends IAPSettings {
    public MockIAPSetting(Context applicationContext) {
        super(applicationContext);
    }

    @Override
    public String getHostPort() {
        return "https://acc.occ.shop.philips.com/";
    }
}
