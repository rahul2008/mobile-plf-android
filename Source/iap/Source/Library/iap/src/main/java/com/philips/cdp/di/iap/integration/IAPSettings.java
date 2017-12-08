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
    private String mHostPort;

    public IAPSettings(Context applicationContext) {
        super(applicationContext);
    }

    public boolean isUseLocalData() {
        return mUseLocalData;
    }

    void setUseLocalData(boolean isLocalData) {
        mUseLocalData = isLocalData;
    }

    public void setProposition(String proposition) {
        mProposition = proposition;
    }

    public String getProposition() {
        return mProposition;
    }

    public String getHostPort() {
        return mHostPort;
    }

    public void setHostPort(String hostPort) {
        mHostPort = hostPort;
    }
}
