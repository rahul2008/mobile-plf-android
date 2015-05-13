package com.philips.cdp.dicommclient.subscription;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.security.DISecurity;
import com.philips.cdp.dicommclient.util.ALog;

public class LocalSubscriptionHandler extends SubscribeHandler implements UDPEventListener{

	private SubscriptionEventListener mSubscriptionEventListener;
	private NetworkNode mNetworkNode;
	private final DISecurity mDISecurity;

	public LocalSubscriptionHandler(DISecurity diSecurity){
		mDISecurity = diSecurity;
	}

	@Override
	public void enableSubscription(NetworkNode networkNode, SubscriptionEventListener subscriptionEventListener) {
		ALog.i(ALog.LOCAL_SUBSCRIPTION, "Enabling local subscription (start udp)");
		mNetworkNode = networkNode;
		mSubscriptionEventListener = subscriptionEventListener;

		UDPReceivingThread.getInstance().addUDPEventListener(this) ;
		if (! UDPReceivingThread.getInstance().isAlive()) {
			UDPReceivingThread.getInstance().start();
		}
	}

	@Override
	public void disableSubscription() {
		ALog.i(ALog.LOCAL_SUBSCRIPTION, "Disabling local subscription (stop udp)");
		mSubscriptionEventListener = null;
		UDPReceivingThread.getInstance().removeUDPEventListener(this);
		if (UDPReceivingThread.getInstance().isAlive()) {
			UDPReceivingThread.getInstance().stopUDPListener();
		}
	}

	@Override
	public void onUDPEventReceived(String data, String fromIp) {
		if (data == null || data.isEmpty())
			return;
		if (fromIp == null || fromIp.isEmpty())
			return;

		if (mNetworkNode.getIpAddress() == null || !mNetworkNode.getIpAddress().equals(fromIp)) {
			ALog.d(ALog.LOCAL_SUBSCRIPTION, "Ignoring event, not from associated network node (" + (fromIp == null? "null" : fromIp) + ")");
			return;
		}


		ALog.i(ALog.LOCAL_SUBSCRIPTION, "UDP event received from " + fromIp);

		if(mSubscriptionEventListener!=null){
			String decryptedData = decryptData(data) ;
			if (decryptedData == null ) {
				ALog.d(ALog.LOCAL_SUBSCRIPTION, "Unable to decrypt data for : " + mNetworkNode.getIpAddress());
				return;
			}

			ALog.d(ALog.LOCAL_SUBSCRIPTION, decryptedData);
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
