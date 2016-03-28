/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.store;

import android.content.Context;

import com.philips.cdp.di.iap.utils.IAPLog;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.handlers.RefreshLoginSessionHandler;

import java.util.concurrent.Semaphore;

public class IAPUser {
    private final static String TAG = IAPUser.class.getSimpleName();

    Semaphore mSemaphore = new Semaphore(0);
    private User mJanRainUser;
    private Store mStore;
    private boolean mTokenRefreshSuccessful;

    public IAPUser(final Context context, final Store store) {
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
                IAPLog.d(TAG, " refreshLoginSuccessful failed with error=" +i);
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
}