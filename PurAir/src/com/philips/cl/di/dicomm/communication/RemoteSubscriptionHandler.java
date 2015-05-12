package com.philips.cl.di.dicomm.communication;


import com.philips.cdp.dicomm.cpp.CPPController;
import com.philips.cdp.dicomm.cpp.DCSEventListener;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cl.di.dev.pa.util.ALog;

public class RemoteSubscriptionHandler extends SubscribeHandler implements DCSEventListener {
	
	private SubscriptionEventListener mSubscriptionEventListener;
	private NetworkNode mNetworkNode;
	private CPPController mCppController;
	
	public RemoteSubscriptionHandler(){
		mCppController = CPPController.getInstance();
	}

	@Override
	public void enableSubscription(NetworkNode networkNode, SubscriptionEventListener subscriptionEventListener) {
		ALog.i(ALog.REMOTE_SUBSCRIPTION, "Enabling remote subscription (start dcs)");
		mNetworkNode = networkNode;
		mSubscriptionEventListener = subscriptionEventListener;
		//DI-Comm change. Moved from Constructor
		mCppController.addDCSEventListener(networkNode.getCppId(), this);
		mCppController.startDCSService();	
	}

	@Override
	public void disableSubscription() {
		ALog.i(ALog.REMOTE_SUBSCRIPTION, "Disabling remote subscription (stop dcs)");
		mSubscriptionEventListener = null;
		//DI-Comm change. Removing the listener on Disabling remote subscription
		if (mNetworkNode != null) {
			mCppController.removeDCSEventListener(mNetworkNode.getCppId());
		}
		mCppController.stopDCSService();
	}

	@Override
	public void onDCSEventReceived(String data, String fromEui64, String action) {
		ALog.i(ALog.REMOTE_SUBSCRIPTION,"onDCSEventReceived: "+data);
		if (data == null || data.isEmpty())
			return;

		if (fromEui64 == null || fromEui64.isEmpty())
			return;
		
		if (!mNetworkNode.getCppId().equals(fromEui64)) {
			ALog.d(ALog.REMOTE_SUBSCRIPTION, "Ignoring event, not from associated network node (" + (fromEui64 == null? "null" : fromEui64) + ")");
			return;
		}
		
		ALog.i(ALog.REMOTE_SUBSCRIPTION, "DCS event received from " + fromEui64);
		ALog.i(ALog.REMOTE_SUBSCRIPTION, data);
		if (mSubscriptionEventListener != null) {
			postSubscriptionEventOnUIThread(data, mSubscriptionEventListener);
		}
	}
}
