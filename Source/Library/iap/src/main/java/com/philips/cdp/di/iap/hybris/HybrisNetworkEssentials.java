/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.hybris;

import android.content.Context;

import com.android.volley.toolbox.HurlStack;
import com.philips.cdp.di.iap.core.NetworkEssentials;
import com.philips.cdp.di.iap.core.StoreSpec;
import com.philips.cdp.di.iap.session.IAPHurlStack;
import com.philips.cdp.di.iap.session.OAuthHandler;
import com.philips.cdp.di.iap.store.HybrisStore;

public class HybrisNetworkEssentials implements NetworkEssentials {
    @Override
    public StoreSpec getStore(final Context context) {
        return new HybrisStore(context);
    }

    @Override
    public HurlStack getHurlStack(OAuthHandler oAuthHandler) {
        return (new IAPHurlStack(oAuthHandler).getHurlStack());
    }

    @Override
    public OAuthHandler getOAuthHandler() {
        return null;
    }
}
