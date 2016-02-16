/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp.di.iap.session;

import android.content.Context;

import com.philips.cdp.di.iap.model.AbstractModel;
import com.philips.cdp.di.iap.store.Store;

public class HybrisDelegate {

    private static HybrisDelegate delegate = new HybrisDelegate();
    private OAuthHandler oAuthHandler;

    private NetworkController controller;
    private Context mContext;

    private HybrisDelegate() {
        oAuthHandler = new TestEnvOAuthHandler();
    }

    public NetworkController getNetworkController(Context context) {
        if(controller == null) {
            controller = new NetworkController(context, delegate.oAuthHandler);
        }
        return controller;
    }

    public static HybrisDelegate getInstance(Context context) {
        if (delegate.controller == null) {
            delegate.mContext = context;
            delegate.controller = delegate.getNetworkController(context);
        }
        return delegate;
    }

    private static int getCartItemCount(Context context, final String janRainID, final String userID) {
        return 0;
    }

    public void sendRequest(int requestCode, AbstractModel model, final RequestListener
            requestListener) {
        getNetworkController(mContext).sendHybrisRequest(requestCode, model, requestListener);
    }
    public void initStore(Context context, final String userName, final String janRainID) {
        controller.initStore(context, userName, janRainID);
    }

    public Store getStore() {
        return controller.getStore();
    }
}