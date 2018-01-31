/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */
package com.philips.cdp2.commlib.lan.discovery;

import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.cdp2.commlib.core.devicecache.CacheData;
import com.philips.cdp2.commlib.core.devicecache.DeviceCache.ExpirationCallback;
import com.philips.cdp2.commlib.core.discovery.ObservableDiscoveryStrategy;
import com.philips.cdp2.commlib.core.exception.MissingPermissionException;
import com.philips.cdp2.commlib.core.exception.TransportUnavailableException;
import com.philips.cdp2.commlib.core.util.Availability.AvailabilityListener;
import com.philips.cdp2.commlib.core.util.ConnectivityMonitor;
import com.philips.cdp2.commlib.lan.LanDeviceCache;
import com.philips.cdp2.commlib.lan.util.WifiNetworkProvider;
import com.philips.cdp2.commlib.ssdp.SSDPControlPoint;
import com.philips.cdp2.commlib.ssdp.SSDPControlPoint.DeviceListener;
import com.philips.cdp2.commlib.ssdp.SSDPDevice;
import com.philips.cdp2.commlib.ssdp.SSDPDiscovery;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static java.util.Objects.requireNonNull;

public class LanDiscoveryStrategy extends ObservableDiscoveryStrategy {

    private static final long NETWORKNODE_TTL_MILLIS = TimeUnit.SECONDS.toMillis(10);

    @NonNull
    private final SSDPDiscovery ssdp;

    @NonNull
    private final LanDeviceCache deviceCache;

    @NonNull
    private final WifiNetworkProvider wifiNetworkProvider;

    @NonNull
    private Set<String> deviceTypes;

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
        if (isConnected && isStartRequested) {
            ssdp.start();

            DICommLog.d(DICommLog.DISCOVERY, "SSDP discovery started.");
        } else {
            ssdp.stop();

            DICommLog.d(DICommLog.DISCOVERY, "SSDP discovery stopped.");
        }
    }

    private final ExpirationCallback expirationCallback = new ExpirationCallback() {

        @Override
        public void onCacheExpired(NetworkNode networkNode) {
            handleNetworkNodeLost(networkNode);
        }
    };

    public LanDiscoveryStrategy(final @NonNull LanDeviceCache deviceCache, final @NonNull ConnectivityMonitor connectivityMonitor, @NonNull WifiNetworkProvider wifiNetworkProvider) {
        this.deviceCache = requireNonNull(deviceCache);
        this.wifiNetworkProvider = requireNonNull(wifiNetworkProvider);
        this.ssdp = createSsdpDiscovery();

        this.deviceTypes = Collections.emptySet();
        this.modelIds = Collections.emptySet();

        requireNonNull(connectivityMonitor);
        connectivityMonitor.addAvailabilityListener(availabilityListener);
    }

    @VisibleForTesting
    SSDPDiscovery createSsdpDiscovery() {
        final SSDPControlPoint ssdpControlPoint = new SSDPControlPoint();
        ssdpControlPoint.addDeviceListener(deviceListener);

        return ssdpControlPoint;
    }

    @Override
    public void start() throws MissingPermissionException, TransportUnavailableException {
        start(Collections.<String>emptySet());
    }

    @Override
    public void start(@NonNull Set<String> deviceTypes) throws MissingPermissionException, TransportUnavailableException {
        start(deviceTypes, Collections.<String>emptySet());
    }

    @Override
    public void start(@NonNull Set<String> deviceTypes, @NonNull Set<String> modelIds) throws MissingPermissionException, TransportUnavailableException {
        this.deviceTypes = deviceTypes;
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
        deviceCache.clear();
    }

    @VisibleForTesting
    void onDeviceDiscovered(@NonNull SSDPDevice device) {
        final NetworkNode networkNode = createNetworkNode(device);
        if (networkNode == null) {
            return;
        }

        if (!deviceTypes.isEmpty() && !deviceTypes.contains(networkNode.getDeviceType())) {
            return;
        }

        if (!modelIds.isEmpty() && !modelIds.contains(networkNode.getModelId())) {
            return;
        }

        final CacheData cacheData = deviceCache.getCacheData(networkNode.getCppId());
        if (cacheData == null) {
            DICommLog.d(DICommLog.DISCOVERY, "Discovered device - name: " + networkNode.getName() + ", deviceType: " + networkNode.getDeviceType());
            deviceCache.addNetworkNode(networkNode, expirationCallback, NETWORKNODE_TTL_MILLIS);
        } else {
            DICommLog.d(DICommLog.DISCOVERY, "Updated device - name: " + networkNode.getName() + ", deviceType: " + networkNode.getDeviceType());
            cacheData.resetTimer();
        }

        notifyNetworkNodeDiscovered(networkNode);
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
        final String homeSsid = getHomeSsid();
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
        networkNode.setHomeSsid(homeSsid);

        if (networkNode.isValid()) {
            return networkNode;
        }
        return null;
    }

    @Nullable
    private String getHomeSsid() {
        WifiInfo wifiInfo = wifiNetworkProvider.getWifiInfo();

        if (wifiInfo == null) {
            return null;
        } else if (wifiInfo.getSupplicantState() == SupplicantState.COMPLETED) {
            return wifiInfo.getSSID();
        }
        return null;
    }
}
