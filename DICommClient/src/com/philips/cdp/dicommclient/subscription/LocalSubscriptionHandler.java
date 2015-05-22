package com.philips.cdp.dicommclient.subscription;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.security.DISecurity;
import com.philips.cdp.dicommclient.util.DLog;

public class LocalSubscriptionHandler extends SubscribeHandler implements UDPEventListener{

	private SubscriptionEventListener mSubscriptionEventListener;
	private UDPReceivingThread mUDPReceivingThread;
	private NetworkNode mNetworkNode;
	private final DISecurity mDISecurity;

	public LocalSubscriptionHandler(DISecurity diSecurity, UDPReceivingThread udpReceivingThread){
		mDISecurity = diSecurity;
		mUDPReceivingThread = udpReceivingThread;
	}

	@Override
	public void enableSubscription(NetworkNode networkNode, SubscriptionEventListener subscriptionEventListener) {
		DLog.i(DLog.LOCAL_SUBSCRIPTION, "Enabling local subscription (start udp)");
		mNetworkNode = networkNode;
		mSubscriptionEventListener = subscriptionEventListener;

		mUDPReceivingThread.addUDPEventListener(this) ;
		if (! mUDPReceivingThread.isAlive()) {
			mUDPReceivingThread.start();
		}
	}

	@Override
	public void disableSubscription() {
		DLog.i(DLog.LOCAL_SUBSCRIPTION, "Disabling local subscription (stop udp)");
		mSubscriptionEventListener = null;
		mUDPReceivingThread.removeUDPEventListener(this);
		if (mUDPReceivingThread.isAlive()) {
			mUDPReceivingThread.stopUDPListener();
		}
	}

	@Override
	public void onUDPEventReceived(String data, String fromIp) {
		if (data == null || data.isEmpty())
			return;
		if (fromIp == null || fromIp.isEmpty())
			return;

		if (mNetworkNode.getIpAddress() == null || !mNetworkNode.getIpAddress().equals(fromIp)) {
			DLog.d(DLog.LOCAL_SUBSCRIPTION, "Ignoring event, not from associated network node (" + (fromIp == null? "null" : fromIp) + ")");
			return;
		}


		DLog.i(DLog.LOCAL_SUBSCRIPTION, "UDP event received from " + fromIp);

		if(mSubscriptionEventListener!=null){
			String decryptedData = decryptData(data) ;
			if (decryptedData == null ) {
				DLog.d(DLog.LOCAL_SUBSCRIPTION, "Unable to decrypt data for : " + mNetworkNode.getIpAddress());
				return;
			}

			DLog.d(DLog.LOCAL_SUBSCRIPTION, decryptedData);
			if (mSubscriptionEventListener != null) {
				postSubscriptionEventOnUIThread(decryptedData, mSubscriptionEventListener);
			}
		}
	}

	private String decryptData(String cypher) {
        if (mDISecurity != null) {
            return mDISecurity.decryptData(cypher, mNetworkNode);
        }
        return cypher;
    }

}
