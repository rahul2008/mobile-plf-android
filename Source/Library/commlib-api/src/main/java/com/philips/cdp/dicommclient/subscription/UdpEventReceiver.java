/*
 * © Koninklijke Philips N.V., 2015.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.subscription;

import com.philips.cdp.dicommclient.util.DICommLog;

import java.util.Locale;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class UdpEventReceiver {

    private static UdpEventReceiver mInstance;

    private final UdpEventListener mUdpEventListener;
    private final Set<UdpEventListener> udpEventListeners = new CopyOnWriteArraySet<>();
    private UdpReceivingThread mUdpReceivingThread;

    public synchronized static UdpEventReceiver getInstance() {
        if (mInstance == null) {
            mInstance = new UdpEventReceiver();
        }
        return mInstance;
    }

    private UdpEventReceiver() {
        mUdpEventListener = new UdpEventListener() {
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
        if (mUdpReceivingThread != null) return;

        DICommLog.i(DICommLog.UDPRECEIVER, "Starting new Thread to receive UDP events");
        mUdpReceivingThread = new UdpReceivingThread(mUdpEventListener);
        mUdpReceivingThread.start();
    }

    private synchronized void stopUdpThreadIfNecessary() {
        if (shouldStopUdpThread()) {
            mUdpReceivingThread.stopThread();
            mUdpReceivingThread = null;
            DICommLog.i(DICommLog.UDPRECEIVER, "Stopped Thread to receive UDP events");
        }
    }

    private boolean shouldStopUdpThread() {
        return mUdpReceivingThread != null && udpEventListeners.isEmpty();
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
