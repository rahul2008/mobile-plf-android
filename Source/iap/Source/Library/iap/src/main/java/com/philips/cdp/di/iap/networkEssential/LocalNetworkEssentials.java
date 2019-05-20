/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.networkEssential;

import android.content.Context;

import com.android.volley.toolbox.HurlStack;
import com.philips.cdp.di.iap.integration.IAPDependencies;
import com.philips.cdp.di.iap.integration.IAPSettings;
import com.philips.cdp.di.iap.session.OAuthListener;
import com.philips.cdp.di.iap.store.LocalStore;
import com.philips.cdp.di.iap.store.StoreListener;

public class LocalNetworkEssentials implements NetworkEssentials {

    @Override
    public StoreListener getStore(final Context context, final IAPSettings iapSettings, final IAPDependencies iapDependencies) {
        return new LocalStore(context);
    }

    @Override
    public HurlStack getHurlStack(Context context, final OAuthListener oAuthHandler) {
        return new HurlStack();
    }

    @Override
    public OAuthListener getOAuthHandler() {
        return null;
    }
}
