/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.ecs.demouapp.ui.networkEssential;

import android.content.Context;

import com.android.volley.toolbox.HurlStack;
import com.ecs.demouapp.ui.integration.ECSDependencies;
import com.ecs.demouapp.ui.integration.ECSSettings;
import com.ecs.demouapp.ui.session.IAPHurlStack;
import com.ecs.demouapp.ui.session.OAuthController;
import com.ecs.demouapp.ui.session.OAuthListener;
import com.ecs.demouapp.ui.store.HybrisStore;
import com.ecs.demouapp.ui.store.StoreListener;


public class HybrisNetworkEssentials implements NetworkEssentials {
    @Override
    public StoreListener getStore(final Context context, ECSSettings iapSettings, ECSDependencies ECSDependencies) {
        return new HybrisStore(context, iapSettings, ECSDependencies);
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
