/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp.di.iap.session;

import android.content.Context;

import com.philips.cdp.di.iap.core.NetworkEssentials;
import com.philips.cdp.di.iap.store.StoreListener;
import com.philips.cdp.di.iap.integration.IAPSettings;
import com.philips.cdp.di.iap.model.AbstractModel;

public class HybrisDelegate {

    private static HybrisDelegate delegate = new HybrisDelegate();

    protected NetworkController controller;
    protected Context mContext;

    private HybrisDelegate() {
    }

    public static HybrisDelegate getInstance(Context context) {
        if (delegate.controller == null) {
            delegate.mContext = context.getApplicationContext();
            delegate.controller = delegate.getNetworkController(context);
        }
        return delegate;
    }

    public static HybrisDelegate getInstance() {
        return delegate;
    }

    public static HybrisDelegate getDelegateWithNetworkEssentials(Context context,
                                                                  NetworkEssentials networkEssentials,
                                                                  IAPSettings iapSettings) {
        delegate.mContext = context;
        delegate.controller = new NetworkController(context, networkEssentials,iapSettings);
        return delegate;
    }

    public NetworkController getNetworkController(Context context) {
        if (controller == null) {
            controller = new NetworkController(context);
        }
        return controller;
    }

    public static NetworkController getNetworkController() {
        return delegate.controller;
    }

    public void sendRequest(int requestCode, AbstractModel model, final RequestListener
            requestListener) {
        controller.sendHybrisRequest(requestCode, model, requestListener);
    }

    public StoreListener getStore() {
        return controller.getStore();
    }
}