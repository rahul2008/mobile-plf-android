/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.hybris;

import android.content.Context;

import com.android.volley.toolbox.HurlStack;
import com.philips.cdp.di.iap.core.NetworkEssentials;
import com.philips.cdp.di.iap.store.StoreListener;
import com.philips.cdp.di.iap.integration.IAPSettings;
import com.philips.cdp.di.iap.session.IAPHurlStack;
import com.philips.cdp.di.iap.session.OAuthListener;
import com.philips.cdp.di.iap.session.OAuthController;
import com.philips.cdp.di.iap.store.HybrisStore;

public class HybrisNetworkEssentials implements NetworkEssentials {
    @Override
    public StoreListener getStore(final Context context, IAPSettings iapSettings) {
        return new HybrisStore(context, iapSettings);
    }

    @Override
    public HurlStack getHurlStack(Context context, OAuthListener oAuthHandler) {
        IAPHurlStack iapHurlStack = new IAPHurlStack(oAuthHandler);
        iapHurlStack.setContext(context);
        return (iapHurlStack.getHurlStack());
    }

    @Override
    public OAuthListener getOAuthHandler() {
        return new OAuthController();
    }
}
