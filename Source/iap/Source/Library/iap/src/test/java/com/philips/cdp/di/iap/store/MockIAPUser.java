/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.store;

public class MockIAPUser extends IAPUser{
    private String mJanRainEmail = "testinapp12@mailinator.com";
    private boolean mTokenRefreshSuccessful = false;

    public MockIAPUser(HybrisStore store) {
        HybrisStore mHybrisStore = store;
        IAPUser mJanRainUser = new IAPUser();

    }

    public String getJanRainID() {
        String mJanRainID = "bfhxdmje7wg3dm";
        return mJanRainID;
    }

    public String getJanRainEmail() {
        return mJanRainEmail;
    }

    public boolean isTokenRefreshSuccessful() {
        return mTokenRefreshSuccessful;
    }

    public void onUserLogoutSuccess() {
    }

    public void onUserLogoutFailure() {
    }

    public void onUserLogoutSuccessWithInvalidAccessToken() {
    }
}
