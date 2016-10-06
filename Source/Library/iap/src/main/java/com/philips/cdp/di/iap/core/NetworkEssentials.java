/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.core;

import android.content.Context;

import com.android.volley.toolbox.HurlStack;
import com.philips.cdp.di.iap.integration.IAPSettings;
import com.philips.cdp.di.iap.session.OAuthListener;

public interface NetworkEssentials {
    StoreSpec getStore(Context context, IAPSettings iapSettings);
    HurlStack getHurlStack(Context context, OAuthListener oAuthHandler);
    OAuthListener getOAuthHandler();
}
