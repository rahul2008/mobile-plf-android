/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.lan.subscription;

import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.security.DISecurity;
import com.philips.cdp.dicommclient.subscription.SubscriptionEventListener;
import com.philips.cdp.dicommclient.subscription.SubscriptionHandler;
import com.philips.cdp.dicommclient.util.DICommLog;

import java.util.Set;

public class LocalSubscriptionHandler extends SubscriptionHandler implements UdpEventListener {

	private Set<SubscriptionEventListener> mSubscriptionEventListeners;
	private UdpEventReceiver mUdpEventReceiver;
	private NetworkNode mNetworkNode;
	private final DISecurity mDISecurity;

	public LocalSubscriptionHandler(DISecurity diSecurity, UdpEventReceiver udpEventReceiver){
		mDISecurity = diSecurity;
		mUdpEventReceiver = udpEventReceiver;
	}

	@Override
	public void enableSubscription(@NonNull NetworkNode networkNode, @NonNull Set<SubscriptionEventListener> subscriptionEventListeners) {
		DICommLog.i(DICommLog.LOCAL_SUBSCRIPTION, "Enabling local subscription (start udp)");
		mNetworkNode = networkNode;
		mSubscriptionEventListeners = subscriptionEventListeners;
		mUdpEventReceiver.startReceivingEvents(this);
	}

	@Override
	public void disableSubscription() {
		DICommLog.i(DICommLog.LOCAL_SUBSCRIPTION, "Disabling local subscription (stop udp)");
		mSubscriptionEventListeners = null;
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

		if (mSubscriptionEventListeners != null) {
			String decryptedData = decryptData(data) ;
			if (decryptedData == null ) {
				DICommLog.d(DICommLog.LOCAL_SUBSCRIPTION, "Unable to decrypt data for : " + mNetworkNode.getIpAddress());
				return;
			}

			DICommLog.d(DICommLog.LOCAL_SUBSCRIPTION, decryptedData);
			postSubscriptionEventOnUIThread(decryptedData, mSubscriptionEventListeners);
		}
	}

	private String decryptData(String cypher) {
        if (mDISecurity != null) {
            return mDISecurity.decryptData(cypher);
        }
        return cypher;
    }

}
