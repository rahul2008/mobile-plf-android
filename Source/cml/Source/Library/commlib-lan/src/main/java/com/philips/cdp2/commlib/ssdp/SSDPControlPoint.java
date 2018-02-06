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
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static com.philips.cdp.dicommclient.util.DICommLog.SSDP;
import static com.philips.cdp2.commlib.ssdp.SSDPMessage.LOCATION;
import static com.philips.cdp2.commlib.ssdp.SSDPMessage.MESSAGE_TYPE_FOUND;
import static com.philips.cdp2.commlib.ssdp.SSDPMessage.MESSAGE_TYPE_NOTIFY;
import static com.philips.cdp2.commlib.ssdp.SSDPMessage.NOTIFICATION_SUBTYPE;
import static com.philips.cdp2.commlib.ssdp.SSDPMessage.NOTIFICATION_SUBTYPE_ALIVE;
import static com.philips.cdp2.commlib.ssdp.SSDPMessage.NOTIFICATION_SUBTYPE_BYEBYE;
import static com.philips.cdp2.commlib.ssdp.SSDPMessage.NOTIFICATION_SUBTYPE_UPDATE;
import static com.philips.cdp2.commlib.ssdp.SSDPMessage.SEARCH_TARGET_DICOMM;
import static com.philips.cdp2.commlib.ssdp.SSDPMessage.SSDP_HOST;
import static com.philips.cdp2.commlib.ssdp.SSDPMessage.SSDP_PORT;
import static java.lang.Thread.currentThread;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.concurrent.Executors.newSingleThreadExecutor;
import static java.util.concurrent.Executors.newSingleThreadScheduledExecutor;

/**
 * SSDPDiscovery control point.
 * <p>
 * As defined in the UPnP specification, control points (CPs) are devices which use UPnP protocols to control UPnP controlled devices (CDs).
 */
@SuppressWarnings("unused")
public class SSDPControlPoint implements SSDPDiscovery {

    public interface DeviceListener {

        void onDeviceAvailable(SSDPDevice device);

        void onDeviceUnavailable(SSDPDevice device);
    }

    private final Map<String, SSDPDevice> deviceCache = new ConcurrentHashMap<>();
    private static final int SEARCH_INTERVAL_SECONDS = 5;

    private WifiManager.MulticastLock lock;

    private final SocketAddress ssdpAddress = new InetSocketAddress(SSDP_HOST, SSDP_PORT);
    private final InetAddress multicastGroupAddress;

    private MulticastSocket broadcastSocket;
    private MulticastSocket listenSocket;

    private ScheduledExecutorService broadcastExecutor = newSingleThreadScheduledExecutor();
    private ScheduledFuture broadcastTaskFuture;

    private ScheduledExecutorService discoveryExecutor = newSingleThreadScheduledExecutor();
    private ScheduledFuture discoveryTaskFuture;

    private ScheduledExecutorService listenExecutor = newSingleThreadScheduledExecutor();
    private ScheduledFuture listenTaskFuture;

    private ExecutorService callbackExecutor = newSingleThreadExecutor();

    private Set<DeviceListener> deviceListeners = new CopyOnWriteArraySet<>();

    private final Runnable searchTask = new Runnable() {

        @Override
        public void run() {
            if (broadcastSocket == null) {
                return;
            }

            try {
                final SSDPMessage searchMessage = new SSDPSearchMessage(SEARCH_TARGET_DICOMM, SEARCH_INTERVAL_SECONDS);
                final String searchMessageString = searchMessage.toString();

                DICommLog.d(SSDP, searchMessageString);

                final byte[] bytes = searchMessageString.getBytes(UTF_8);
                final DatagramPacket requestPacket = new DatagramPacket(bytes, bytes.length, ssdpAddress);

                broadcastSocket.send(requestPacket);
            } catch (IOException e) {
                DICommLog.e(SSDP, "Error sending search message: " + e.getMessage());
            }
        }
    };

    public SSDPControlPoint() {
        this.multicastGroupAddress = getMultiCastGroupAddress();
    }

    @Override
    public void start() {
        if (acquireMulticastLock()) {
            openSockets();

            startListening();
            startDiscovery();
            startSearching();
        }
    }

