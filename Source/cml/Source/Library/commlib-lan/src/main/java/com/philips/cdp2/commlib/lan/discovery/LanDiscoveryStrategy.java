/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */
package com.philips.cdp2.commlib.lan.discovery;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.cdp2.commlib.core.devicecache.CacheData;
import com.philips.cdp2.commlib.core.devicecache.DeviceCache;
import com.philips.cdp2.commlib.core.devicecache.DeviceCache.ExpirationCallback;
import com.philips.cdp2.commlib.core.discovery.ObservableDiscoveryStrategy;
import com.philips.cdp2.commlib.core.exception.MissingPermissionException;
import com.philips.cdp2.commlib.core.exception.TransportUnavailableException;
import com.philips.cdp2.commlib.core.util.Availability.AvailabilityListener;
import com.philips.cdp2.commlib.core.util.ConnectivityMonitor;
import com.philips.cdp2.commlib.lan.util.SsidProvider;
import com.philips.cdp2.commlib.ssdp.DefaultSSDPControlPoint;
import com.philips.cdp2.commlib.ssdp.DefaultSSDPControlPoint.DeviceListener;
import com.philips.cdp2.commlib.ssdp.SSDPControlPoint;
import com.philips.cdp2.commlib.ssdp.SSDPDevice;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static java.util.Objects.requireNonNull;

public class LanDiscoveryStrategy extends ObservableDiscoveryStrategy {

    private static final long NETWORK_NODE_TTL_MILLIS = TimeUnit.SECONDS.toMillis(15);

    @NonNull
    private final SSDPControlPoint ssdpControlPoint;

    @NonNull
    private final DeviceCache deviceCache;

    @NonNull
    private final SsidProvider ssidProvider;

    @NonNull
    private Set<String> modelIds;

    private boolean isConnected;

    private boolean isStartRequested;

    private final DeviceListener deviceListener = new DeviceListener() {
        @Override
        public void onDeviceAvailable(SSDPDevice ssdpDevice) {
            onDeviceDiscovered(ssdpDevice);
        }

        @Override
        public void onDeviceUnavailable(SSDPDevice ssdpDevice) {
            onDeviceLost(ssdpDevice);
        }
    };

    private final AvailabilityListener<ConnectivityMonitor> availabilityListener = new AvailabilityListener<ConnectivityMonitor>() {
        @Override
        public void onAvailabilityChanged(@NonNull ConnectivityMonitor connectivityMonitor) {
            isConnected = connectivityMonitor.isAvailable();
            handleDiscoveryStateChanged();
        }
    };

    private void handleDiscoveryStateChanged() {
        final boolean isDiscovering = ssdpControlPoint.isDiscovering();

        if (isConnected && isStartRequested) {
            try {
                ssdpControlPoint.start();

                if (!isDiscovering) {
                    notifyDiscoveryStarted();
                }
                DICommLog.d(DICommLog.DISCOVERY, "SSDP started.");
            } catch (TransportUnavailableException e) {
                DICommLog.e(DICommLog.DISCOVERY, "Error starting SSDP: " + e.getMessage());
                notifyDiscoveryFailedToStart();
            }
        } else {
            ssdpControlPoint.stop();

            if (isDiscovering) {
                notifyDiscoveryStopped();
            }
            DICommLog.d(DICommLog.DISCOVERY, "SSDP stopped.");
        }
    }

    private final ExpirationCallback expirationCallback = new ExpirationCallback() {

        @Override
        public void onCacheExpired(NetworkNode networkNode) {
            handleNetworkNodeLost(networkNode);
        }
    };

    public LanDiscoveryStrategy(final @NonNull DeviceCache deviceCache, final @NonNull ConnectivityMonitor connectivityMonitor, final @NonNull SsidProvider ssidProvider) {
        this.deviceCache = requireNonNull(deviceCache);
        this.ssidProvider = requireNonNull(ssidProvider);
        this.ssdpControlPoint = createSsdpControlPoint();
        this.modelIds = Collections.emptySet();

        requireNonNull(connectivityMonitor);
        connectivityMonitor.addAvailabilityListener(availabilityListener);
    }

