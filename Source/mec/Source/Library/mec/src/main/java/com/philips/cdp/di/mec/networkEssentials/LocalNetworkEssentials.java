/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.mec.networkEssentials;

import android.content.Context;

import com.android.volley.toolbox.HurlStack;
import com.philips.cdp.di.mec.integration.MECDependencies;
import com.philips.cdp.di.mec.integration.MECSettings;
import com.philips.cdp.di.mec.session.OAuthListener;
import com.philips.cdp.di.mec.store.LocalStore;
import com.philips.cdp.di.mec.store.StoreListener;

public class LocalNetworkEssentials implements NetworkEssentials {

    @Override
    public StoreListener getStore(final Context context, final MECSettings iapSettings, final MECDependencies iapDependencies) {
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
