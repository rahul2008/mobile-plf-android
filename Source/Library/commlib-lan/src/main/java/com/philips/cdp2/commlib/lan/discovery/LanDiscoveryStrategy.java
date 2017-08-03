/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */
package com.philips.cdp2.commlib.lan.discovery;

import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.philips.cdp.dicommclient.discovery.SsdpServiceHelper;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.cdp2.commlib.lan.util.WifiNetworkProvider;
import com.philips.cdp2.commlib.core.devicecache.DeviceCache.ExpirationCallback;
import com.philips.cdp2.commlib.core.discovery.ObservableDiscoveryStrategy;
import com.philips.cdp2.commlib.core.exception.MissingPermissionException;
import com.philips.cdp2.commlib.core.exception.TransportUnavailableException;
import com.philips.cdp2.commlib.lan.LanDeviceCache;
import com.philips.cdp2.commlib.core.util.ConnectivityMonitor;
import com.philips.cl.di.common.ssdp.contants.DiscoveryMessageID;
import com.philips.cl.di.common.ssdp.controller.InternalMessage;
import com.philips.cl.di.common.ssdp.lib.SsdpService;
import com.philips.cl.di.common.ssdp.models.DeviceModel;
import com.philips.cl.di.common.ssdp.models.SSDPdevice;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public final class LanDiscoveryStrategy extends ObservableDiscoveryStrategy {

    private static long NETWORKNODE_TTL_MILLIS = TimeUnit.SECONDS.toMillis(10);

    private static final Object LOCK = new Object();

    @NonNull
    private final SsdpServiceHelper ssdpServiceHelper;
    @NonNull
    private final LanDeviceCache deviceCache;
    private final ConnectivityMonitor connectivityMonitor;
    private final WifiNetworkProvider wifiNetworkProvider;

    private Map<String, NetworkNode> networkNodeCache = new HashMap<>();

    private final Handler.Callback ssdpCallback = new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            if (msg == null) {
                return false;
            }
            boolean isHandled = false;

            final DeviceModel device = (DeviceModel) ((InternalMessage) msg.obj).obj;

            synchronized (LOCK) {
                switch (DiscoveryMessageID.getID(msg.what)) {
                    case DEVICE_DISCOVERED:
                        onDeviceDiscovered(device);
                        isHandled = true;
                        break;
                    case DEVICE_LOST:
                        onDeviceLost(device);
                        isHandled = true;
                        break;
                    default:
                        break;
                }
            }
            return isHandled;
        }
    };

    private ExpirationCallback expirationCallback = new ExpirationCallback() {
        @Override
        public void onCacheExpired(NetworkNode networkNode) {
            deviceCache.remove(networkNode.getCppId());
        }
    };

    public LanDiscoveryStrategy(final @NonNull LanDeviceCache deviceCache, final @NonNull ConnectivityMonitor connectivityMonitor, WifiNetworkProvider wifiNetworkProvider) {
        this.deviceCache = deviceCache;
        this.connectivityMonitor = connectivityMonitor;
        this.wifiNetworkProvider = wifiNetworkProvider;
        this.ssdpServiceHelper = new SsdpServiceHelper(SsdpService.getInstance(), ssdpCallback);
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
        ssdpServiceHelper.startDiscoveryAsync();
        deviceCache.resetTimers();

        DICommLog.d(DICommLog.DISCOVERY, "SSDP started.");
    }

    @Override
    public void stop() {
        ssdpServiceHelper.stopDiscoveryAsync();
        deviceCache.stopTimers();

        DICommLog.d(DICommLog.DISCOVERY, "SSDP stopped.");
    }

    private void onDeviceDiscovered(@NonNull DeviceModel deviceModel) {
        final NetworkNode networkNode = createNetworkNode(deviceModel);
        if (networkNode == null) {
            return;
        }

        if (this.networkNodeCache.containsKey(networkNode.getCppId())) {
            DICommLog.i(DICommLog.SSDP, "Updated device - name: " + networkNode.getName() + ", deviceType: " + networkNode.getDeviceType());

            notifyNetworkNodeUpdated(networkNode);
        } else {
            this.networkNodeCache.put(networkNode.getCppId(), networkNode);
            deviceCache.addNetworkNode(networkNode, expirationCallback, NETWORKNODE_TTL_MILLIS);
            DICommLog.i(DICommLog.SSDP, "Discovered device - name: " + networkNode.getName() + ", deviceType: " + networkNode.getDeviceType());

            notifyNetworkNodeDiscovered(networkNode);
        }
    }

    private void onDeviceLost(@NonNull DeviceModel deviceModel) {
        final NetworkNode networkNode = createNetworkNode(deviceModel);
        if (networkNode == null) {
            return;
        }
        deviceCache.remove(networkNode.getCppId());
        DICommLog.i(DICommLog.SSDP, "Lost device - name: " + networkNode.getName() + ", deviceType: " + networkNode.getDeviceType());

        notifyNetworkNodeLost(networkNode);
    }

    @Nullable
    private NetworkNode createNetworkNode(@NonNull DeviceModel deviceModel) {
        SSDPdevice ssdpDevice = deviceModel.getSsdpDevice();
        if (ssdpDevice == null) {
            return null;
        }

        final String cppId = ssdpDevice.getCppId();
        final String ipAddress = deviceModel.getIpAddress();
        final String name = ssdpDevice.getFriendlyName();
        final String deviceType = ssdpDevice.getModelName();
        final String homeSsid = getHomeSsid();
        Long bootId = -1L;
        final String modelNumber = ssdpDevice.getModelNumber();

        try {
            bootId = Long.parseLong(deviceModel.getBootID());
        } catch (NumberFormatException e) {
            // NOP
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
