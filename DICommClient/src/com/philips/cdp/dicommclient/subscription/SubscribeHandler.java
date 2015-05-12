package com.philips.cdp.dicommclient.subscription;

import android.os.Handler;
import android.os.Looper;

import com.philips.cdp.dicomm.util.ALog;
import com.philips.cdp.dicomm.util.WrappedHandler;
import com.philips.cdp.dicommclient.networknode.NetworkNode;

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
	        	ALog.d(ALog.REQUESTQUEUE, "Processing response from request");
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
