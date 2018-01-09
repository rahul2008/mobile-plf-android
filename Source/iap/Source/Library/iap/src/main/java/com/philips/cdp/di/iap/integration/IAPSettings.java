/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.integration;

import android.content.Context;

import com.philips.platform.uappframework.uappinput.UappSettings;

/**
 * It is used to get required inputs from propositions
 */
public class IAPSettings extends UappSettings {
    private boolean mUseLocalData;
    private String mProposition;
    private String mHostPort;

    /**
     * used to create IAPSettings instance
     * @param applicationContext
     * @since 1.0.0
     */
    public IAPSettings(Context applicationContext) {
        super(applicationContext);
    }

    /**
     * It specifies whether It is Hybris flow or not
     * @return mUseLocalData - boolean seLocalData
     * @since 1.0.0
     */
    public boolean isUseLocalData() {
        return mUseLocalData;
    }

    /**
     * enable or disable Hybris flow.
     * @param isLocalData
     * @since 1.0.0
     */
    void setUseLocalData(boolean isLocalData) {
        mUseLocalData = isLocalData;
    }

    /**
     * sets proposition ID
     * @param proposition - String proposition
     * @since 1.0.0
     */
    public void setProposition(String proposition) {
        mProposition = proposition;
    }

    /**
     * Returns proposition ID
     * @return proposition ID - String mProposition
     * @since 1.0.0
     */
    public String getProposition() {
        return mProposition;
    }

    /**
     *
     * @return hostport - String mHostPort
     * @since 1.0.0
     */
    public String getHostPort() {
        return mHostPort;
    }

    /**
     * sets hostPort id
     * @param hostPort - String hostPort
     * @since 1.0.0
     */
    public void setHostPort(String hostPort) {
        mHostPort = hostPort;
    }
}
