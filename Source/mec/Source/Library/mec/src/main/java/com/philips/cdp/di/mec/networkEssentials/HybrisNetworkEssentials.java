/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.mec.networkEssentials;

import android.content.Context;

import com.android.volley.toolbox.HurlStack;
import com.philips.cdp.di.mec.integration.MECDependencies;
import com.philips.cdp.di.mec.integration.MECSettings;
import com.philips.cdp.di.mec.session.MECHurlStack;
import com.philips.cdp.di.mec.session.OAuthListener;
import com.philips.cdp.di.mec.store.HybrisStore;
import com.philips.cdp.di.mec.store.StoreListener;

public class HybrisNetworkEssentials implements NetworkEssentials {
    @Override
    public StoreListener getStore(final Context context, MECSettings iapSettings, MECDependencies iapDependencies) {
        return new HybrisStore(context, iapSettings,iapDependencies);
    }

    @Override
    public HurlStack getHurlStack(Context context, OAuthListener oAuthHandler) {
        MECHurlStack iapHurlStack = new MECHurlStack(oAuthHandler);
        return (iapHurlStack.getHurlStack());
    }

    @Override
    public OAuthListener getOAuthHandler() {
        return null;
    }


}
