/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.store;

import android.app.Activity;
import android.content.Context;

import com.philips.cdp.di.iap.utils.IAPLog;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.handlers.RefreshLoginSessionHandler;
import com.philips.cdp.registration.listener.UserRegistrationListener;
import com.philips.cdp.registration.settings.RegistrationHelper;

import java.util.concurrent.Semaphore;

public class IAPUser implements UserRegistrationListener {
    private final static String TAG = IAPUser.class.getSimpleName();

    Semaphore mSemaphore = new Semaphore(0);
    private User mJanRainUser;
    private Store mStore;
    private boolean mTokenRefreshSuccessful;

    public IAPUser(final Context context, final Store store) {
        RegistrationHelper.getInstance().registerUserRegistrationListener(this);
        mStore = store;
        mJanRainUser = new User(context);
    }

    public String getJanRainID() {
        return mJanRainUser.getAccessToken();
    }

    public String getJanRainEmail() {
        return mJanRainUser.getEmail();
    }

    public void refreshLoginSession() {
        mTokenRefreshSuccessful = false;
        IAPLog.d(TAG, " requesting refresh login session for user");
        mJanRainUser.refreshLoginSession(new RefreshLoginSessionHandler() {
            @Override
            public void onRefreshLoginSessionSuccess() {
                IAPLog.d(TAG, " refreshLoginSuccessful");
                mStore.updateJanRainIDBasedUrls();
                mTokenRefreshSuccessful = true;
                unlockOAuthThread();
            }

            @Override
            public void onRefreshLoginSessionFailedWithError(final int i) {
                IAPLog.d(TAG, " refreshLoginSuccessful failed with error=" + i);
                unlockOAuthThread();
            }

            @Override
            public void onRefreshLoginSessionInProgress(final String s) {
            }
        });

        lockOAuthThread();
    }

    public boolean isTokenRefreshSuccessful() {
        return mTokenRefreshSuccessful;
    }

    private void unlockOAuthThread() {
        mSemaphore.release();
    }

    private void lockOAuthThread() {
        try {
            mSemaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUserRegistrationComplete(Activity activity) {
        //NOP
    }

    @Override
    public void onPrivacyPolicyClick(Activity activity) {
        //NOP
    }

    @Override
    public void onTermsAndConditionClick(Activity activity) {
        //NOP
    }

    @Override
    public void onUserLogoutSuccess() {
        IAPLog.e(IAPLog.LOG, "IAPUser ====onUserLogoutSuccess");
        RegistrationHelper.getInstance().unRegisterUserRegistrationListener(this);
        mStore.setUserLogout(true);
    }

    @Override
    public void onUserLogoutFailure() {
        //NOP
    }

    @Override
    public void onUserLogoutSuccessWithInvalidAccessToken() {
        //NOP
    }
}