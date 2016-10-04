/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.integration;

import android.content.Context;

public class MockIAPSetting extends IAPSettings {
    public MockIAPSetting(Context applicationContext) {
        super(applicationContext);
    }

    @Override
    public void setUseLocalData(boolean mUseLocalData) {
        super.setUseLocalData(mUseLocalData);
    }

   /* public boolean setPlanA() {
        return true;
    }

    public boolean setPlanB() {
        return true;
    }*/
}
