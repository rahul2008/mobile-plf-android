/*
 * (C) 2015-2017 Koninklijke Philips N.V.
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
import com.philips.cdp2.commlib.lan.NetworkMonitor;
import com.philips.cl.di.common.ssdp.contants.DiscoveryMessageID;
import com.philips.cl.di.common.ssdp.controller.InternalMessage;
import com.philips.cl.di.common.ssdp.lib.SsdpService;
import com.philips.cl.di.common.ssdp.models.DeviceModel;
import com.philips.cl.di.common.ssdp.models.SSDPdevice;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public final class LanDiscoveryStrategy extends ObservableDiscoveryStrategy {

    private static final Lock LOCK = new ReentrantLock();

    //    private DICommApplianceDatabase<T> applianceDatabase;
    //    private DICommApplianceFactory<T> applianceFactory;
    //    private LinkedHashMap<String, T> allAppliancesMap;
    //    private List<NetworkNode> addedAppliances;
    //    private final Set<DiscoveryEventListener> discoveryEventListeners = new CopyOnWriteArraySet<>();
    //    private NetworkNodeDatabase networkNodeDatabase;

    @NonNull
    private final NetworkMonitor networkMonitor;

    @NonNull
    private final SsdpServiceHelper ssdpServiceHelper;

    private Map<String, NetworkNode> networkNodeCache = new HashMap<>();

    private final Handler.Callback ssdpCallback = new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            return handleDevice(msg);
        }
    };

    public LanDiscoveryStrategy(@NonNull NetworkMonitor networkMonitor) {
        //        this.applianceFactory = applianceFactory;
        //        this.applianceDatabase = applianceDatabase;
        //        this.networkNodeDatabase = new NetworkNodeDatabase(DICommClientWrapper.getContext());
        this.networkMonitor = networkMonitor;
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
        if (NetworkMonitor.NetworkState.WIFI_WITH_INTERNET.equals(networkMonitor.getLastKnownNetworkState())) {
            ssdpServiceHelper.startDiscoveryAsync();
            DICommLog.d(DICommLog.DISCOVERY, "Starting SSDP service - Start called (wifi_internet)");
        }
        networkMonitor.startNetworkChangedReceiver();
    }

    @Override
    public void stop() {
        ssdpServiceHelper.stopDiscoveryImmediate();
        networkMonitor.stopNetworkChangedReceiver();
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

        /* FIXME move to ApplianceManager
        if (deviceModel.getSsdpDevice() == null) {
            return;
        }
        final String lostApplianceCppId = deviceModel.getSsdpDevice().getCppId();

        ArrayList<T> discoveredAppliances = getAllDiscoveredAppliances();

        for (T appliance : discoveredAppliances) {
            if (appliance.getNetworkNode().getCppId().equals(lostApplianceCppId)) {
                DICommLog.d(DICommLog.DISCOVERY, "Lost appliance - marking as DISCONNECTED: " + appliance.toString());

                appliance.getNetworkNode().setConnectionState(ConnectionState.DISCONNECTED);
                notifyDiscoveryListenersListChanged();
            }
        }
        */
    }

    /*
    private void initializeAppliancesMapFromDataBase() {
        DICommLog.i(DICommLog.DISCOVERY, "Initializing appliances from database");
        allAppliancesMap = new LinkedHashMap<String, T>();

        List<T> allAppliances = loadAllAddedAppliancesFromDatabase();
        List<NetworkNode> addedAppliances = new ArrayList<NetworkNode>();
        for (T appliance : allAppliances) {
            allAppliancesMap.put(appliance.getNetworkNode().getCppId(), appliance);
            addedAppliances.add(appliance.getNetworkNode());
        }
        this.addedAppliances = addedAppliances;
    }

    private List<T> loadAllAddedAppliancesFromDatabase() {
        List<T> result = new ArrayList<T>();

        List<NetworkNode> networkNodes = networkNodeDatabase.getAll();

        for (NetworkNode networkNode : networkNodes) {
            T appliance = createApplianceFromNetworkNode(networkNode);

            if (appliance == null) {
                continue;
            }
            applianceDatabase.loadDataForAppliance(appliance);
            result.add(appliance);
        }
        return result;
    }

    private T createApplianceFromNetworkNode(final @NonNull NetworkNode networkNode) {
        if (applianceFactory.canCreateApplianceForNode(networkNode)) {
            final T appliance = applianceFactory.createApplianceForNode(networkNode);
            appliance.getNetworkNode().setEncryptionKeyUpdatedListener(new NetworkNode.EncryptionKeyUpdatedListener() {
                @Override
                public void onKeyUpdate() {
                    saveOrUpdateAppliance(appliance);
                }
            });
            return appliance;
        } else {
            DICommLog.d(DICommLog.DISCOVERY, "Cannot create appliance for network node: " + networkNode.toString());
            return null;
        }
    }


    private void addNewAppliance(NetworkNode networkNode) {
        final T appliance = createApplianceFromNetworkNode(networkNode);
        if (appliance == null) {
            return;
        }
        allAppliancesMap.put(appliance.getNetworkNode().getCppId(), appliance);
        DICommLog.d(DICommLog.DISCOVERY, "Successfully added appliance: " + appliance);

        notifyDiscoveryListenersListChanged();
    }

    private void updateExistingAppliance(NetworkNode networkNode) {
        T existingAppliance = allAppliancesMap.get(networkNode.getCppId());
        boolean isApplianceUpdated = false;

        if (networkNode.getHomeSsid() != null && !networkNode.getHomeSsid().equals(existingAppliance.getNetworkNode().getHomeSsid())) {
            existingAppliance.getNetworkNode().setHomeSsid(networkNode.getHomeSsid());
            isApplianceUpdated = true;
        }

        if (!networkNode.getIpAddress().equals(existingAppliance.getNetworkNode().getIpAddress())) {
            existingAppliance.getNetworkNode().setIpAddress(networkNode.getIpAddress());
            isApplianceUpdated = true;
        }

        if (!existingAppliance.getName().equals(networkNode.getName())) {
            existingAppliance.getNetworkNode().setName(networkNode.getName());
            isApplianceUpdated = true;
        }

        if (existingAppliance.getNetworkNode().getBootId() != networkNode.getBootId() || existingAppliance.getNetworkNode().getEncryptionKey() == null) {
            existingAppliance.getNetworkNode().setEncryptionKey(null);
            existingAppliance.getNetworkNode().setBootId(networkNode.getBootId());

            DICommLog.d(DICommLog.PAIRING, "Discovery-Boot id changed pairing set to false.");

            isApplianceUpdated = true;
        }

        if (existingAppliance.getNetworkNode().getConnectionState() != networkNode.getConnectionState()) {
            existingAppliance.getNetworkNode().setConnectionState(networkNode.getConnectionState());

            isApplianceUpdated = true;
        }

        if (isApplianceUpdated) {
            saveOrUpdateAppliance(existingAppliance);
            notifyDiscoveryListenersListChanged();

            DICommLog.d(DICommLog.DISCOVERY, "Successfully updated appliance: " + existingAppliance);
        }
    }
    */

    private NetworkNode createNetworkNode(@NonNull DeviceModel deviceModel) {
        SSDPdevice ssdpDevice = deviceModel.getSsdpDevice();
        if (ssdpDevice == null) {
            return null;
        }

        DICommLog.i(DICommLog.DISCOVERY, "Appliance discovered - name: " + ssdpDevice.getFriendlyName());

        final String cppId = ssdpDevice.getCppId();
        final String ipAddress = deviceModel.getIpAddress();
        final String name = ssdpDevice.getFriendlyName();
        final String modelName = ssdpDevice.getModelName();
        final String networkSsid = networkMonitor.getLastKnownNetworkSsid();
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
        networkNode.setDeviceType(modelName);
        networkNode.setConnectionState(ConnectionState.CONNECTED_LOCALLY);
        networkNode.setHomeSsid(networkSsid);

        if (isValidNetworkNode(networkNode)) {
            return networkNode;
        }
        return null;
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
        if (networkNode.getDeviceType() == null || networkNode.getDeviceType().isEmpty()) {
            DICommLog.d(DICommLog.DISCOVERY, "Not a valid networkNode - deviceType is null");
            return false;
        }
        return true;
    }

    /*
    private ArrayList<T> getAllDiscoveredAppliances() {
        return new ArrayList<T>(allAppliancesMap.values());
    }

    private void notifyDiscoveryListenersListChanged() {
        for (DiscoveryEventListener listener : discoveryEventListeners) {
            listener.onDiscoveredAppliancesListChanged();
        }
        DICommLog.v(DICommLog.DISCOVERY, "Notified discovery listeners of change event.");
    }

    private long saveOrUpdateAppliance(T appliance) {
        long rowId = -1L;
        if (networkNodeDatabase.contains(appliance.getNetworkNode())) {
            rowId = networkNodeDatabase.save(appliance.getNetworkNode());
            applianceDatabase.save(appliance);
        } else {
            DICommLog.d(DICommLog.DISCOVERY, "Not updating NetworkNode database - not yet in database.");
        }
        return rowId;
    }
    */

    private boolean handleDevice(Message msg) {
        if (msg == null) {
            return false;
        }
        boolean result = false;

        DeviceModel device;
        try {
            device = (DeviceModel) ((InternalMessage) msg.obj).obj;

            LOCK.lock();
            switch (DiscoveryMessageID.getID(msg.what)) {
                case DEVICE_DISCOVERED:
                    onDeviceDiscovered(device);
                    result = true;
                case DEVICE_LOST:
                    onDeviceLost(device);
                    result = true;
                default:
                    break;
            }
            LOCK.unlock();
        } catch (Throwable t) {
            DICommLog.e(DICommLog.DISCOVERY, "Invalid appliance detected, reason: " + t.getMessage());
        }
        return result;
    }
}
