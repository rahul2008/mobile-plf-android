/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.dicommclient.subscription;

import android.os.Handler;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp2.commlib.core.util.HandlerProvider;

import java.util.Set;

public abstract class SubscriptionHandler {

    private Handler mSubscriptionEventResponseHandler = HandlerProvider.createHandler();

    public abstract void enableSubscription(NetworkNode networkNode, Set<SubscriptionEventListener> subscriptionEventListeners);

    public abstract void disableSubscription();

    protected void postSubscriptionEventOnUiThread(final String portName, final String decryptedData, final Set<SubscriptionEventListener> subscriptionEventListeners) {
        mSubscriptionEventResponseHandler.post(new Runnable() {
            @Override
            public void run() {
                for (SubscriptionEventListener subscriptionEventListener : subscriptionEventListeners) {
                    subscriptionEventListener.onSubscriptionEventReceived(portName, decryptedData);
                }
            }
        });
    }

    protected void postSubscriptionEventDecryptionFailureOnUiThread(final String portName, final Set<SubscriptionEventListener> subscriptionEventListeners) {
        mSubscriptionEventResponseHandler.post(new Runnable() {
            @Override
            public void run() {
                for (SubscriptionEventListener subscriptionEventListener : subscriptionEventListeners) {
                    subscriptionEventListener.onSubscriptionEventDecryptionFailed(portName);
                }
            }
        });
    }
}
