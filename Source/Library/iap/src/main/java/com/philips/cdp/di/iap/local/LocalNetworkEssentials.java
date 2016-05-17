package com.philips.cdp.di.iap.local;

import android.content.Context;

import com.android.volley.toolbox.HurlStack;
import com.philips.cdp.di.iap.applocal.AppLocalStore;
import com.philips.cdp.di.iap.core.NetworkEssentials;
import com.philips.cdp.di.iap.core.StoreSpec;
import com.philips.cdp.di.iap.session.OAuthHandler;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class LocalNetworkEssentials implements NetworkEssentials {

    @Override
    public StoreSpec getStore(final Context context) {
        return new AppLocalStore(context);
    }

    @Override
    public HurlStack getHurlStack(final OAuthHandler oAuthHandler) {
        return new HurlStack();
    }

    @Override
    public OAuthHandler getOAuthHandler() {
        return null;
    }
}