    @Override
    public void stop() {
        stopSearching();
        stopDiscovery();
        stopListening();

        closeSockets();
        releaseMulticastLock();
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

    private boolean acquireMulticastLock() {
        final WifiManager wifiManager = (WifiManager) ContextProvider.get().getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        if (wifiManager == null) {
            DICommLog.e(SSDP, "Error obtaining Wi-Fi system service.");
            return false;
        }

        lock = wifiManager.createMulticastLock("SSDPControlPointMulticastLock");
        lock.setReferenceCounted(true);
        lock.acquire();

        return lock.isHeld();
    }

    private void releaseMulticastLock() {
        if (lock != null && lock.isHeld()) {
            lock.release();
        }
    }

    private void openSockets() {
        try {
            broadcastSocket = createBroadcastSocket();
            broadcastSocket.setReuseAddress(true);
            broadcastSocket.joinGroup(multicastGroupAddress);
            broadcastSocket.bind(null);

            listenSocket = createListenSocket();
            listenSocket.setReuseAddress(true);
            listenSocket.joinGroup(multicastGroupAddress);
        } catch (IOException e) {
            throw new IllegalStateException("Error opening socket(s): " + e.getMessage());
        }
    }

    private void closeSockets() {
        closeSocket(broadcastSocket);
        closeSocket(listenSocket);
    }

    private void closeSocket(final MulticastSocket socket) {
        if (socket == null) {
            return;
        }

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

    private void startSearching() {
        broadcastTaskFuture = broadcastExecutor.scheduleAtFixedRate(searchTask, 0, SEARCH_INTERVAL_SECONDS, TimeUnit.SECONDS);
    }

    private void stopSearching() {
        if (broadcastTaskFuture != null) {
            broadcastTaskFuture.cancel(true);
        }
    }

    private void startDiscovery() {
        final Runnable discoveryTask = new MessageTask(broadcastSocket, MESSAGE_TYPE_FOUND);
        discoveryTaskFuture = discoveryExecutor.schedule(discoveryTask, 0, TimeUnit.SECONDS);
    }

    private void stopDiscovery() {
        if (discoveryTaskFuture != null) {
            discoveryTaskFuture.cancel(true);
        }
    }

    private void startListening() {
        final Runnable listenTask = new MessageTask(listenSocket, MESSAGE_TYPE_NOTIFY);
        listenTaskFuture = listenExecutor.schedule(listenTask, 0, TimeUnit.SECONDS);
    }

    private void stopListening() {
        if (listenTaskFuture != null) {
            listenTaskFuture.cancel(true);
        }
    }

    public void addDeviceListener(final @NonNull DeviceListener listener) {
        this.deviceListeners.add(listener);
    }

    public void removeDeviceListener(final @NonNull DeviceListener listener) {
        this.deviceListeners.remove(listener);
    }

    @VisibleForTesting
    void handleMessage(final SSDPMessage message) {
        final String usn = message.get(SSDPMessage.USN);

        final SSDPDevice device;

        if (deviceCache.containsKey(usn)) {
            device = deviceCache.get(usn);
        } else {
            device = createDeviceFromSsdpMessage(message);

            if (device == null) {
                return;
            } else {
                deviceCache.put(usn, device);
            }
        }

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

    @Nullable
    private static SSDPDevice createDeviceFromSsdpMessage(final SSDPMessage message) {
        final SSDPDevice device;
        final URL descriptionUrl;
        final String location = message.get(LOCATION);

        try {
            descriptionUrl = new URL(location);
        } catch (MalformedURLException e) {
            DICommLog.e(SSDP, "Invalid description location: " + location);
            return null;
        }

        device = SSDPDevice.createFromUrl(descriptionUrl);
        if (device == null) {
            return null;
        }

        final String ipAddress = descriptionUrl.getHost();
        final boolean isSecure = descriptionUrl.getProtocol().equals("https");

        device.setIpAddress(ipAddress);
        device.setSecure(isSecure);

        return device;
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

    private final class MessageTask implements Runnable {

        @NonNull
        private final MulticastSocket socket;

        @NonNull
        private final String messageType;

        MessageTask(final @NonNull MulticastSocket socket, final @NonNull String messageType) {
            this.socket = socket;
            this.messageType = messageType;
        }

        @Override
        public void run() {
            byte[] buffer = new byte[1024];

            while (!currentThread().isInterrupted()) {
                if (socket.isClosed()) {
                    return;
                }
                final DatagramPacket responsePacket = new DatagramPacket(buffer, buffer.length);

                try {
                    socket.receive(responsePacket);
                } catch (IOException ignored) {
                    return;
                }
                final String response = new String(responsePacket.getData(), UTF_8);

                if (response.startsWith(messageType)) {
                    int length = responsePacket.getLength();
                    byte[] payload = new byte[length];
                    ByteBuffer.wrap(responsePacket.getData(), 0, length).get(payload);

                    final String payloadString = new String(payload, UTF_8);
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
    }
}
