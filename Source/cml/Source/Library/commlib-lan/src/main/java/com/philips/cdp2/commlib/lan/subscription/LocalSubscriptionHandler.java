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

    private Set<SubscriptionEventListener> subscriptionEventListeners;
    private UdpEventReceiver udpEventReceiver;
    private NetworkNode networkNode;
    private final DISecurity diSecurity;

    public LocalSubscriptionHandler(DISecurity diSecurity, UdpEventReceiver udpEventReceiver) {
        this.diSecurity = diSecurity;
        this.udpEventReceiver = udpEventReceiver;
    }

    @Override
    public void enableSubscription(@NonNull NetworkNode networkNode, @NonNull Set<SubscriptionEventListener> subscriptionEventListeners) {
        DICommLog.i(DICommLog.LOCAL_SUBSCRIPTION, "Enabling local subscription (start udp)");

        this.networkNode = networkNode;
        this.subscriptionEventListeners = subscriptionEventListeners;

        udpEventReceiver.startReceivingEvents(this);
    }

    @Override
    public void disableSubscription() {
        DICommLog.i(DICommLog.LOCAL_SUBSCRIPTION, "Disabling local subscription (stop udp)");

        subscriptionEventListeners = null;
        udpEventReceiver.stopReceivingEvents(this);
    }

    @Override
    public void onUDPEventReceived(String data, String portName, String fromIp) {
        if (subscriptionEventListeners == null || subscriptionEventListeners.isEmpty()) {
            return;
        }

        if (data == null || data.isEmpty()) {
            return;
        }

        if (fromIp == null || fromIp.isEmpty()) {
            return;
        }

        if (networkNode.getIpAddress() == null || !networkNode.getIpAddress().equals(fromIp)) {
            DICommLog.d(DICommLog.LOCAL_SUBSCRIPTION, "Ignoring event, not from associated network node (" + fromIp + ")");
            return;
        }

        DICommLog.i(DICommLog.LOCAL_SUBSCRIPTION, "UDP event received from " + fromIp);

        String decryptedData;

        if (diSecurity == null) {
            decryptedData = data;
        } else {
            decryptedData = diSecurity.decryptData(data);
        }

        if (decryptedData == null) {
            DICommLog.w(DICommLog.LOCAL_SUBSCRIPTION, "Unable to decrypt data for: " + networkNode.getIpAddress());

            postSubscriptionEventDecryptionFailureOnUiThread(portName, subscriptionEventListeners);
        } else {
            DICommLog.d(DICommLog.LOCAL_SUBSCRIPTION, decryptedData);

            postSubscriptionEventOnUiThread(portName, decryptedData, subscriptionEventListeners);
        }
    }
}
