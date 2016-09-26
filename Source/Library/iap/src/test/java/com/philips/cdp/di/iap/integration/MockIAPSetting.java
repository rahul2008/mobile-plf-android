package com.philips.cdp.di.iap.integration;

import android.content.Context;

/**
 * Created by indrajitkumar on 22/09/16.
 */

public class MockIAPSetting extends IAPSettings {
    public MockIAPSetting(Context applicationContext) {
        super(applicationContext);
    }

    @Override
    public boolean isUseLocalData() {
        return false;
    }

    @Override
    public void setUseLocalData(boolean mUseLocalData) {
        super.setUseLocalData(mUseLocalData);
    }
}
