/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */
package com.philips.ssdp;

import android.support.annotation.NonNull;
import android.util.Log;

import com.philips.cdp.dicommclient.discovery.SsdpDiscovery;
import com.philips.cdp.dicommclient.util.DICommLog;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static com.philips.ssdp.SSDPMessage.HOST;
import static com.philips.ssdp.SSDPMessage.MAX_WAIT_TIME;
import static com.philips.ssdp.SSDPMessage.MESSAGE_TYPE_FOUND;
import static com.philips.ssdp.SSDPMessage.MESSAGE_TYPE_NOTIFY;
import static com.philips.ssdp.SSDPMessage.MESSAGE_TYPE_SEARCH;
import static com.philips.ssdp.SSDPMessage.NAMESPACE;
import static com.philips.ssdp.SSDPMessage.NAMESPACE_DISCOVER;
import static com.philips.ssdp.SSDPMessage.NOTIFICATION_SUBTYPE;
import static com.philips.ssdp.SSDPMessage.NOTIFICATION_SUBTYPE_ALIVE;
import static com.philips.ssdp.SSDPMessage.NOTIFICATION_SUBTYPE_BYEBYE;
import static com.philips.ssdp.SSDPMessage.NOTIFICATION_SUBTYPE_UPDATE;
import static com.philips.ssdp.SSDPMessage.SEARCH_TARGET;
import static com.philips.ssdp.SSDPMessage.SEARCH_TARGET_ALL;
import static com.philips.ssdp.SSDPMessage.SSDP_HOST;
import static com.philips.ssdp.SSDPMessage.SSDP_PORT;
import static com.philips.ssdp.SSDPUtils.CHARSET_UTF8;
import static com.philips.ssdp.SSDPUtils.PROTOCOL_HTTPS;

public class SSDPControlPoint implements SsdpDiscovery {
    private static final String TAG = "SSDPControlPoint";

    private static final int MAX_WAIT_TIME_IN_SECONDS = 1;
    private static final int TIMEOUT = 3000;

    private final SocketAddress ssdpAddress = new InetSocketAddress(SSDP_HOST, SSDP_PORT);
    private DatagramSocket socket;
    private String searchTarget;
    private TimerTask searchTask;
    private DiscoveryThread discoveryThread;
    private final Executor executor = Executors.newSingleThreadExecutor();

    private Set<DeviceListener> deviceListeners = new CopyOnWriteArraySet<>();
    private Set<SSDPDevice> discoveredDevices = new CopyOnWriteArraySet<>();

    @Override
    public void start() {
        startDiscovery("TODO", MAX_WAIT_TIME_IN_SECONDS);
    }

    @Override
    public void stop() {
        stopDiscovery();
    }

    @Override
    public boolean isStarted() {
        return false;
    }

    public interface DeviceListener {
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
                searchMessage.getHeaders().put(SEARCH_TARGET, SSDPControlPoint.this.searchTarget);
                searchMessage.getHeaders().put(HOST, SSDP_HOST);
                searchMessage.getHeaders().put(NAMESPACE, NAMESPACE_DISCOVER);
                searchMessage.getHeaders().put(MAX_WAIT_TIME, String.valueOf(MAX_WAIT_TIME_IN_SECONDS));

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
        this.executor.execute(new Runnable() {
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

    public synchronized void startDiscovery(final String searchTarget, final int maxWaitTimeInSeconds) {
        if (maxWaitTimeInSeconds < 1 || maxWaitTimeInSeconds > 5) {
            throw new IllegalArgumentException("Max wait time should be a value between 1 and 5 inclusive.");
        }
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

    public synchronized void stopDiscovery() {
        if (this.searchTask != null) {
            searchTask.cancel();
            searchTask = null;
        }
        stopDiscoveryThread();
    }

    public void destroy() {
        destroySocket();
    }

    public boolean addDeviceListener(final @NonNull DeviceListener listener) {
        return this.deviceListeners.add(listener);
    }

    public boolean removeDeviceListener(final @NonNull DeviceListener listener) {
        return this.deviceListeners.remove(listener);
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

        HTTP.getInstance().get(descriptionUrl, TIMEOUT, new HTTP.HttpResponseCallback() {
            @Override
            public void onResponse(String response) {
                DICommLog.d(TAG, "Got HTTP response: " + response );

                String ipAddress = descriptionUrl.getHost();
                boolean isSecure = descriptionUrl.getProtocol().equals(PROTOCOL_HTTPS);

                final SSDPDevice device = SSDPDevice.create(response, ipAddress, isSecure);
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
        for (DeviceListener listener : deviceListeners) {
            listener.onDeviceAvailable(device);
        }
    }

    private void notifyDeviceUnavailable(SSDPDevice device) {
        for (DeviceListener listener : deviceListeners) {
            listener.onDeviceUnavailable(device);
        }
    }
}
