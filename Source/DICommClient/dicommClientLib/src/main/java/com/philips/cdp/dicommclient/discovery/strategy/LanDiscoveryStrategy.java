/*
 * Copyright (c) 2016 Koninklijke Philips N.V.
 * All rights reserved.
 */
package com.philips.cdp.dicommclient.discovery.strategy;

import android.os.Handler;
import android.os.Message;

import com.philips.cdp.dicommclient.appliance.DICommAppliance;
import com.philips.cdp.dicommclient.appliance.DICommApplianceDatabase;
import com.philips.cdp.dicommclient.appliance.DICommApplianceFactory;
import com.philips.cdp.dicommclient.discovery.DiscoveryEventListener;
import com.philips.cdp.dicommclient.discovery.NetworkMonitor;
import com.philips.cdp.dicommclient.discovery.SsdpServiceHelper;
import com.philips.cdp.dicommclient.networknode.ConnectionState;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.networknode.NetworkNodeDatabase;
import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.cl.di.common.ssdp.contants.DiscoveryMessageID;
import com.philips.cl.di.common.ssdp.controller.InternalMessage;
import com.philips.cl.di.common.ssdp.lib.SsdpService;
import com.philips.cl.di.common.ssdp.models.DeviceModel;
import com.philips.cl.di.common.ssdp.models.SSDPdevice;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public final class LanDiscoveryStrategy<T extends DICommAppliance> implements DiscoveryStrategy {

    private DICommApplianceDatabase<T> applianceDatabase;
    private DICommApplianceFactory<T> applianceFactory;
    private LinkedHashMap<String, T> allAppliancesMap;
    private List<DiscoveryEventListener> discoveryEventListeners;
    private Lock lock = new ReentrantLock();
    private NetworkMonitor networkMonitor;
    private NetworkNodeDatabase networkNodeDatabase;
    private SsdpServiceHelper ssdpServiceHelper;

    private Handler.Callback ssdpCallback = new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            return handleDevice(msg);
        }
    };

    public LanDiscoveryStrategy() {
        ssdpServiceHelper = new SsdpServiceHelper(SsdpService.getInstance(), ssdpCallback);
        discoveryEventListeners = new ArrayList<>();
    }

    @Override
    public void addNetworkNodeListener(NetworkNodeListener listener) {
        // TODO
    }

    @Override
    public void removeNetworkNodeListener(NetworkNodeListener listener) {
        // TODO
    }

    @Override
    public void start() {
        if (networkMonitor.getLastKnownNetworkState() == NetworkMonitor.NetworkState.WIFI_WITH_INTERNET) {
            ssdpServiceHelper.startDiscoveryAsync();
            DICommLog.d(DICommLog.DISCOVERY, "Starting SSDP service - Start called (wifi_internet)");
        }
        networkMonitor.startNetworkChangedReceiver();
    }

    @Override
    public void stop() {
        // TODO
    }

    private void onApplianceDiscovered(DeviceModel deviceModel) {
        final NetworkNode networkNode = createNetworkNode(deviceModel);

        if (networkNode == null) {
            return;
        }
        DICommLog.i(DICommLog.SSDP, "Discovered appliance - name: " + networkNode.getName() + ", modelname: " + networkNode.getModelName());

        if (allAppliancesMap.containsKey(networkNode.getCppId())) {
            updateExistingAppliance(networkNode);
        } else {
            addNewAppliance(networkNode);
        }
    }

    private void addNewAppliance(NetworkNode networkNode) {
        if (!applianceFactory.canCreateApplianceForNode(networkNode)) {
            DICommLog.d(DICommLog.DISCOVERY, "Cannot create appliance for networknode: " + networkNode);
            return;
        }
        final T appliance = applianceFactory.createApplianceForNode(networkNode);
        appliance.getNetworkNode().setEncryptionKeyUpdatedListener(new NetworkNode.EncryptionKeyUpdatedListener() {
            @Override
            public void onKeyUpdate() {
                updateApplianceInDatabase(appliance);
            }
        });

        allAppliancesMap.put(appliance.getNetworkNode().getCppId(), appliance);
        DICommLog.d(DICommLog.DISCOVERY, "Successfully added appliance: " + appliance);
        notifyDiscoveryListener();
    }

    private void updateExistingAppliance(NetworkNode networkNode) {
        T existingAppliance = allAppliancesMap.get(networkNode.getCppId());
        boolean notifyListeners = true;

        if (networkNode.getHomeSsid() != null && !networkNode.getHomeSsid().equals(existingAppliance.getNetworkNode().getHomeSsid())) {
            existingAppliance.getNetworkNode().setHomeSsid(networkNode.getHomeSsid());
            updateApplianceInDatabase(existingAppliance);
        }

        if (!networkNode.getIpAddress().equals(existingAppliance.getNetworkNode().getIpAddress())) {
            existingAppliance.getNetworkNode().setIpAddress(networkNode.getIpAddress());
        }

        if (!existingAppliance.getName().equals(networkNode.getName())) {
            existingAppliance.getNetworkNode().setName(networkNode.getName());
            updateApplianceInDatabase(existingAppliance);
            notifyListeners = true;
        }

        if (existingAppliance.getNetworkNode().getBootId() != networkNode.getBootId() || existingAppliance.getNetworkNode().getEncryptionKey() == null) {
            existingAppliance.getNetworkNode().setEncryptionKey(null);
            existingAppliance.getNetworkNode().setBootId(networkNode.getBootId());
            DICommLog.d(DICommLog.PAIRING, "Discovery-Boot id changed pairing set to false");
        }

        if (existingAppliance.getNetworkNode().getConnectionState() != networkNode.getConnectionState()) {
            existingAppliance.getNetworkNode().setConnectionState(networkNode.getConnectionState());
            notifyListeners = true;
        }

        if (notifyListeners) {
            notifyDiscoveryListener();
        }
        DICommLog.d(DICommLog.DISCOVERY, "Successfully updated appliance: " + existingAppliance);
    }

    public long updateApplianceInDatabase(T appliance) {
        if (!networkNodeDatabase.contains(appliance.getNetworkNode())) {
            DICommLog.d(DICommLog.DISCOVERY, "Not updating NetworkNode database - not yet in database");
            return -1;
        }
        return insertApplianceToDatabase(appliance);
    }

    private NetworkNode createNetworkNode(DeviceModel deviceModel) {
        SSDPdevice ssdpDevice = deviceModel.getSsdpDevice();
        if (ssdpDevice == null) return null;

        DICommLog.i(DICommLog.DISCOVERY, "Appliance discovered - name: " + ssdpDevice.getFriendlyName());
        String cppId = ssdpDevice.getCppId();
        String ipAddress = deviceModel.getIpAddress();
        String name = ssdpDevice.getFriendlyName();
        String modelName = ssdpDevice.getModelName();
        String networkSsid = networkMonitor.getLastKnownNetworkSsid();
        Long bootId = -1l;
        String modelNumber = ssdpDevice.getModelNumber();

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
        networkNode.setModelType(modelNumber);
        networkNode.setModelName(modelName);
        networkNode.setConnectionState(ConnectionState.CONNECTED_LOCALLY);
        networkNode.setHomeSsid(networkSsid);

        if (!isValidNetworkNode(networkNode)) {
            return null;
        }
        return networkNode;
    }

    private boolean isValidNetworkNode(NetworkNode networkNode) {
        if (networkNode.getCppId() == null || networkNode.getCppId().isEmpty()) {
            DICommLog.d(DICommLog.DISCOVERY, "Not a valid networkNode - cppId is null");
            return false;
        }
        if (networkNode.getIpAddress() == null || networkNode.getIpAddress().isEmpty()) {
            DICommLog.d(DICommLog.DISCOVERY, "Not a valid networkNode - ipAddress is null");
            return false;
        }
        if (networkNode.getName() == null || networkNode.getName().isEmpty()) {
            DICommLog.d(DICommLog.DISCOVERY, "Not a valid networkNode - name is null");
            return false;
        }
        if (networkNode.getModelName() == null || networkNode.getModelName().isEmpty()) {
            DICommLog.d(DICommLog.DISCOVERY, "Not a valid networkNode - modelName is null");
            return false;
        }
        return true;
    }

    private boolean onApplianceLost(DeviceModel deviceModel) {
        if (deviceModel == null || deviceModel.getSsdpDevice() == null) return false;
        String lostApplianceCppId = deviceModel.getSsdpDevice().getCppId();

        ArrayList<T> discoveredAppliances = getAllDiscoveredAppliances();

        for (T appliance : discoveredAppliances) {
            if (appliance.getNetworkNode().getCppId().equals(lostApplianceCppId)) {
                DICommLog.d(DICommLog.DISCOVERY, "Lost appliance - marking as DISCONNECTED: " + appliance);
                appliance.getNetworkNode().setConnectionState(ConnectionState.DISCONNECTED);
                notifyDiscoveryListener();

                return true;
            }
        }
        return false;
    }

    public ArrayList<T> getAllDiscoveredAppliances() {
        return new ArrayList<T>(allAppliancesMap.values());
    }

    private void notifyDiscoveryListener() {
        if (discoveryEventListeners == null) {
            return;
        }

        for (DiscoveryEventListener listener : discoveryEventListeners) {
            listener.onDiscoveredAppliancesListChanged();
        }
        DICommLog.v(DICommLog.DISCOVERY, "Notified listener of change event");
    }

    public long insertApplianceToDatabase(T appliance) {
        long rowId = networkNodeDatabase.save(appliance.getNetworkNode());
        applianceDatabase.save(appliance);

        return rowId;
    }

    private boolean handleDevice(Message msg) {
        if (msg == null) {
            return false;
        }
        boolean result = false;

        DeviceModel device;
        try {
            device = (DeviceModel) ((InternalMessage) msg.obj).obj;

            lock.lock();
            switch (DiscoveryMessageID.getID(msg.what)) {
                case DEVICE_DISCOVERED:
                    onApplianceDiscovered(device);
                    result = true;
                case DEVICE_LOST:
                    onApplianceLost(device);
                    result = true;
                default:
                    break;
            }
            lock.unlock();
        } catch (Exception e) {
            DICommLog.d(DICommLog.DISCOVERY, "Invalid appliance detected, reason: " + e.getMessage());
        }
        return result;
    }
}
