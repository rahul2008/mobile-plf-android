/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.ecs.demouapp.ui.networkEssential;

import android.content.Context;

import com.android.volley.toolbox.HurlStack;
import com.ecs.demouapp.ui.integration.ECSDependencies;
import com.ecs.demouapp.ui.integration.ECSSettings;
import com.ecs.demouapp.ui.session.OAuthListener;
import com.ecs.demouapp.ui.store.LocalStore;
import com.ecs.demouapp.ui.store.StoreListener;


public class LocalNetworkEssentials implements NetworkEssentials {

    @Override
    public StoreListener getStore(final Context context, final ECSSettings iapSettings, final ECSDependencies ECSDependencies) {
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
