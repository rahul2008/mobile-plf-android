/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.mec.session;

import android.content.Context;

import com.philips.cdp.di.mec.integration.MECDependencies;
import com.philips.cdp.di.mec.integration.MECSettings;
import com.philips.cdp.di.mec.model.AbstractModel;
import com.philips.cdp.di.mec.networkEssentials.NetworkEssentials;
import com.philips.cdp.di.mec.store.StoreListener;


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
                                                                  MECSettings iapSettings, MECDependencies iapDependencies) {
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