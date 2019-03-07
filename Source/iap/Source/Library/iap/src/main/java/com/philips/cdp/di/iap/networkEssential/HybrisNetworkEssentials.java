/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.networkEssential;

import android.content.Context;

import com.android.volley.toolbox.HurlStack;
import com.philips.cdp.di.iap.integration.IAPDependencies;
import com.philips.cdp.di.iap.integration.IAPSettings;
import com.philips.cdp.di.iap.session.IAPHurlStack;
import com.philips.cdp.di.iap.session.OAuthController;
import com.philips.cdp.di.iap.session.OAuthListener;
import com.philips.cdp.di.iap.store.HybrisStore;
import com.philips.cdp.di.iap.store.StoreListener;

public class HybrisNetworkEssentials implements NetworkEssentials {
    @Override
    public StoreListener getStore(final Context context, IAPSettings iapSettings, IAPDependencies iapDependencies) {
        return new HybrisStore(context, iapSettings,iapDependencies);
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
