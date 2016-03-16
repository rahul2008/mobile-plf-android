/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.store;

import android.content.Context;

import com.philips.cdp.registration.User;

public class IAPUser {
    private User mJanRainUser;
    private Context mContext;

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
}