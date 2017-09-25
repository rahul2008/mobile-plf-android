/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.ssdp;

import android.support.annotation.NonNull;
import android.util.Log;

import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.cdp2.commlib.lan.util.HTTP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArraySet;

import static com.philips.cdp2.commlib.lan.util.HTTP.PROTOCOL_HTTPS;
import static com.philips.cdp2.commlib.ssdp.SSDPMessage.HOST;
import static com.philips.cdp2.commlib.ssdp.SSDPMessage.MAX_WAIT_TIME;
import static com.philips.cdp2.commlib.ssdp.SSDPMessage.MESSAGE_TYPE_FOUND;
import static com.philips.cdp2.commlib.ssdp.SSDPMessage.MESSAGE_TYPE_NOTIFY;
import static com.philips.cdp2.commlib.ssdp.SSDPMessage.MESSAGE_TYPE_SEARCH;
import static com.philips.cdp2.commlib.ssdp.SSDPMessage.NAMESPACE;
import static com.philips.cdp2.commlib.ssdp.SSDPMessage.NAMESPACE_DISCOVER;
import static com.philips.cdp2.commlib.ssdp.SSDPMessage.NOTIFICATION_SUBTYPE;
import static com.philips.cdp2.commlib.ssdp.SSDPMessage.NOTIFICATION_SUBTYPE_ALIVE;
import static com.philips.cdp2.commlib.ssdp.SSDPMessage.NOTIFICATION_SUBTYPE_BYEBYE;
import static com.philips.cdp2.commlib.ssdp.SSDPMessage.NOTIFICATION_SUBTYPE_UPDATE;
import static com.philips.cdp2.commlib.ssdp.SSDPMessage.SEARCH_TARGET;
import static com.philips.cdp2.commlib.ssdp.SSDPMessage.SEARCH_TARGET_ALL;
import static com.philips.cdp2.commlib.ssdp.SSDPMessage.SSDP_HOST;
import static com.philips.cdp2.commlib.ssdp.SSDPMessage.SSDP_PORT;
import static java.util.concurrent.Executors.newSingleThreadExecutor;

public class SSDPControlPoint implements SSDPDiscovery {
    private static final String TAG = "SSDPControlPoint";

    private static final Charset CHARSET_UTF8 = Charset.forName("UTF-8");

    private static final int DEFAULT_WAIT_TIME_IN_SECONDS = 5;
    private static final int TIMEOUT = 3000;

    private final SocketAddress ssdpAddress = new InetSocketAddress(SSDP_HOST, SSDP_PORT);
    private DatagramSocket socket;
    private String searchTarget;
    private TimerTask searchTask;
    private DiscoveryThread discoveryThread;

    private Set<SSDPDeviceListener> SSDPDeviceListeners = new CopyOnWriteArraySet<>();
    private Set<SSDPDevice> discoveredDevices = new CopyOnWriteArraySet<>();
    private int maxWaitTimeInSeconds = -1;

    @Override
    public void start() {
        startDiscovery();
    }

    @Override
    public void stop() {
        stopDiscovery();
    }

    @Override
    public boolean isStarted() {
        return discoveryThread != null;
    }

    public interface SSDPDeviceListener {
        void onDeviceAvailable(SSDPDevice device);

        void onDeviceUnavailable(SSDPDevice device);
    }

    private final class DiscoveryThread extends Thread {
        private boolean isFinished;

        private DiscoveryThread() {
            super("SSDPDiscoveryThread");
        }

        @Override
        public void run() {
            byte[] buffer = new byte[1024];

            while (!isFinished && !isInterrupted()) {
                if (socket == null || socket.isClosed()) {
                    return;
                }
                final DatagramPacket responsePacket = new DatagramPacket(buffer, buffer.length);

                try {
                    socket.receive(responsePacket);
                } catch (IOException ignored) {
                    return;
                }
                final String response = new String(responsePacket.getData(), CHARSET_UTF8);

                if (response.startsWith(MESSAGE_TYPE_FOUND) || response.startsWith(MESSAGE_TYPE_NOTIFY)) {
                    int length = responsePacket.getLength();
                    byte[] payload = new byte[length];
                    ByteBuffer.wrap(responsePacket.getData(), 0, length).get(payload);

                    final String payloadString = new String(payload, CHARSET_UTF8);
                    final SSDPMessage message = new SSDPMessage(payloadString);
                    Log.d(TAG, message.toString());

                    try {
                        createDevice(message);
                    } catch (MalformedURLException ignored) {
                    }
                }
            }
            DICommLog.d(TAG, "DiscoveryThread finished.");
        }

        void finish() {
            isFinished = true;
        }
    }

