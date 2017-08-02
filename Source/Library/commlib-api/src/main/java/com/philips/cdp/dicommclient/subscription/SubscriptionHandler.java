/*
 * © Koninklijke Philips N.V., 2015.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.subscription;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.cdp.dicommclient.util.WrappedHandler;
import com.philips.cdp2.commlib.core.util.HandlerProvider;

public abstract class SubscriptionHandler {

    private SubscriptionEventListener mSubscriptionEventListener;
    private WrappedHandler mSubscriptionEventResponseHandler;

    public abstract void enableSubscription(NetworkNode networkNode, SubscriptionEventListener subscriptionEventListener);
	public abstract void disableSubscription();

	protected void postSubscriptionEventOnUIThread(final String decryptedData, SubscriptionEventListener subscriptionEventListener) {

		mSubscriptionEventListener = subscriptionEventListener;
		mSubscriptionEventResponseHandler = getSubscriptionEventResponseHandler();

		Runnable responseRunnable = new Runnable() {
	        @Override
	        public void run() {
	        	DICommLog.d(DICommLog.REQUESTQUEUE, "Processing response from request");
	        	mSubscriptionEventListener.onSubscriptionEventReceived(decryptedData);
	    }};

        mSubscriptionEventResponseHandler.post(responseRunnable);
    }


	protected WrappedHandler getSubscriptionEventResponseHandler() {
        if(mSubscriptionEventResponseHandler==null){
        	mSubscriptionEventResponseHandler = new WrappedHandler(HandlerProvider.createHandler());
        }
        return mSubscriptionEventResponseHandler;
    }

}
