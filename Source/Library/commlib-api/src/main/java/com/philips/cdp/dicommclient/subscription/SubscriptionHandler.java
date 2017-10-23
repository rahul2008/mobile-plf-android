/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.dicommclient.subscription;

import android.support.annotation.VisibleForTesting;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.util.WrappedHandler;
import com.philips.cdp2.commlib.core.util.HandlerProvider;

import java.util.Set;

public abstract class SubscriptionHandler {

    private WrappedHandler mSubscriptionEventResponseHandler;

    public abstract void enableSubscription(NetworkNode networkNode, Set<SubscriptionEventListener> subscriptionEventListeners);

    public abstract void disableSubscription();

    protected void postSubscriptionEventOnUiThread(final String portName, final String decryptedData, final Set<SubscriptionEventListener> subscriptionEventListeners) {
        mSubscriptionEventResponseHandler = createSubscriptionEventResponseHandler();

        Runnable responseRunnable = new Runnable() {
            @Override
            public void run() {
                for (SubscriptionEventListener subscriptionEventListener : subscriptionEventListeners) {
                    subscriptionEventListener.onSubscriptionEventReceived(portName, decryptedData);
                }
            }
        };

        mSubscriptionEventResponseHandler.post(responseRunnable);
    }

    protected void postSubscriptionEventDecryptionFailureOnUiThread(final String portName, final Set<SubscriptionEventListener> subscriptionEventListeners) {
        mSubscriptionEventResponseHandler = createSubscriptionEventResponseHandler();

        Runnable decryptionFailureRunnable = new Runnable() {
            @Override
            public void run() {
                for (SubscriptionEventListener subscriptionEventListener : subscriptionEventListeners) {
                    subscriptionEventListener.onSubscriptionEventDecryptionFailed(portName);
                }
            }
        };

        mSubscriptionEventResponseHandler.post(decryptionFailureRunnable);
    }

    @VisibleForTesting
    protected WrappedHandler createSubscriptionEventResponseHandler() {
        if (mSubscriptionEventResponseHandler == null) {
            mSubscriptionEventResponseHandler = new WrappedHandler(HandlerProvider.createHandler());
        }
        return mSubscriptionEventResponseHandler;
    }

}
