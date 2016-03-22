/*
 * © Koninklijke Philips N.V., 2015.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.subscription;

import com.philips.cdp.dicommclient.util.DICommLog;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class UdpEventReceiver {

    private static UdpEventReceiver mInstance;

    private final UdpEventListener mUdpEventListener;
    private final Set<UdpEventListener> mUdpEventListenersSet;
    private UdpReceivingThread mUdpReceivingThread;

    private UdpEventReceiver() {
        mUdpEventListener = new UdpEventListener() {
            @Override
            public void onUDPEventReceived(String data, String fromIp) {
                notifyAllEventListeners(data, fromIp);
            }
        };
        mUdpEventListenersSet = new HashSet<UdpEventListener>();
    }

    public synchronized static UdpEventReceiver getInstance() {
        if (mInstance == null) {
            mInstance = new UdpEventReceiver();
        }
        return mInstance;
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
        return mUdpReceivingThread != null && mUdpEventListenersSet.isEmpty();
    }

    private void addUdpEventListener(UdpEventListener udpEventListener) {
        synchronized (mUdpEventListenersSet) {
            if (mUdpEventListenersSet.add(udpEventListener)) {
                DICommLog.i(DICommLog.UDPRECEIVER, "Added new listener to set");
            }
        }
    }

    public void removeUdpEventListener(UdpEventListener udpEventListener) {
        synchronized (mUdpEventListenersSet) {
            if (mUdpEventListenersSet.remove(udpEventListener)) {
                DICommLog.i(DICommLog.UDPRECEIVER, "Removed listener from set");
            }
        }
    }

    private void notifyAllEventListeners(String data, String fromIp) {
        ArrayList<UdpEventListener> udpEventListeners;
        synchronized (mUdpEventListenersSet) {
            udpEventListeners = new ArrayList<UdpEventListener>(mUdpEventListenersSet);
        }

        DICommLog.d(DICommLog.UDPRECEIVER, String.format("Notifying %d listeners of UDP event", udpEventListeners.size()));
        for (UdpEventListener listener : udpEventListeners) {
            listener.onUDPEventReceived(data, fromIp);
        }
    }
}
