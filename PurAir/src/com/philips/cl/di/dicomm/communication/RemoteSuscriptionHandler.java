package com.philips.cl.di.dicomm.communication;

import android.content.Context;

import com.philips.cl.di.dev.pa.cpp.CPPController;
import com.philips.cl.di.dev.pa.cpp.DCSEventListener;
import com.philips.cl.di.dev.pa.newpurifier.NetworkNode;
import com.philips.cl.di.dev.pa.util.ALog;

public class RemoteSuscriptionHandler extends SubscribeHandler implements DCSEventListener {
	
	private SubscriptionEventListener mSubscriptionEventListener;
	private NetworkNode mNetworkNode;
	private CPPController mCppController;
	
	//TODO: DICOMM Refactor, if cppcontroller is available without context, then we need to remove this
	public RemoteSuscriptionHandler(Context context){
		mCppController = CPPController.getInstance(context);
	}
	
	public void registerSubscriptionListener(SubscriptionEventListener subscriptionEventListener){
		mSubscriptionEventListener = subscriptionEventListener;		
	}
	
	public void unRegisterSubscriptionListener(SubscriptionEventListener subscriptionEventListener){
		mSubscriptionEventListener = null;		
	}

	@Override
	public void enableSubscriptionFromAppliance(NetworkNode networkNode) {
		ALog.i(ALog.REMOTE_SUBSCRIPTION, "Enabling remote subscription (start dcs)");
		mNetworkNode = networkNode;
		//DI-Comm change. Moved from Constructor
		mCppController.addDCSEventListener(networkNode.getCppId(), this);
		mCppController.startDCSService();		
	}

	@Override
	public void disableSubscriptionFromAppliance() {
		ALog.i(ALog.REMOTE_SUBSCRIPTION, "Disabling remote subscription (stop dcs)");
		mCppController.stopDCSService();
		//DI-Comm change. Removing the listener on Disabling remote subscription
		mCppController.removeDCSListener(mNetworkNode.getCppId());
	}

	@Override
	public void onDCSEventReceived(String data, String fromEui64, String action) {
		ALog.i(ALog.REMOTE_SUBSCRIPTION,"onDCSEventReceived: "+data);
		if (data == null || data.isEmpty())
			return;

		if (fromEui64 == null || fromEui64.isEmpty())
			return;
		
		if (!mNetworkNode.getCppId().equals(fromEui64)) {
			ALog.d(ALog.SUBSCRIPTION, "Ignoring event, not from associated network node (" + (fromEui64 == null? "null" : fromEui64) + ")");
			return;
		}
		
		ALog.i(ALog.REMOTE_SUBSCRIPTION, "DCS event received from " + fromEui64);
		ALog.i(ALog.REMOTE_SUBSCRIPTION, data);
		if (mSubscriptionEventListener != null) {
			mSubscriptionEventListener.onSubscriptionEventReceived();
		}
	}
}
