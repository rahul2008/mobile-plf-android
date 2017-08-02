/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */
package com.philips.cdp2.commlib.lan.discovery;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.discovery.SsdpServiceHelper;
import com.philips.cdp.dicommclient.networknode.ConnectionState;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.cdp2.commlib.core.discovery.ObservableDiscoveryStrategy;
import com.philips.cdp2.commlib.core.exception.MissingPermissionException;
import com.philips.cdp2.commlib.core.exception.TransportUnavailableException;
import com.philips.cl.di.common.ssdp.contants.DiscoveryMessageID;
import com.philips.cl.di.common.ssdp.controller.InternalMessage;
import com.philips.cl.di.common.ssdp.lib.SsdpService;
import com.philips.cl.di.common.ssdp.models.DeviceModel;
import com.philips.cl.di.common.ssdp.models.SSDPdevice;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class LanDiscoveryStrategy extends ObservableDiscoveryStrategy {

    private static final Object LOCK = new Object();

    @NonNull
    private final SsdpServiceHelper ssdpServiceHelper;

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

    public LanDiscoveryStrategy() {
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
        DICommLog.d(DICommLog.DISCOVERY, "SSDP started.");
    }

    @Override
    public void stop() {
        ssdpServiceHelper.stopDiscoveryAsync();
        DICommLog.d(DICommLog.DISCOVERY, "SSDP stopped.");
    }

    private void onDeviceDiscovered(@NonNull DeviceModel deviceModel) {
        final NetworkNode networkNode = createNetworkNode(deviceModel);

        if (networkNode == null) {
            return;
        }

        DICommLog.i(DICommLog.SSDP, "Discovered appliance - name: " + networkNode.getName() + ", deviceType: " + networkNode.getDeviceType());

        if (this.networkNodeCache.containsKey(networkNode.getCppId())) {
            notifyNetworkNodeUpdated(networkNode);
        } else {
            this.networkNodeCache.put(networkNode.getCppId(), networkNode);
            notifyNetworkNodeDiscovered(networkNode);
        }
    }

    private void onDeviceLost(@NonNull DeviceModel deviceModel) {
        final NetworkNode networkNode = createNetworkNode(deviceModel);
        if (networkNode != null) {
            notifyNetworkNodeLost(networkNode);
        }
    }

    private NetworkNode createNetworkNode(@NonNull DeviceModel deviceModel) {
        SSDPdevice ssdpDevice = deviceModel.getSsdpDevice();
        if (ssdpDevice == null) {
            return null;
        }

        DICommLog.i(DICommLog.DISCOVERY, "SSDP device discovered - name: " + ssdpDevice.getFriendlyName());

        final String cppId = ssdpDevice.getCppId();
        final String ipAddress = deviceModel.getIpAddress();
        final String name = ssdpDevice.getFriendlyName();
        final String deviceType = ssdpDevice.getModelName();
        final String networkSsid = ""; // FIXME TODO Still needed?
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
        networkNode.setConnectionState(ConnectionState.CONNECTED_LOCALLY);
        networkNode.setHomeSsid(networkSsid);

        if (networkNode.isValid()) {
            return networkNode;
        }
        return null;
    }
}