    private final class SearchTask extends TimerTask {
        @Override
        public void run() {
            if (socket == null) {
                return;
            }

            try {
                final SSDPMessage searchMessage = new SSDPMessage(MESSAGE_TYPE_SEARCH);
                searchMessage.getHeaders().put(SEARCH_TARGET, searchTarget);
                searchMessage.getHeaders().put(HOST, SSDP_HOST);
                searchMessage.getHeaders().put(NAMESPACE, NAMESPACE_DISCOVER);
                searchMessage.getHeaders().put(MAX_WAIT_TIME, String.valueOf(maxWaitTimeInSeconds));

                final String searchMessageString = searchMessage.toString();
                Log.d(TAG, searchMessageString);

                byte[] bytes = searchMessageString.getBytes(CHARSET_UTF8);

                final DatagramPacket requestPacket = new DatagramPacket(bytes, bytes.length, ssdpAddress);
                socket.send(requestPacket);
            } catch (IOException e) {
                Log.e(TAG, "Error sending search message.", e);
            }
        }
    }

    public SSDPControlPoint() {
        newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                if (socket == null) {
                    try {
                        socket = new DatagramSocket(null);
                        socket.setReuseAddress(true);
                        socket.bind(null);
                    } catch (SocketException e) {
                        throw new IllegalStateException("Error creating socket.");
                    }
                }
            }
        });
    }

    private void startDiscovery() {
        startDiscovery(null, DEFAULT_WAIT_TIME_IN_SECONDS);
    }

    private synchronized void startDiscovery(final String searchTarget, final int maxWaitTimeInSeconds) {
        if (maxWaitTimeInSeconds < 1 || maxWaitTimeInSeconds > 5) {
            throw new IllegalArgumentException("Max wait time should be a value between 1 and 5 inclusive.");
        }
        this.maxWaitTimeInSeconds = maxWaitTimeInSeconds;
        startDiscoveryThread();

        if (this.searchTask == null) {
            this.searchTarget = (searchTarget == null) ? SEARCH_TARGET_ALL : searchTarget;
            this.searchTask = new SearchTask();

            try {
                new Timer().scheduleAtFixedRate(this.searchTask, 0, maxWaitTimeInSeconds * 1000);
            } catch (IllegalStateException ignored) {
            }
        }
    }

    private synchronized void stopDiscovery() {
        if (this.searchTask != null) {
            searchTask.cancel();
            searchTask = null;
        }
        stopDiscoveryThread();
    }

    public void destroy() {
        destroySocket();
    }

    public boolean addDeviceListener(final @NonNull SSDPDeviceListener listener) {
        return this.SSDPDeviceListeners.add(listener);
    }

    public boolean removeDeviceListener(final @NonNull SSDPDeviceListener listener) {
        return this.SSDPDeviceListeners.remove(listener);
    }

    private void destroySocket() {
        if (socket == null) {
            return;
        }
        socket.disconnect();
        socket.close();
        socket = null;
    }

    private synchronized void startDiscoveryThread() {
        if (discoveryThread == null) {
            discoveryThread = new DiscoveryThread();
            discoveryThread.start();
        }
    }

    private synchronized void stopDiscoveryThread() {
        if (this.discoveryThread == null) {
            return;
        }
        this.discoveryThread.finish();
        this.discoveryThread = null;
    }

    private void createDevice(final SSDPMessage message) throws MalformedURLException {
        final URL descriptionUrl = new URL(message.get(SSDPMessage.LOCATION));

        HTTP.getInstance().get(descriptionUrl, TIMEOUT, new HTTP.RequestCallback() {
            @Override
            public void onResponse(String description) {
                String ipAddress = descriptionUrl.getHost();
                boolean isSecure = descriptionUrl.getProtocol().equals(PROTOCOL_HTTPS);

                final SSDPDevice device = SSDPDevice.create(description, ipAddress, isSecure);
                if (device == null) {
                    return;
                }

                if (discoveredDevices.add(device)) {
                    String notificationSubType = message.get(NOTIFICATION_SUBTYPE);

                    if (notificationSubType == null) {
                        notifyDeviceAvailable(device);
                    } else {
                        switch (notificationSubType) {
                            case NOTIFICATION_SUBTYPE_ALIVE:
                            case NOTIFICATION_SUBTYPE_UPDATE:
                                notifyDeviceAvailable(device);
                                break;
                            case NOTIFICATION_SUBTYPE_BYEBYE:
                                notifyDeviceUnavailable(device);
                                break;
                        }
                    }
                }
            }

            @Override
            public void onError(String message, Throwable reason) {
                DICommLog.e(TAG, "Error obtaining description: " + message + ", reason: " + reason.getMessage());
            }
        });
    }

    private void notifyDeviceAvailable(SSDPDevice device) {
        for (SSDPDeviceListener listener : SSDPDeviceListeners) {
            listener.onDeviceAvailable(device);
        }
    }

    private void notifyDeviceUnavailable(SSDPDevice device) {
        for (SSDPDeviceListener listener : SSDPDeviceListeners) {
            listener.onDeviceUnavailable(device);
        }
    }
}
