/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.integration;

import android.content.Context;

import com.philips.platform.uappframework.uappinput.UappSettings;

public class IAPSettings extends UappSettings {
    private boolean mUseLocalData;
    private String mProposition;

    public IAPSettings(Context applicationContext) {
        super(applicationContext);
    }

    public boolean isUseLocalData() {
        return mUseLocalData;
    }

    public void setUseLocalData(boolean mUseLocalData) {
        this.mUseLocalData = mUseLocalData;
    }

    public String getProposition() {
        return mProposition;
    }

    public void setProposition(String mProposition) {
        this.mProposition = mProposition;
    }

}
