/*
 * © Koninklijke Philips N.V., 2015.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.subscription;

import android.os.Handler;
import android.os.Looper;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.util.DLog;
import com.philips.cdp.dicommclient.util.WrappedHandler;

public abstract class SubscribeHandler {

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
	        	DLog.d(DLog.REQUESTQUEUE, "Processing response from request");
	        	mSubscriptionEventListener.onSubscriptionEventReceived(decryptedData);
	    }};

        mSubscriptionEventResponseHandler.post(responseRunnable);
    }


	protected WrappedHandler getSubscriptionEventResponseHandler() {
        if(mSubscriptionEventResponseHandler==null){
        	mSubscriptionEventResponseHandler = new WrappedHandler(new Handler(Looper.getMainLooper()));
        }
        return mSubscriptionEventResponseHandler;
    }

}
