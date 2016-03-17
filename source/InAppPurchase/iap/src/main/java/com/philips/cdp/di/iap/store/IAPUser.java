/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.store;

import android.content.Context;

import com.philips.cdp.registration.User;
import com.philips.cdp.registration.handlers.RefreshUserHandler;

public class IAPUser {
    private User mJanRainUser;
    private Context mContext;

    public interface TokenRefreshCallBack {
        void onTokenRefresh(String newToken);
    }

    public IAPUser(Context context) {
        mContext = context;
        mJanRainUser = new User(context);
    }

    public String getJanRainID() {
        return mJanRainUser.getAccessToken();
    }

    public String getJanRainEmail() {
        return mJanRainUser.getUserInstance(mContext).getEmail();
    }

    public void refreshUser(final TokenRefreshCallBack callBack) {
        mJanRainUser.refreshUser(mContext, new RefreshUserHandler() {
            @Override
            public void onRefreshUserSuccess() {
                if (callBack != null) {
                    callBack.onTokenRefresh(mJanRainUser.getAccessToken());
                }
            }

            @Override
            public void onRefreshUserFailed(final int i) {
                if (callBack != null) {
                    callBack.onTokenRefresh(mJanRainUser.getAccessToken());
                }
            }
        });
    }
}