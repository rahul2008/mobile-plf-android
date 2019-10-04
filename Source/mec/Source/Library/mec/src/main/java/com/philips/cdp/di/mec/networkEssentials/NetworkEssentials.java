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
import com.philips.cdp.di.mec.store.StoreListener;

public interface NetworkEssentials {
    StoreListener getStore(Context context, MECSettings iapSettings, MECDependencies iapDependencies);
    HurlStack getHurlStack(Context context, OAuthListener oAuthHandler);
    OAuthListener getOAuthHandler();
}
