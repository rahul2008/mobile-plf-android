/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.store;

import android.content.Context;

import com.philips.cdp.registration.User;
import com.philips.cdp.registration.handlers.RefreshLoginSessionHandler;

public class IAPUser {

    private Object mLock = new Object();
    private User mJanRainUser;
    private Context mContext;
    private Store mStore;

    boolean debug = true;
    public IAPUser(final Context context, final Store store) {
        mContext = context;
        mStore = store;
        mJanRainUser = new User(context);
    }

    public String getJanRainID() {
        if(debug) {
            debug = false;
            return "12";
        }
        return mJanRainUser.getAccessToken();
    }

    public String getJanRainEmail() {
        return mJanRainUser.getUserInstance(mContext).getEmail();
    }

    public void refreshLoginSession() {
        mJanRainUser.refreshLoginSession(new RefreshLoginSessionHandler() {
            @Override
            public void onRefreshLoginSessionSuccess() {
                mStore.updateJanRainIDBasedUrls();
                notifyOAuthHandler();
            }

            @Override
            public void onRefreshLoginSessionFailedWithError(final int i) {
                mStore.updateJanRainIDBasedUrls();
                notifyOAuthHandler();
            }
        }, mContext);
        putOAuthOnWait();
    }

    private  void putOAuthOnWait() {
        synchronized (mLock) {
            try {
                mLock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    private void notifyOAuthHandler() {
        synchronized (mLock) {
            mLock.notifyAll();
        }
    }
}