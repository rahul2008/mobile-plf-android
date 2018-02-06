/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.ssdp;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.cdp2.commlib.core.util.ContextProvider;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.MulticastSocket;
import java.net.SocketAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static com.philips.cdp.dicommclient.util.DICommLog.SSDP;
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
import static java.lang.Thread.currentThread;
import static java.util.concurrent.Executors.newSingleThreadExecutor;
import static java.util.concurrent.Executors.newSingleThreadScheduledExecutor;

/**
 * SSDPDiscovery control point.
 * <p>
 * As defined in the UPnP specification, control points (CPs) are devices which use UPnP protocols to control UPnP controlled devices (CDs).
 */
@SuppressWarnings("unused")
public class SSDPControlPoint implements SSDPDiscovery {

    private static final int SEARCH_INTERVAL_SECONDS = 5;
    private static final String SSDP_HOST = "239.255.255.250";
    private static final int SSDP_PORT = 1900;

    private WifiManager wifi;
    private WifiManager.MulticastLock lock;

    private final SocketAddress ssdpAddress = new InetSocketAddress(SSDP_HOST, SSDP_PORT);
    private final InetAddress multicastGroupAddress;

    private MulticastSocket broadcastSocket;
    private MulticastSocket listenSocket;

    private ScheduledExecutorService searchExecutor = newSingleThreadScheduledExecutor();
    private ScheduledFuture searchTaskFuture;

    private ScheduledExecutorService discoveryExecutor = newSingleThreadScheduledExecutor();
    private ScheduledFuture discoveryTaskFuture;

    private ScheduledExecutorService listenExecutor = newSingleThreadScheduledExecutor();
    private ScheduledFuture listenTaskFuture;

    private ExecutorService callbackExecutor = newSingleThreadExecutor();

    private Set<DeviceListener> deviceListeners = new CopyOnWriteArraySet<>();

    public interface DeviceListener {
        void onDeviceAvailable(SSDPDevice device);

        void onDeviceUnavailable(SSDPDevice device);
    }

    private final Runnable searchTask = new Runnable() {

        @Override
        public void run() {
            if (broadcastSocket == null) {
                return;
            }

            try {
                final SSDPMessage searchMessage = new SSDPMessage(MESSAGE_TYPE_SEARCH);
                searchMessage.getHeaders().put(HOST, SSDP_HOST + ":" + SSDP_PORT);
                searchMessage.getHeaders().put(NAMESPACE, NAMESPACE_DISCOVER);
                searchMessage.getHeaders().put(MAX_WAIT_TIME, String.valueOf(SEARCH_INTERVAL_SECONDS));
                searchMessage.getHeaders().put(SEARCH_TARGET, SEARCH_TARGET_DICOMM);

                final String searchMessageString = searchMessage.toString();
                DICommLog.d(SSDP, searchMessageString);

                final byte[] bytes = searchMessageString.getBytes(StandardCharsets.UTF_8);
                final DatagramPacket requestPacket = new DatagramPacket(bytes, bytes.length, ssdpAddress);

                broadcastSocket.send(requestPacket);
            } catch (IOException e) {
                DICommLog.e(SSDP, "Error sending search message: " + e.getMessage());
            }
        }
    };

    private final Runnable discoveryTask = new Runnable() {

        @Override
        public void run() {
            byte[] buffer = new byte[1024];

            while (!currentThread().isInterrupted()) {
                if (broadcastSocket == null || broadcastSocket.isClosed()) {
                    return;
                }
                final DatagramPacket responsePacket = new DatagramPacket(buffer, buffer.length);

                try {
                    broadcastSocket.receive(responsePacket);
                } catch (IOException ignored) {
                    return;
                }
                final String response = new String(responsePacket.getData(), StandardCharsets.UTF_8);

                if (response.startsWith(MESSAGE_TYPE_FOUND)) {
                    int length = responsePacket.getLength();
                    byte[] payload = new byte[length];
                    ByteBuffer.wrap(responsePacket.getData(), 0, length).get(payload);

                    final String payloadString = new String(payload, StandardCharsets.UTF_8);
                    final SSDPMessage message = new SSDPMessage(payloadString);

                    DICommLog.d(SSDP, message.toString());

                    callbackExecutor.execute(new Runnable() {
                        @Override
                        public void run() {
                            handleMessage(message);
                        }
                    });
                }
            }
        }
    };

