/*
 * © Koninklijke Philips N.V., 2015, 2016.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.subscription;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.security.DISecurity;
import com.philips.cdp.dicommclient.util.DICommLog;

public class LocalSubscriptionHandler extends SubscriptionHandler implements UdpEventListener{

	private SubscriptionEventListener mSubscriptionEventListener;
	private UdpEventReceiver mUdpEventReceiver;
	private NetworkNode mNetworkNode;
	private final DISecurity mDISecurity;

	public LocalSubscriptionHandler(DISecurity diSecurity, UdpEventReceiver udpEventReceiver){
		mDISecurity = diSecurity;
		mUdpEventReceiver = udpEventReceiver;
	}

	@Override
	public void enableSubscription(NetworkNode networkNode, SubscriptionEventListener subscriptionEventListener) {
		DICommLog.i(DICommLog.LOCAL_SUBSCRIPTION, "Enabling local subscription (start udp)");
		mNetworkNode = networkNode;
		mSubscriptionEventListener = subscriptionEventListener;
		mUdpEventReceiver.startReceivingEvents(this);
	}

	@Override
	public void disableSubscription() {
		DICommLog.i(DICommLog.LOCAL_SUBSCRIPTION, "Disabling local subscription (stop udp)");
		mSubscriptionEventListener = null;
		mUdpEventReceiver.stopReceivingEvents(this);
	}

	@Override
	public void onUDPEventReceived(String data, String fromIp) {
		if (data == null || data.isEmpty())
			return;
		if (fromIp == null || fromIp.isEmpty())
			return;

		if (mNetworkNode.getIpAddress() == null || !mNetworkNode.getIpAddress().equals(fromIp)) {
			DICommLog.d(DICommLog.LOCAL_SUBSCRIPTION, "Ignoring event, not from associated network node (" + fromIp + ")");
			return;
		}


		DICommLog.i(DICommLog.LOCAL_SUBSCRIPTION, "UDP event received from " + fromIp);

		if(mSubscriptionEventListener!=null){
			String decryptedData = decryptData(data) ;
			if (decryptedData == null ) {
				DICommLog.d(DICommLog.LOCAL_SUBSCRIPTION, "Unable to decrypt data for : " + mNetworkNode.getIpAddress());
				return;
			}

			DICommLog.d(DICommLog.LOCAL_SUBSCRIPTION, decryptedData);
			if (mSubscriptionEventListener != null) {
				postSubscriptionEventOnUIThread(decryptedData, mSubscriptionEventListener);
			}
		}
	}

	private String decryptData(String cypher) {
        if (mDISecurity != null) {
            return mDISecurity.decryptData(cypher);
        }
        return cypher;
    }

}
