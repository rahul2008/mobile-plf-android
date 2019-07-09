/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.ecs.demouapp.ui.session;

import android.content.Context;

import com.ecs.demouapp.ui.integration.ECSDependencies;
import com.ecs.demouapp.ui.integration.ECSSettings;
import com.ecs.demouapp.ui.model.AbstractModel;
import com.ecs.demouapp.ui.networkEssential.NetworkEssentials;
import com.ecs.demouapp.ui.store.StoreListener;


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
                                                                  ECSSettings iapSettings, ECSDependencies ECSDependencies) {
        delegate.controller = delegate.getNetworkController(iapSettings.getContext());
        delegate.controller.setIapSettings(iapSettings);
        delegate.controller.setmECSDependencies(ECSDependencies);
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