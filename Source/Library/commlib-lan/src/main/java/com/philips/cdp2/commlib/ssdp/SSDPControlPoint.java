/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.ssdp;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.cdp2.commlib.lan.util.HTTP;
import com.philips.cdp2.commlib.lan.util.HTTP.RequestCallback;

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
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static com.philips.cdp2.commlib.lan.util.HTTP.PROTOCOL_HTTPS;
import static com.philips.cdp2.commlib.ssdp.SSDPMessage.HOST;
import static com.philips.cdp2.commlib.ssdp.SSDPMessage.LOCATION;
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
import static com.philips.cdp2.commlib.ssdp.SSDPMessage.SEARCH_TARGET_DICOMM;
import static com.philips.cdp2.commlib.ssdp.SSDPMessage.SSDP_HOST;
import static com.philips.cdp2.commlib.ssdp.SSDPMessage.SSDP_PORT;
import static java.lang.Thread.currentThread;
import static java.util.concurrent.Executors.newSingleThreadScheduledExecutor;

public class SSDPControlPoint implements SSDPDiscovery {
    private static final String TAG = "SSDPControlPoint";

    private static final Charset CHARSET_UTF8 = Charset.forName("UTF-8");

    private static final int SEARCH_INTERVAL_SECONDS = 5;
    private static final int DESCRIPTION_TIMEOUT_MILLIS = 3000;

    private final SocketAddress ssdpAddress = new InetSocketAddress(SSDP_HOST, SSDP_PORT);
    private DatagramSocket socket;

    private ScheduledExecutorService searchExecutor = newSingleThreadScheduledExecutor();
    private SearchTask searchTask;
    private ScheduledFuture searchTaskFuture;

    private ScheduledExecutorService discoveryExecutor = newSingleThreadScheduledExecutor();
    private Runnable discoveryTask;
    private ScheduledFuture discoveryTaskFuture;

    private Set<DeviceListener> deviceListeners = new CopyOnWriteArraySet<>();
    private Set<SSDPDevice> discoveredDevices = new CopyOnWriteArraySet<>();

    public interface DeviceListener {
        void onDeviceAvailable(SSDPDevice device);

        void onDeviceUnavailable(SSDPDevice device);
    }

    private final class SearchTask implements Runnable {

        @Override
        public void run() {
            if (socket == null) {
                return;
            }

            try {
                final SSDPMessage searchMessage = new SSDPMessage(MESSAGE_TYPE_SEARCH);
                searchMessage.getHeaders().put(SEARCH_TARGET, SEARCH_TARGET_DICOMM);
                searchMessage.getHeaders().put(HOST, SSDP_HOST);
                searchMessage.getHeaders().put(NAMESPACE, NAMESPACE_DISCOVER);
                searchMessage.getHeaders().put(MAX_WAIT_TIME, String.valueOf(SEARCH_INTERVAL_SECONDS));

                final String searchMessageString = searchMessage.toString();
                DICommLog.d(DICommLog.SSDP, searchMessageString);

                final byte[] bytes = searchMessageString.getBytes(CHARSET_UTF8);
                final DatagramPacket requestPacket = new DatagramPacket(bytes, bytes.length, ssdpAddress);

                socket.send(requestPacket);
            } catch (IOException e) {
                DICommLog.e(DICommLog.SSDP, "Error sending search message: " + e.getMessage());
            }
        }
    }

    private final class DiscoveryTask implements Runnable {

        @Override
        public void run() {
            byte[] buffer = new byte[1024];

            while (!currentThread().isInterrupted()) {
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
                    DICommLog.d(DICommLog.SSDP, message.toString());

                    handleMessage(message);
                }
            }
        }
    }

    public SSDPControlPoint() {
        setupSocket();
    }

    @Override
    public void start() {
        startDiscovery();
        startSearch();
    }

    @Override
    public void stop() {
        stopSearch();
        stopDiscovery();
    }

    @Override
    public boolean isStarted() {
        return discoveryTaskFuture != null && !discoveryTaskFuture.isDone();
    }

    private void setupSocket() {
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

    private void startDiscovery() {
        if (this.discoveryTask == null) {
            this.discoveryTask = new DiscoveryTask();

            try {
                discoveryTaskFuture = discoveryExecutor.schedule(this.discoveryTask, 0, TimeUnit.SECONDS);
            } catch (IllegalStateException ignored) {
            }
        }
    }

    private void stopDiscovery() {
        if (discoveryTaskFuture != null) {
            discoveryTaskFuture.cancel(true);
            discoveryTaskFuture = null;
        }
    }

    private void startSearch() {
        if (this.searchTask == null) {
            this.searchTask = new SearchTask();

            try {
                searchTaskFuture = searchExecutor.scheduleAtFixedRate(this.searchTask, 0, SEARCH_INTERVAL_SECONDS, TimeUnit.SECONDS);
            } catch (IllegalStateException ignored) {
            }
        }
    }

    private void stopSearch() {
        if (searchTaskFuture != null) {
            searchTaskFuture.cancel(true);
            searchTask = null;
        }
    }

    public boolean addDeviceListener(final @NonNull DeviceListener listener) {
        return this.deviceListeners.add(listener);
    }

    public boolean removeDeviceListener(final @NonNull DeviceListener listener) {
        return this.deviceListeners.remove(listener);
    }

    public void destroy() {
        if (socket == null) {
            return;
        }
        socket.disconnect();
        socket.close();
        socket = null;
    }

    @VisibleForTesting
    void handleMessage(final SSDPMessage message) {
        final URL descriptionUrl;
        final String location = message.get(LOCATION);

        try {
            descriptionUrl = new URL(location);
        } catch (MalformedURLException e) {
            DICommLog.e(DICommLog.SSDP, "Invalid description location: " + location);
            return;
        }

        createHttp().get(descriptionUrl, DESCRIPTION_TIMEOUT_MILLIS, new RequestCallback() {
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
                DICommLog.e(DICommLog.SSDP, "Error obtaining description: " + message + ", reason: " + reason.getMessage());
            }
        });
    }

    @VisibleForTesting
    HTTP createHttp() {
        return HTTP.getInstance();
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
