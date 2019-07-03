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
import com.philips.cdp.di.iap.store.StoreListener;

public interface NetworkEssentials {
    StoreListener getStore(Context context, IAPSettings iapSettings, IAPDependencies iapDependencies);
    HurlStack getHurlStack(Context context, OAuthListener oAuthHandler);
    OAuthListener getOAuthHandler();
}
