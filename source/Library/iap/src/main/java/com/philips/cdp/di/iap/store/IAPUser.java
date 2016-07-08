/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.store;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

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
    private HybrisStore mStore;
    private boolean mTokenRefreshSuccessful;

    //Track the locking. It should not lock if the call back was received earlier than locking
    private volatile boolean mLockReleaseRequested;

    public IAPUser(final Context context, final HybrisStore store) {
        RegistrationHelper.getInstance().registerUserRegistrationListener(this);
        mStore = store;
        mJanRainUser = new User(context);
    }

    public String getJanRainID() {
        return mJanRainUser.getAccessToken();
    }

    public String getJanRainEmail() {
//        Log.i("Janrain UUID", mJanRainUser.getJanrainUUID());
        return mJanRainUser.getEmail();
    }

    public void refreshLoginSession() {
        mTokenRefreshSuccessful = false;
        mLockReleaseRequested = false;
        IAPLog.d(TAG, " requesting refresh login session for user");
        mJanRainUser.refreshLoginSession(new RefreshLoginSessionHandler() {
            @Override
            public void onRefreshLoginSessionSuccess() {
                IAPLog.d(TAG, " refreshLoginSuccessful");
                mStore.updateJanRainIDBasedUrls();
                mTokenRefreshSuccessful = true;
                mLockReleaseRequested = true;
                unlockOAuthThread();
            }

            @Override
            public void onRefreshLoginSessionFailedWithError(final int i) {
                IAPLog.d(TAG, " refreshLoginSuccessful failed with error=" + i);
                mLockReleaseRequested = true;
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
        //We got the refresh call back earlier than request locking.
        if (mLockReleaseRequested)
            return;

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