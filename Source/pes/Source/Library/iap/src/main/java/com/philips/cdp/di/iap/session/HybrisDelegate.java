/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.session;

import android.content.Context;

import com.philips.cdp.di.iap.integration.IAPDependencies;
import com.philips.cdp.di.iap.integration.IAPSettings;
import com.philips.cdp.di.iap.model.AbstractModel;
import com.philips.cdp.di.iap.networkEssential.NetworkEssentials;
import com.philips.cdp.di.iap.store.StoreListener;

public class HybrisDelegate {

    private static HybrisDelegate delegate = new HybrisDelegate();
    protected NetworkController controller;

    private HybrisDelegate() {
    }

    public static HybrisDelegate getInstance(Context context) {
        if (delegate.controller == null) {
            delegate.controller = delegate.getNetworkController(context);
        }
        return delegate;
    }

    public static HybrisDelegate getInstance() {
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

    public static HybrisDelegate getDelegateWithNetworkEssentials(NetworkEssentials networkEssentials,
                                                                  IAPSettings iapSettings, IAPDependencies iapDependencies) {
        delegate.controller = delegate.getNetworkController(iapSettings.getContext());
        delegate.controller.setIapSettings(iapSettings);
        delegate.controller.setmIapDependencies(iapDependencies);
        delegate.controller.setNetworkEssentials(networkEssentials);
        return delegate;
    }

    public void sendRequest(int requestCode, AbstractModel model, final RequestListener
            requestListener) {
        controller.sendHybrisRequest(requestCode, model, requestListener);
    }

    public StoreListener getStore() {
        return controller.getStore();
    }
}