    private final Runnable listenTask = new Runnable() {

        @Override
        public void run() {
            byte[] buffer = new byte[1024];

            while (!currentThread().isInterrupted()) {
                if (listenSocket == null || listenSocket.isClosed()) {
                    return;
                }
                final DatagramPacket responsePacket = new DatagramPacket(buffer, buffer.length);

                try {
                    listenSocket.receive(responsePacket);
                } catch (IOException ignored) {
                    return;
                }
                final String response = new String(responsePacket.getData(), StandardCharsets.UTF_8);

                if (response.startsWith(MESSAGE_TYPE_NOTIFY)) {
                    int length = responsePacket.getLength();
                    byte[] payload = new byte[length];
                    ByteBuffer.wrap(responsePacket.getData(), 0, length).get(payload);

                    final String payloadString = new String(payload, StandardCharsets.UTF_8);
                    final SSDPMessage message = new SSDPMessage(payloadString);

                    DICommLog.d(SSDP, message.toString());

                    callbackExecutor.execute(new Runnable() {
                        @Override
                        public void run() {
                            handleMessage(message);
                        }
                    });
                }
            }
        }
    };

    public SSDPControlPoint() {
        this.multicastGroupAddress = getMultiCastGroupAddress();
    }

    @Override
    public void start() {
        openSockets();

        startListening();
        startDiscovery();
        startSearch();
    }

    @Override
    public void stop() {
        stopSearch();
        stopDiscovery();
        stopListening();

        closeSockets();
    }

    @Nullable
    private static InetAddress getMultiCastGroupAddress() {
        InetAddress inetAddress = null;
        try {
            inetAddress = InetAddress.getByName(SSDP_HOST);
        } catch (UnknownHostException e) {
            DICommLog.e(SSDP, "Error obtaining multicast group address: " + e.getMessage());
        }
        return inetAddress;
    }

    private void openSockets() {
        wifi = (WifiManager) ContextProvider.get().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifi == null) {
            DICommLog.e(SSDP, "Error obtaining Wi-Fi system service.");
            return;
        }

        lock = wifi.createMulticastLock("SSDPControlPointMulticastLock");
        lock.setReferenceCounted(true);
        lock.acquire();

        try {
            broadcastSocket = createBroadcastSocket();
            broadcastSocket.setReuseAddress(true);
            broadcastSocket.joinGroup(multicastGroupAddress);
            broadcastSocket.bind(null);

            listenSocket = createListenSocket();
            listenSocket.setReuseAddress(true);
            listenSocket.joinGroup(multicastGroupAddress);
        } catch (IOException e) {
            throw new IllegalStateException("Error opening sockets: " + e.getMessage());
        }
    }

    private void closeSockets() {
        closeSocket(broadcastSocket);
        closeSocket(listenSocket);

        if (lock != null && lock.isHeld()) {
            lock.release();
        }
    }

    private void closeSocket(final @NonNull MulticastSocket socket) {
        try {
            socket.leaveGroup(multicastGroupAddress);
            socket.close();
        } catch (IOException e) {
            DICommLog.e(SSDP, "Error leaving multicast group: " + e.getMessage());
        }
    }

    @NonNull
    @VisibleForTesting
    MulticastSocket createBroadcastSocket() throws IOException {
        return new MulticastSocket(null);
    }

    @NonNull
    @VisibleForTesting
    MulticastSocket createListenSocket() throws IOException {
        return new MulticastSocket(ssdpAddress);
    }

    private void startDiscovery() {
        try {
            discoveryTaskFuture = discoveryExecutor.schedule(discoveryTask, 0, TimeUnit.SECONDS);
        } catch (IllegalStateException ignored) {
        }
    }

    private void stopDiscovery() {
        if (discoveryTaskFuture != null) {
            discoveryTaskFuture.cancel(true);
        }
    }

    private void startListening() {
        try {
            listenTaskFuture = listenExecutor.schedule(listenTask, 0, TimeUnit.SECONDS);
        } catch (IllegalStateException ignored) {
        }
    }

    private void stopListening() {
        if (listenTaskFuture != null) {
            listenTaskFuture.cancel(true);
        }
    }

    private void startSearch() {
        try {
            searchTaskFuture = searchExecutor.scheduleAtFixedRate(searchTask, 0, SEARCH_INTERVAL_SECONDS, TimeUnit.SECONDS);
        } catch (IllegalStateException ignored) {
        }
    }

    private void stopSearch() {
        if (searchTaskFuture != null) {
            searchTaskFuture.cancel(true);
        }
    }

    public boolean addDeviceListener(final @NonNull DeviceListener listener) {
        return this.deviceListeners.add(listener);
    }

    public boolean removeDeviceListener(final @NonNull DeviceListener listener) {
        return this.deviceListeners.remove(listener);
    }

    @VisibleForTesting
    void handleMessage(final SSDPMessage message) {
        final URL descriptionUrl;
        final String location = message.get(LOCATION);

        try {
            descriptionUrl = new URL(location);
        } catch (MalformedURLException e) {
            DICommLog.e(SSDP, "Invalid description location: " + location);
            return;
        }

        final SSDPDevice device = SSDPDevice.createFromUrl(descriptionUrl);
        if (device == null) {
            return;
        }

        final String ipAddress = descriptionUrl.getHost();
        final boolean isSecure = descriptionUrl.getProtocol().equals("https");

        device.setIpAddress(ipAddress);
        device.setSecure(isSecure);

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
