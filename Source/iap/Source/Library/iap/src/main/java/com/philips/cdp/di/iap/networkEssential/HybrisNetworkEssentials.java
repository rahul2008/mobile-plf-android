/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.networkEssential;

import android.content.Context;

import com.android.volley.toolbox.HurlStack;
import com.philips.cdp.di.iap.integration.IAPSettings;
import com.philips.cdp.di.iap.session.IAPHurlStack;
import com.philips.cdp.di.iap.session.OAuthController;
import com.philips.cdp.di.iap.session.OAuthListener;
import com.philips.cdp.di.iap.store.HybrisStore;
import com.philips.cdp.di.iap.store.StoreListener;
import com.philips.platform.pif.DataInterface.USR.UserDataInterface;

public class HybrisNetworkEssentials implements NetworkEssentials {

    private UserDataInterface mUserDataInterface;
    public HybrisNetworkEssentials(UserDataInterface userDataInterface) {
        mUserDataInterface = userDataInterface;
    }

    @Override
    public StoreListener getStore(final Context context, IAPSettings iapSettings) {
        return new HybrisStore(context, iapSettings,mUserDataInterface);
    }

    @Override
    public HurlStack getHurlStack(Context context, OAuthListener oAuthHandler) {
        IAPHurlStack iapHurlStack = new IAPHurlStack(oAuthHandler);
        return (iapHurlStack.getHurlStack());
    }

    @Override
    public OAuthListener getOAuthHandler() {
        return new OAuthController();
    }
}
