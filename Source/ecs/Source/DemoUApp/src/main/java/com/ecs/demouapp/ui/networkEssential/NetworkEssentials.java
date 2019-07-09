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
import com.ecs.demouapp.ui.store.StoreListener;


public interface NetworkEssentials {
    StoreListener getStore(Context context, ECSSettings iapSettings, ECSDependencies ECSDependencies);
    HurlStack getHurlStack(Context context, OAuthListener oAuthHandler);
    OAuthListener getOAuthHandler();
}