    @VisibleForTesting
    SSDPControlPoint createSsdpControlPoint() {
        final DefaultSSDPControlPoint ssdpControlPoint = new DefaultSSDPControlPoint();
        ssdpControlPoint.addDeviceListener(deviceListener);

        return ssdpControlPoint;
    }

    @Override
    public void start() throws MissingPermissionException{
        start(Collections.<String>emptySet());
    }

    @Override
    public void start(@NonNull Set<String> modelIds) {
        this.modelIds = modelIds;

        isStartRequested = true;
        handleDiscoveryStateChanged();
    }

    @Override
    public void stop() {
        isStartRequested = false;
        handleDiscoveryStateChanged();
    }

    @Override
    public void clearDiscoveredNetworkNodes() {
        Collection<CacheData> items = deviceCache.clear();

        for (CacheData item : items) {
            notifyNetworkNodeLost(item.getNetworkNode());
        }
    }

    @VisibleForTesting
    void onDeviceDiscovered(@NonNull SSDPDevice device) {
        final NetworkNode networkNode = createNetworkNode(device);
        if (networkNode == null) {
            return;
        }

        if (!nodePassesFilter(networkNode)) {
            return;
        }

        final CacheData cacheData = deviceCache.getCacheData(networkNode.getCppId());
        if (cacheData == null) {
            DICommLog.d(DICommLog.DISCOVERY, "Discovered device - name: " + networkNode.getName() + ", deviceType: " + networkNode.getDeviceType());
            deviceCache.add(networkNode, expirationCallback, NETWORK_NODE_TTL_MILLIS);
        } else {
            DICommLog.d(DICommLog.DISCOVERY, "Updated device - name: " + networkNode.getName() + ", deviceType: " + networkNode.getDeviceType());
            cacheData.resetTimer();
        }

        notifyNetworkNodeDiscovered(networkNode);
    }

    private boolean nodePassesFilter(final NetworkNode networkNode) {
        return modelIds.isEmpty() || modelIds.contains(networkNode.getModelId()) || modelIds.contains(networkNode.getDeviceType());
    }

    @VisibleForTesting
    void onDeviceLost(@NonNull SSDPDevice ssdpDevice) {
        final NetworkNode networkNode = createNetworkNode(ssdpDevice);
        if (networkNode == null) {
            return;
        }
        handleNetworkNodeLost(networkNode);
    }

    private void handleNetworkNodeLost(final @NonNull NetworkNode networkNode) {
        deviceCache.remove(networkNode.getCppId());
        DICommLog.i(DICommLog.DISCOVERY, "Lost device - name: " + networkNode.getName() + ", deviceType: " + networkNode.getDeviceType());

        notifyNetworkNodeLost(networkNode);
    }

    @VisibleForTesting
    @Nullable
    NetworkNode createNetworkNode(@NonNull SSDPDevice ssdpDevice) {
        final String cppId = ssdpDevice.getCppId();
        final String ipAddress = ssdpDevice.getIpAddress();
        final String name = ssdpDevice.getFriendlyName();
        final String deviceType = ssdpDevice.getModelName();
        final String homeSsid = ssidProvider.getCurrentSsid();
        Long bootId = -1L;
        final String modelNumber = ssdpDevice.getModelNumber();

        try {
            bootId = Long.parseLong(ssdpDevice.getBootId());
        } catch (NumberFormatException ignored) {
        }

        NetworkNode networkNode = new NetworkNode();
        networkNode.setBootId(bootId);
        networkNode.setCppId(cppId);
        networkNode.setIpAddress(ipAddress);
        networkNode.setName(name);
        networkNode.setModelId(modelNumber);
        networkNode.setDeviceType(deviceType);
        networkNode.setNetworkSsid(homeSsid);

        if (networkNode.isValid()) {
            return networkNode;
        }
        return null;
    }
}
