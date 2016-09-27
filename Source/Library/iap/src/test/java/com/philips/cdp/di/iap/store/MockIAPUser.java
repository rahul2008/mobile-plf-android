package com.philips.cdp.di.iap.store;

import com.philips.cdp.registration.handlers.RefreshLoginSessionHandler;

/**
 * Created by indrajitkumar on 26/09/16.
 */
public class MockIAPUser extends IAPUser{
    private IAPUser mJanRainUser;
    private final HybrisStore mHybrisStore;
    private String mJanRainID = "bfhxdmje7wg3dm";
    private String mJanRainEmail = "testinapp12@mailinator.com";
    private boolean mTokenRefreshSuccessful;

    public MockIAPUser(HybrisStore store) {
        mHybrisStore = store;
        mJanRainUser = new IAPUser();
    }


    public String getJanRainID() {
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
