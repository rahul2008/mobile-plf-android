package com.philips.cdp.dicommclient.subscription;


import com.philips.cdp.dicommclient.cpp.CppController;
import com.philips.cdp.dicommclient.cpp.listener.DcsEventListener;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.util.DLog;

public class RemoteSubscriptionHandler extends SubscribeHandler implements DcsEventListener {

	private SubscriptionEventListener mSubscriptionEventListener;
	private NetworkNode mNetworkNode;
	private CppController mCppController;

	public RemoteSubscriptionHandler(CppController cppController){
		mCppController = cppController;
	}

	@Override
	public void enableSubscription(NetworkNode networkNode, SubscriptionEventListener subscriptionEventListener) {
		DLog.i(DLog.REMOTE_SUBSCRIPTION, "Enabling remote subscription (start dcs)");
		mNetworkNode = networkNode;
		mSubscriptionEventListener = subscriptionEventListener;
		//DI-Comm change. Moved from Constructor
		mCppController.addDCSEventListener(networkNode.getCppId(), this);
		mCppController.startDCSService();
	}

	@Override
	public void disableSubscription() {
		DLog.i(DLog.REMOTE_SUBSCRIPTION, "Disabling remote subscription (stop dcs)");
		mSubscriptionEventListener = null;
		//DI-Comm change. Removing the listener on Disabling remote subscription
		if (mNetworkNode != null) {
			mCppController.removeDCSEventListener(mNetworkNode.getCppId());
		}
		mCppController.stopDCSService();
	}

	@Override
	public void onDCSEventReceived(String data, String fromEui64, String action) {
		DLog.i(DLog.REMOTE_SUBSCRIPTION,"onDCSEventReceived: "+data);
		if (data == null || data.isEmpty())
			return;

		if (fromEui64 == null || fromEui64.isEmpty())
			return;

		if (!mNetworkNode.getCppId().equals(fromEui64)) {
			DLog.d(DLog.REMOTE_SUBSCRIPTION, "Ignoring event, not from associated network node (" + (fromEui64 == null? "null" : fromEui64) + ")");
			return;
		}

		DLog.i(DLog.REMOTE_SUBSCRIPTION, "DCS event received from " + fromEui64);
		DLog.i(DLog.REMOTE_SUBSCRIPTION, data);
		if (mSubscriptionEventListener != null) {
			postSubscriptionEventOnUIThread(data, mSubscriptionEventListener);
		}
	}
}
