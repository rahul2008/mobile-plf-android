/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp.di.iap.session;

import android.content.Context;

public class HybrisDelegate {

    private static HybrisDelegate delegate = new HybrisDelegate();
    private static OAuthHandler oAuthHandler;

    private HybrisDelegate() {
        oAuthHandler = new TestEnvOAuthHandler();
    }

    static HybrisDelegate getInstance() {
        return delegate;
    }

    static int getCartItemCount(Context context,final String janRainID, final String userID) {
        return 0;
    }
}