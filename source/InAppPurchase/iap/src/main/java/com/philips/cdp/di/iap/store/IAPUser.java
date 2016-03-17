/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.store;

import android.content.Context;

import com.philips.cdp.registration.User;
import com.philips.cdp.registration.handlers.RefreshLoginSessionHandler;
import com.philips.cdp.registration.handlers.RefreshUserHandler;

public class IAPUser {
    private User mJanRainUser;
    private Context mContext;
    private Store mStore;

    public interface TokenRefreshCallBack {
        void onTokenRefresh(boolean result);
    }

    public IAPUser(final Context context, final Store store) {
        mContext = context;
        mStore = store;
        mJanRainUser = new User(context);

    }

    public String getJanRainID() {
        return mJanRainUser.getAccessToken();
    }

    public String getJanRainEmail() {
        return mJanRainUser.getUserInstance(mContext).getEmail();
    }

    public void refreshUser(final TokenRefreshCallBack callBack) {
        mJanRainUser.refreshLoginSession(new RefreshLoginSessionHandler() {
            @Override
            public void onRefreshLoginSessionSuccess() {
                if (callBack != null) {
                    mStore.updateJanRainIDBasedUrls();
                    callBack.onTokenRefresh(true);
                }
            }

            @Override
            public void onRefreshLoginSessionFailedWithError(final int i) {
                if (callBack != null) {
                    mStore.updateJanRainIDBasedUrls();
                    callBack.onTokenRefresh(false);
                }
            }
        }, mContext);
        mJanRainUser.refreshUser(mContext, new RefreshUserHandler() {
            @Override
            public void onRefreshUserSuccess() {
                if (callBack != null) {
                    mStore.updateJanRainIDBasedUrls();
                    callBack.onTokenRefresh(true);
                }
            }

            @Override
            public void onRefreshUserFailed(final int i) {
                if (callBack != null) {
                    callBack.onTokenRefresh(false);
                }
            }
        });
    }
}