/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.mec.integration;

import android.content.Context;

import com.philips.platform.uappframework.uappinput.UappSettings;

/**
 * MECSettings class is used to initialize basic settings for InAppPurchase. Right now InAppPurchase doesn’t have any settings to be initialized. So only default initialization of IAPSettings is required to be passed while creating IAPInterface object.
 * @since 1.0.0
 */
public class MECSettings extends UappSettings {
    private boolean mUseLocalData;
    private String mProposition;
    private String mHostPort;


    public MECSettings(Context applicationContext) {
        super(applicationContext);
    }

}
