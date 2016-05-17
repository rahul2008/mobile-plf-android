/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.core;

import android.content.Context;

import com.android.volley.toolbox.HurlStack;
import com.philips.cdp.di.iap.session.OAuthHandler;

public interface NetworkEssentials {
    StoreSpec getStore(Context context);
    HurlStack getHurlStack(Context context, OAuthHandler oAuthHandler);
    OAuthHandler getOAuthHandler();
}
