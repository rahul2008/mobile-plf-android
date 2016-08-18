/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.applocal;

import android.content.Context;

import com.android.volley.toolbox.HurlStack;
import com.philips.cdp.di.iap.core.NetworkEssentials;
import com.philips.cdp.di.iap.core.StoreSpec;
import com.philips.cdp.di.iap.integration.IAPDependencies;
import com.philips.cdp.di.iap.session.OAuthHandler;

public class LocalNetworkEssentials implements NetworkEssentials {

    @Override
    public StoreSpec getStore(final Context context, final IAPDependencies iapDependencies) {
        return new AppLocalStore(context);
    }

    @Override
    public HurlStack getHurlStack(Context context, final OAuthHandler oAuthHandler) {
        return new HurlStack();
    }

    @Override
    public OAuthHandler getOAuthHandler() {
        return null;
    }
}
