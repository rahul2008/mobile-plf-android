/*
 * © Koninklijke Philips N.V., 2015.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.subscription;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.cdp.dicommclient.util.WrappedHandler;
import com.philips.cdp2.commlib.core.util.HandlerProvider;

import java.util.Set;

public abstract class SubscriptionHandler {

    private Set<SubscriptionEventListener> mSubscriptionEventListeners;
    private WrappedHandler mSubscriptionEventResponseHandler;

    public abstract void enableSubscription(NetworkNode networkNode, Set<SubscriptionEventListener> subscriptionEventListeners);

    public abstract void disableSubscription();

    protected void postSubscriptionEventOnUIThread(final String portName, final String decryptedData, Set<SubscriptionEventListener> subscriptionEventListeners) {

        mSubscriptionEventListeners = subscriptionEventListeners;
        mSubscriptionEventResponseHandler = getSubscriptionEventResponseHandler();

        Runnable responseRunnable = new Runnable() {
            @Override
            public void run() {
                DICommLog.d(DICommLog.REQUESTQUEUE, "Processing response from request");
                for (SubscriptionEventListener subscriptionEventListener : mSubscriptionEventListeners) {
                    subscriptionEventListener.onSubscriptionEventReceived(portName, decryptedData);
                }
            }
        };

        mSubscriptionEventResponseHandler.post(responseRunnable);
    }

    protected WrappedHandler getSubscriptionEventResponseHandler() {
        if (mSubscriptionEventResponseHandler == null) {
            mSubscriptionEventResponseHandler = new WrappedHandler(HandlerProvider.createHandler());
        }
        return mSubscriptionEventResponseHandler;
    }

}
