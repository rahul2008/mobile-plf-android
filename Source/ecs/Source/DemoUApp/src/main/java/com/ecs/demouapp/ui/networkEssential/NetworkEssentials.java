/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.ecs.demouapp.ui.networkEssential;

import android.content.Context;

import com.android.volley.toolbox.HurlStack;
import com.ecs.demouapp.ui.integration.IAPDependencies;
import com.ecs.demouapp.ui.integration.IAPSettings;
import com.ecs.demouapp.ui.session.OAuthListener;
import com.ecs.demouapp.ui.store.StoreListener;


public interface NetworkEssentials {
    StoreListener getStore(Context context, IAPSettings iapSettings, IAPDependencies iapDependencies);
    HurlStack getHurlStack(Context context, OAuthListener oAuthHandler);
    OAuthListener getOAuthHandler();
}
