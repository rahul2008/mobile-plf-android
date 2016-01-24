/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp.di.iap.session;

public class HybrisDelegate {

    private static HybrisDelegate delegate = new HybrisDelegate();
    private OAuthHandler oAuthHandler;

    private HybrisDelegate() {
        oAuthHandler = new TestEnvOAuthHandler();
    }

    static HybrisDelegate getInstance() {
        return delegate;
    }

    static int getCartItemCount(final String janRainID, final String userID) {
        return 0;
    }
}