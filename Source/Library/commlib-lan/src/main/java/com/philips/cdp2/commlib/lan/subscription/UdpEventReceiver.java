/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

/*
 * © Koninklijke Philips N.V., 2015.
 *   All rights reserved.
 */

package com.philips.cdp2.commlib.lan.subscription;

import com.philips.cdp.dicommclient.util.DICommLog;

import java.util.Locale;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class UdpEventReceiver {

    private static UdpEventReceiver INSTANCE;

    private final UdpEventListener udpEventListener;
    private final Set<UdpEventListener> udpEventListeners = new CopyOnWriteArraySet<>();
    private UdpReceivingThread udpReceivingThread;

    public synchronized static UdpEventReceiver getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UdpEventReceiver();
        }
        return INSTANCE;
    }

    private UdpEventReceiver() {
        udpEventListener = new UdpEventListener() {
            @Override
            public void onUDPEventReceived(String data, String fromIp) {
                notifyAllEventListeners(data, fromIp);
            }
        };
    }

    public void startReceivingEvents(UdpEventListener udpEventListener) {
        startUdpThreadIfNecessary();
        addUdpEventListener(udpEventListener);
    }

    public void stopReceivingEvents(UdpEventListener udpEventListener) {
        removeUdpEventListener(udpEventListener);
        stopUdpThreadIfNecessary();
    }

    private synchronized void startUdpThreadIfNecessary() {
        if (udpReceivingThread != null) return;

        DICommLog.i(DICommLog.UDPRECEIVER, "Starting new Thread to receive UDP events");
        udpReceivingThread = new UdpReceivingThread(udpEventListener);
        udpReceivingThread.start();
    }

    private synchronized void stopUdpThreadIfNecessary() {
        if (shouldStopUdpThread()) {
            udpReceivingThread.stopThread();
            udpReceivingThread = null;
            DICommLog.i(DICommLog.UDPRECEIVER, "Stopped Thread to receive UDP events");
        }
    }

    private boolean shouldStopUdpThread() {
        return udpReceivingThread != null && udpEventListeners.isEmpty();
    }

    private void addUdpEventListener(UdpEventListener udpEventListener) {
        if (udpEventListeners.add(udpEventListener)) {
            DICommLog.i(DICommLog.UDPRECEIVER, "Added new listener to set");
        }
    }

    public void removeUdpEventListener(UdpEventListener udpEventListener) {
        if (udpEventListeners.remove(udpEventListener)) {
            DICommLog.i(DICommLog.UDPRECEIVER, "Removed listener from set");
        }
    }

    private void notifyAllEventListeners(String data, String fromIp) {
        DICommLog.d(DICommLog.UDPRECEIVER, String.format(Locale.US, "Notifying %d listeners of UDP event", udpEventListeners.size()));

        for (UdpEventListener listener : udpEventListeners) {
            listener.onUDPEventReceived(data, fromIp);
        }
    }

}
