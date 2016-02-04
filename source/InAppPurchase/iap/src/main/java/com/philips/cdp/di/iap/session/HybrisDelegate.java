/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp.di.iap.session;

import android.content.Context;
import android.os.Bundle;

public class HybrisDelegate {

    private static HybrisDelegate delegate = new HybrisDelegate();
    private static OAuthHandler oAuthHandler;

    private NetworkController controller;

    private HybrisDelegate() {
        oAuthHandler = new TestEnvOAuthHandler();
    }

    public static HybrisDelegate getInstance(Context context) {
        if(delegate.controller == null) {
            delegate.controller = new NetworkController(context);
        }
        return delegate;
    }

    private static int getCartItemCount(Context context,final String janRainID, final String userID) {
        return 0;
    }

    public void sendRequest(int requestCode, final RequestListener requestListener, Bundle bundle) {
        controller.sendHybrisRequest(requestCode, requestListener, bundle);
    }

    public void initStore(Context context, final String userName, final String janRainID) {
        controller.initStore(context, userName, janRainID);
    }
}