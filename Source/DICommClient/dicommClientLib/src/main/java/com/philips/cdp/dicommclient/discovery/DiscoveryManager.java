/*
 * (C) Koninklijke Philips N.V., 2015, 2016.
 * All rights reserved.
 */
package com.philips.cdp.dicommclient.discovery;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.philips.cdp.dicommclient.appliance.DICommAppliance;
import com.philips.cdp.dicommclient.appliance.DICommApplianceDatabase;
import com.philips.cdp.dicommclient.appliance.DICommApplianceFactory;
import com.philips.cdp.dicommclient.cpp.CppController;
import com.philips.cdp.dicommclient.discovery.NetworkMonitor.NetworkChangedCallback;
import com.philips.cdp.dicommclient.discovery.NetworkMonitor.NetworkState;
import com.philips.cdp.dicommclient.networknode.ConnectionState;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.networknode.NetworkNode.EncryptionKeyUpdatedListener;
import com.philips.cdp.dicommclient.networknode.NetworkNodeDatabase;
import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.cl.di.common.ssdp.contants.DiscoveryMessageID;
import com.philips.cl.di.common.ssdp.controller.InternalMessage;
import com.philips.cl.di.common.ssdp.lib.SsdpService;
import com.philips.cl.di.common.ssdp.models.DeviceModel;
import com.philips.cl.di.common.ssdp.models.SSDPdevice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Discovery of the appliances is managed by Discovery Manager. It is the main
 * interface to the User Interface. The output of Discovery Manager is the list
 * of DICommAppliances which is further handled by User Interface and Appliance
 * Manager. In order to build this list, the Discovery Manager makes use of
 * input from SSDP, a pairing database and network changes.
 *
 * @author Jeroen Mols
 * @date 30 Apr 2014
 */
public class DiscoveryManager<T extends DICommAppliance> {

    private static DiscoveryManager<? extends DICommAppliance> mInstance;

    private LinkedHashMap<String, T> mAllAppliancesMap;
    private List<NetworkNode> mAddedAppliances;

    private DICommApplianceFactory<T> mApplianceFactory;
    private NetworkNodeDatabase mNetworkNodeDatabase;
    private DICommApplianceDatabase<T> mApplianceDatabase;

    private static final Object mDiscoveryLock = new Object();
    private NetworkMonitor mNetwork;
    private SsdpServiceHelper mSsdpHelper;

    private List<DiscoveryEventListener> mDiscoveryEventListenersList;

    public static final int DISCOVERY_WAITFORLOCAL_MESSAGE = 9000001;
    public static final int DISCOVERY_SYNCLOCAL_MESSAGE = 9000002;
    private static final int DISCOVERY_WAITFORLOCAL_TIMEOUT = 10000;
    private static final int DISCOVERY_SYNCLOCAL_TIMEOUT = 10000;

    private static Handler mDiscoveryTimeoutHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == DISCOVERY_WAITFORLOCAL_MESSAGE) {
                synchronized (mDiscoveryLock) {
                    DiscoveryManager.getInstance().markNonDiscoveredAppliancesRemote();
                }
            } else if (msg.what == DISCOVERY_SYNCLOCAL_MESSAGE) {
                synchronized (mDiscoveryLock) {
                    DiscoveryManager.getInstance().markLostAppliancesInBackgroundOfflineOrRemote();
                }
            }
        }
    };

    static synchronized <U extends DICommAppliance> DiscoveryManager<U> createSharedInstance(Context applicationContext, CppController cppController, DICommApplianceFactory<U> applianceFactory) {
        return createSharedInstance(applicationContext, cppController, applianceFactory, new NullApplianceDatabase<U>());
    }

    static synchronized <U extends DICommAppliance> DiscoveryManager<U> createSharedInstance(Context applicationContext, CppController cppController, DICommApplianceFactory<U> applianceFactory, DICommApplianceDatabase<U> applianceDatabase) {
        if (mInstance != null) {
            throw new RuntimeException("DiscoveryManager can only be initialized once");
        }

        NetworkMonitor networkMonitor = new NetworkMonitor(applicationContext);

        DiscoveryManager<U> discoveryManager = new DiscoveryManager<U>(applianceFactory, applianceDatabase, networkMonitor);
        mInstance = discoveryManager;
        return discoveryManager;
    }

    public static synchronized DiscoveryManager<? extends DICommAppliance> getInstance() {
        return mInstance;
    }

    /* package, for testing */ DiscoveryManager(DICommApplianceFactory<T> applianceFactory, DICommApplianceDatabase<T> applianceDatabase, NetworkMonitor networkMonitor) {
        mApplianceFactory = applianceFactory;
        mApplianceDatabase = applianceDatabase;

        mNetworkNodeDatabase = new NetworkNodeDatabase(DICommClientWrapper.getContext());
        initializeAppliancesMapFromDataBase();

        mSsdpHelper = new SsdpServiceHelper(SsdpService.getInstance(), mHandlerCallback);

        mNetwork = networkMonitor;
        mNetwork.setListener(mNetworkChangedCallback);
        if (mDiscoveryEventListenersList == null) {
            mDiscoveryEventListenersList = new ArrayList<DiscoveryEventListener>();
        }
    }

    public void addDiscoveryEventListener(DiscoveryEventListener listener) {
        if (listener != null && !mDiscoveryEventListenersList.contains(listener)) {
            mDiscoveryEventListenersList.add(listener);
        }
    }

    public void removeDiscoverEventListener(DiscoveryEventListener listener) {
        mDiscoveryEventListenersList.remove(listener);
    }

    public void start() {
        if (mNetwork.getLastKnownNetworkState() == NetworkState.WIFI_WITH_INTERNET) {
            mSsdpHelper.startDiscoveryAsync();
            DICommLog.d(DICommLog.DISCOVERY, "Starting SSDP service - Start called (wifi_internet)");
        }
        mNetwork.startNetworkChangedReceiver();
    }

    public void stop() {
        mSsdpHelper.stopDiscoveryAsync();
        DICommLog.d(DICommLog.DISCOVERY, "Stopping SSDP service - Stop called");
        mNetwork.stopNetworkChangedReceiver();
    }

    public ArrayList<T> getAllDiscoveredAppliances() {
        return new ArrayList<T>(mAllAppliancesMap.values());
    }

    public List<T> getAddedAppliances() {
        List<T> appliances = new ArrayList<T>();
        for (NetworkNode addedAppliance : mAddedAppliances) {
            appliances.add(mAllAppliancesMap.get(addedAppliance.getCppId()));
        }
        return appliances;
    }

    // TODO DIComm refactor: this method should be removed completely
    public List<T> updateAddedAppliances() {
        mAddedAppliances = mNetworkNodeDatabase.getAll();
        return getAddedAppliances();
    }

    // TODO DIComm refactor: this method should be removed from public interface
    public void removeFromDiscoveredList(String cppId) {
        if (cppId == null || cppId.isEmpty()) return;
        DICommAppliance appliance = mAllAppliancesMap.remove(cppId);
        if (appliance != null) {
            appliance.getNetworkNode().setEncryptionKeyUpdatedListener(null);
        }
    }

    // TODO DIComm refactor: this method should be removed from public interface
    public void updatePairingStatus(String cppId, NetworkNode.PAIRED_STATUS state) {
        if (cppId == null || cppId.isEmpty()) return;
        if (mAllAppliancesMap.containsKey(cppId)) {
            mAllAppliancesMap.get(cppId).getNetworkNode().setPairedState(state);
        }
    }

    public List<T> getNewAppliancesDiscovered() {
        boolean addToNewApplianceList = true;
        List<T> discoveredAppliances = getAllDiscoveredAppliances();
        List<T> addedAppliances = getAddedAppliances();
        List<T> newAppliances = new ArrayList<T>();

        for (T appliance : discoveredAppliances) {
            for (T addedAppliance : addedAppliances) {
                if (appliance.getNetworkNode().getCppId().equals(addedAppliance.getNetworkNode().getCppId())) {
                    addToNewApplianceList = false;
                    break;
                }
            }
            if (addToNewApplianceList) {
                //Add connected appliance only, ignore disconnected
                if (appliance.getNetworkNode().getConnectionState() != ConnectionState.DISCONNECTED) {
                    appliance.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.NOT_PAIRED);
                    newAppliances.add(appliance);
                }
            }
            addToNewApplianceList = true;
        }
        return newAppliances;
    }

    public T getApplianceByCppId(String cppId) {
        if (cppId == null || cppId.isEmpty()) return null;
        return mAllAppliancesMap.get(cppId);
    }

    private NetworkChangedCallback mNetworkChangedCallback = new NetworkChangedCallback() {

        @Override
        public void onNetworkChanged(NetworkState networkState, String networkSsid) {
            // REMARK: Wifi switch will go through the none state
            cancelConnectViaCppAfterLocalAttempt();
            synchronized (mDiscoveryLock) {
                switch (networkState) {
                    case NONE:
                        markAllAppliancesOffline();
                        mSsdpHelper.stopDiscoveryImmediate();
                        DICommLog.d(DICommLog.DISCOVERY, "Stopping SSDP service - Network change (no network)");
                        break;
                    case MOBILE:
                        markAllAppliancesRemote();
                        mSsdpHelper.stopDiscoveryImmediate();
                        DICommLog.d(DICommLog.DISCOVERY, "Stopping SSDP service - Network change (mobile data)");
                        break;
                    case WIFI_WITH_INTERNET:
                        markOtherNetworkAppliancesRemote(networkSsid);
                        connectViaCppAfterLocalAttemptDelayed();
                        mSsdpHelper.startDiscoveryAsync();
                        DICommLog.d(DICommLog.DISCOVERY, "Starting SSDP service - Network change (wifi internet)");
                        break;
                    default:
                        break;
                }
            }
        }
    };

    private CppDiscoverEventListener mCppDiscoverEventListener = new CppDiscoverEventListener() {

        // TODO DIComm Refactor: investigate and fix commented code
        @Override
        public void onSignedOnViaCpp() {
            DICommLog.v(DICommLog.DISCOVERY, "Signed on to CPP - setting all appliances online via cpp");
            //updateAllAppliancesOnlineViaCpp();
        }

        // TODO DIComm Refactor: investigate and fix commented code
        @Override
        public void onSignedOffViaCpp() {
            DICommLog.v(DICommLog.DISCOVERY, "Signed on to CPP - setting all appliances offline via cpp");
            //		updateAllAppliancesOfflineViaCpp();
        }

        @Override
        public void onDiscoverEventReceived(DiscoverInfo info, boolean isResponseToRequest) {
            if (info == null) return;
            DICommLog.v(DICommLog.DISCOVERY, "Received discover event from CPP: " + (isResponseToRequest ? "requested" : "change"));

            boolean notifyListeners = false;
            synchronized (mDiscoveryLock) {
                if (isResponseToRequest) {
                    DICommLog.v(DICommLog.DISCOVERY, "Received connected appliances list via cpp");
                    notifyListeners = updateConnectedStateViaCppAllAppliances(info);
                } else {
                    DICommLog.v(DICommLog.DISCOVERY, "Received connected appliance update via CPP");
                    notifyListeners = updateConnectedStateViaCppReturnedAppliances(info);
                }
            }

            if (notifyListeners) {
                notifyDiscoveryListener();
            }
        }
    };

    private Handler.Callback mHandlerCallback = new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            if (msg == null) return false;

            final DiscoveryMessageID messageID = DiscoveryMessageID.getID(msg.what);
            DeviceModel device = getDeviceModelFromMessage(msg);
            if (device == null) return false;

            synchronized (mDiscoveryLock) {
                switch (messageID) {
                    case DEVICE_DISCOVERED:
                        return onApplianceDiscovered(device);
                    case DEVICE_LOST:
                        return onApplianceLost(device);
                    default:
                        break;
                }
            }
            return false;
        }
    };

    // ********** START SSDP METHODS ************
    private boolean onApplianceDiscovered(DeviceModel deviceModel) {

        NetworkNode networkNode = createNetworkNode(deviceModel);
        if (networkNode == null) return false;
        DICommLog.i(DICommLog.SSDP, "Discovered appliance - name: " + networkNode.getName() + "   modelname: " + networkNode.getModelName());
        if (mAllAppliancesMap.containsKey(networkNode.getCppId())) {
            updateExistingAppliance(networkNode);
        } else {
            addNewAppliance(networkNode);
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

    private void updateExistingAppliance(NetworkNode networkNode) {
        T existingAppliance = mAllAppliancesMap.get(networkNode.getCppId());
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
            existingAppliance.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.NOT_PAIRED);
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

    /**
     * Completely new appliance - never seen before
     */
    private void addNewAppliance(NetworkNode networkNode) {
        if (!mApplianceFactory.canCreateApplianceForNode(networkNode)) {
            DICommLog.d(DICommLog.DISCOVERY, "Cannot create appliance for networknode: " + networkNode);
            return;
        }
        final T appliance = mApplianceFactory.createApplianceForNode(networkNode);
        appliance.getNetworkNode().setEncryptionKeyUpdatedListener(new EncryptionKeyUpdatedListener() {
            @Override
            public void onKeyUpdate() {
                updateApplianceInDatabase(appliance);
            }
        });

        mAllAppliancesMap.put(appliance.getNetworkNode().getCppId(), appliance);
        DICommLog.d(DICommLog.DISCOVERY, "Successfully added appliance: " + appliance);
        notifyDiscoveryListener();
    }

    public void markLostAppliancesInBackgroundOfflineOrRemote() {
        DICommLog.d(DICommLog.DISCOVERY, "Syncing appliances list for lost appliances in background");
        boolean statusUpdated = false;

        ArrayList<String> onlineCppIds = mSsdpHelper.getOnlineDevicesCppId();

        for (DICommAppliance appliance : mAllAppliancesMap.values()) {
            if (appliance.getNetworkNode().getConnectionState() == ConnectionState.DISCONNECTED)
                continue; // must be offline: not discovered
            if (isConnectedRemotely(appliance))
                continue; // must be remote: not discovered
            if (onlineCppIds.contains(appliance.getNetworkNode().getCppId()))
                continue; // State is correct

            // Losing an appliance in the background means it is offline
            appliance.getNetworkNode().setConnectionState(ConnectionState.DISCONNECTED);
            DICommLog.v(DICommLog.DISCOVERY, "Marked non discovered DISCONNECTED: " + appliance.getName());

            statusUpdated = true;
        }
        if (!statusUpdated) return;
        notifyDiscoveryListener();
    }
    // ********** END SSDP METHODS ************

    // ********** START NETWORK METHODS ************
    private void markOtherNetworkAppliancesRemote(String ssid) {
        DICommLog.d(DICommLog.DISCOVERY, "Marking all paired appliances REMOTE that will not appear on network: " + ssid);
        boolean statusUpdated = false;
        for (DICommAppliance appliance : mAllAppliancesMap.values()) {
            if (!isConnectedLocally(appliance) &&
                    !isConnectedRemotely(appliance) &&
                    !isOnHomeSsid(ssid, appliance) &&
                    isPaired(appliance)) {
                appliance.getNetworkNode().setConnectionState(ConnectionState.CONNECTED_REMOTELY);
                statusUpdated = true;
                DICommLog.v(DICommLog.DISCOVERY, "Marked other network REMOTE: " + appliance.getName());
            }
        }
        if (!statusUpdated) return;
        notifyDiscoveryListener();
    }

    private boolean isOnHomeSsid(final String ssid, final DICommAppliance appliance) {
        return appliance.getNetworkNode().getHomeSsid() != null && appliance.getNetworkNode().getHomeSsid().equals(ssid);
    }

    private void markNonDiscoveredAppliancesRemote() {
        DICommLog.d(DICommLog.DISCOVERY, "Marking paired appliances that where not discovered locally REMOTE");
        boolean statusUpdated = false;
        for (DICommAppliance appliance : mAllAppliancesMap.values()) {
            if (!isConnectedLocally(appliance) &&
                    !isConnectedRemotely(appliance) &&
                    isPaired(appliance)) {
                appliance.getNetworkNode().setConnectionState(ConnectionState.CONNECTED_REMOTELY);
                statusUpdated = true;
                DICommLog.v(DICommLog.DISCOVERY, "Marked non discovered REMOTE: " + appliance.getName());
            }
        }
        if (!statusUpdated) return;
        notifyDiscoveryListener();
    }

    private boolean isPaired(final DICommAppliance appliance) {
        return appliance.getNetworkNode().getPairedState() == NetworkNode.PAIRED_STATUS.PAIRED;
    }

    private boolean isConnectedRemotely(final DICommAppliance appliance) {
        return appliance.getNetworkNode().getConnectionState() == ConnectionState.CONNECTED_REMOTELY;
    }

    private boolean isConnectedLocally(final DICommAppliance appliance) {
        return appliance.getNetworkNode().getConnectionState() == ConnectionState.CONNECTED_LOCALLY;
    }

    private void markAllAppliancesRemote() {
        DICommLog.d(DICommLog.DISCOVERY, "Marking all paired appliances REMOTE");
        boolean statusUpdated = false;
        for (DICommAppliance appliance : mAllAppliancesMap.values()) {
            if (isPaired(appliance)) {
                appliance.getNetworkNode().setConnectionState(ConnectionState.CONNECTED_REMOTELY);
                statusUpdated = true;
                DICommLog.v(DICommLog.DISCOVERY, "Marked paired/cpponline REMOTE: " + appliance.getName());
            } else {
                appliance.getNetworkNode().setConnectionState(ConnectionState.DISCONNECTED);
                statusUpdated = true;
                DICommLog.v(DICommLog.DISCOVERY, "Marked non paired/cppoffline DISCONNECTED: " + appliance.getName());
            }
        }
        if (!statusUpdated) return;
        notifyDiscoveryListener();
    }

    private void markAllAppliancesOffline() {
        DICommLog.d(DICommLog.DISCOVERY, "Marking all appliances OFFLINE");
        boolean statusUpdated = false;
        for (DICommAppliance appliance : mAllAppliancesMap.values()) {
            if (appliance.getNetworkNode().getConnectionState().equals(ConnectionState.DISCONNECTED))
                continue;

            appliance.getNetworkNode().setConnectionState(ConnectionState.DISCONNECTED);
            statusUpdated = true;
            DICommLog.v(DICommLog.DISCOVERY, "Marked OFFLINE: " + appliance.getName());
        }
        if (!statusUpdated) return;
        notifyDiscoveryListener();
    }
    // ********** END NETWORK METHODS ************

    // ********** START CPP METHODS ************
    public boolean updateConnectedStateViaCppAllAppliances(DiscoverInfo info) {
        DICommLog.i(DICommLog.DISCOVERY, "updateConnectedState");
        boolean connected = info.isConnected();
        boolean notifyListeners = false;

        List<String> cppIds = Arrays.asList(info.getClientIds());

        DICommLog.i(DICommLog.DISCOVERY, "List: " + cppIds);

        for (DICommAppliance appliances : getAllDiscoveredAppliances()) {
            boolean updatedState = false;
            boolean currentOnlineViaCpp = connected;
            if (cppIds.isEmpty()) {
                notifyListeners = updateConnectedStateOfflineViaCpp(appliances);
                continue;
            }
            if (!cppIds.contains(appliances.getNetworkNode().getCppId())) {
                currentOnlineViaCpp = !connected;
            }

            if (currentOnlineViaCpp) {
                updatedState = updateConnectedStateOnlineViaCpp(appliances);
            } else {
                updatedState = updateConnectedStateOfflineViaCpp(appliances);
            }
            if (updatedState) {
                notifyListeners = true;
            }
        }
        return notifyListeners;
    }

    public boolean updateConnectedStateViaCppReturnedAppliances(DiscoverInfo info) {
        boolean connected = info.isConnected();
        boolean notifyListeners = false;

        for (String cppIds : info.getClientIds()) {
            DICommAppliance appliance = getApplianceByCppId(cppIds);
            if (appliance == null) {
                DICommLog.v(DICommLog.DISCOVERY, "Received discover event for unknown appliance: " + cppIds);
                continue;
            }
            boolean isUpdated = false;
            if (connected) {
                isUpdated = updateConnectedStateOnlineViaCpp(appliance);
            } else {
                isUpdated = updateConnectedStateOfflineViaCpp(appliance);
            }
            if (isUpdated) {
                notifyListeners = true;
            }
        }
        return notifyListeners;
    }

    private boolean updateConnectedStateOnlineViaCpp(DICommAppliance appliance) {
        DICommLog.i(DICommLog.DISCOVERY, "updateConnectedStateOnlineViaCpp: " + appliance.getName());
        if (appliance.getNetworkNode().getPairedState() == NetworkNode.PAIRED_STATUS.NOT_PAIRED)
            return false;
        if (appliance.getNetworkNode().getConnectionState() != ConnectionState.DISCONNECTED)
            return false;
        if (mNetwork.getLastKnownNetworkState() == NetworkState.NONE) return false;

        appliance.getNetworkNode().setConnectionState(ConnectionState.CONNECTED_REMOTELY);
        DICommLog.v(DICommLog.DISCOVERY, "Marked Cpp online REMOTE: " + appliance.getName());
        return true;
    }

    private boolean updateConnectedStateOfflineViaCpp(DICommAppliance appliance) {
        DICommLog.i(DICommLog.DISCOVERY, "updateConnectedStateOfflineViaCpp: " + appliance.getName());
        if (appliance.getNetworkNode().getConnectionState() != ConnectionState.CONNECTED_REMOTELY)
            return false;

        appliance.getNetworkNode().setConnectionState(ConnectionState.DISCONNECTED);
        DICommLog.v(DICommLog.DISCOVERY, "Marked Cpp offline DISCONNECTED: " + appliance.getName());
        return true;
    }

    // ********** END CPP METHODS ************

    // ********** START ASYNC METHODS ************

    /**
     * Only needs to be done after a network change to Wifi network
     */
    private void connectViaCppAfterLocalAttemptDelayed() {
        DICommLog.i(DICommLog.DISCOVERY, "START delayed job to connect via cpp to appliances that did not appear local");
        mDiscoveryTimeoutHandler.sendEmptyMessageDelayed(DISCOVERY_WAITFORLOCAL_MESSAGE, DISCOVERY_WAITFORLOCAL_TIMEOUT);
    }

    private void cancelConnectViaCppAfterLocalAttempt() {
        if (mDiscoveryTimeoutHandler.hasMessages(DISCOVERY_WAITFORLOCAL_MESSAGE)) {
            mDiscoveryTimeoutHandler.removeMessages(DISCOVERY_WAITFORLOCAL_MESSAGE);
            DICommLog.i(DICommLog.DISCOVERY, "CANCEL delayed job to connect via cpp to appliances");
        }
    }

    /**
     * Only needs to be done after the SSDP service has actually stopped.
     * (appliances could have gone offline during the stopped period)
     */
    public void syncLocalAppliancesWithSsdpStackDelayed() {
        DICommLog.i(DICommLog.DISCOVERY, "START delayed job to mark local appliances offline that did not reappear after ssdp restart");
        mDiscoveryTimeoutHandler.sendEmptyMessageDelayed(DISCOVERY_SYNCLOCAL_MESSAGE, DISCOVERY_SYNCLOCAL_TIMEOUT);
    }

    public void cancelSyncLocalAppliancesWithSsdpStack() {
        if (mDiscoveryTimeoutHandler.hasMessages(DISCOVERY_SYNCLOCAL_MESSAGE)) {
            mDiscoveryTimeoutHandler.removeMessages(DISCOVERY_SYNCLOCAL_MESSAGE);
            DICommLog.i(DICommLog.DISCOVERY, "CANCEL delayed job to mark local appliances offline");
        }
    }
    // ********** END ASYNC METHODS ************

    private NetworkNode createNetworkNode(DeviceModel deviceModel) {
        SSDPdevice ssdpDevice = deviceModel.getSsdpDevice();
        if (ssdpDevice == null) return null;

        DICommLog.i(DICommLog.DISCOVERY, "Appliance discovered - name: " + ssdpDevice.getFriendlyName());
        String cppId = ssdpDevice.getCppId();
        String ipAddress = deviceModel.getIpAddress();
        String name = ssdpDevice.getFriendlyName();
        String modelName = ssdpDevice.getModelName();
        String networkSsid = mNetwork.getLastKnownNetworkSsid();
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

        if (!isValidNetworkNode(networkNode)) return null;

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

    private DeviceModel getDeviceModelFromMessage(Message msg) {
        if (msg == null) return null;

        try {
            DeviceModel device = (DeviceModel) ((InternalMessage) msg.obj).obj;
            return device;
        } catch (Exception e) {
            DICommLog.d(DICommLog.DISCOVERY, "Invalid appliance detected: " + "Error: " + e.getMessage());
        }
        return null;
    }

    private void initializeAppliancesMapFromDataBase() {
        DICommLog.i(DICommLog.DISCOVERY, "Initializing appliances from database");
        mAllAppliancesMap = new LinkedHashMap<String, T>();

        List<T> allAppliances = loadAllAddedAppliancesFromDatabase();
        List<NetworkNode> addedAppliances = new ArrayList<NetworkNode>();
        for (T appliance : allAppliances) {
            mAllAppliancesMap.put(appliance.getNetworkNode().getCppId(), appliance);
            addedAppliances.add(appliance.getNetworkNode());
        }
        mAddedAppliances = addedAppliances;
    }

    private void notifyDiscoveryListener() {
        if (mDiscoveryEventListenersList == null) return;

        for (DiscoveryEventListener listener : mDiscoveryEventListenersList) {
            listener.onDiscoveredAppliancesListChanged();
        }

        printDiscoveredAppliances(DICommLog.DISCOVERY);
        DICommLog.v(DICommLog.DISCOVERY, "Notified listener of change event");
    }

    public void printDiscoveredAppliances(String tag) {
        if (tag == null || tag.isEmpty()) {
            tag = DICommLog.DISCOVERY;
        }

        if (mAllAppliancesMap.size() <= 0) {
            DICommLog.d(tag, "No appliances discovered - map is 0");
            return;
        }

        String offline = "Offline appliances %d: ";
        String local = "Local appliances %d: ";
        String cpp = "Cpp appliances %d: ";
        for (DICommAppliance appliances : mAllAppliancesMap.values()) {
            switch (appliances.getNetworkNode().getConnectionState()) {
                case DISCONNECTED:
                    offline += appliances.getName() + ", ";
                    break;
                case CONNECTED_LOCALLY:
                    local += appliances.getName() + ", ";
                    break;
                case CONNECTED_REMOTELY:
                    cpp += appliances.getName() + ", ";
                    break;
            }
        }
        DICommLog.d(tag, String.format(offline, offline.length() - offline.replace(",", "").length()));
        DICommLog.d(tag, String.format(local, local.length() - local.replace(",", "").length()));
        DICommLog.d(tag, String.format(cpp, cpp.length() - cpp.replace(",", "").length()));
    }

    private List<T> loadAllAddedAppliancesFromDatabase() {
        List<T> result = new ArrayList<T>();

        List<NetworkNode> networkNodes = mNetworkNodeDatabase.getAll();

        for (NetworkNode networkNode : networkNodes) {
            if (!mApplianceFactory.canCreateApplianceForNode(networkNode)) {
                DICommLog.e(DICommLog.DISCOVERY, "Did not load appliance from database - factory cannot create appliance");
                continue;
            }

            final T appliance = mApplianceFactory.createApplianceForNode(networkNode);
            mApplianceDatabase.loadDataForAppliance(appliance);
            networkNode.setEncryptionKeyUpdatedListener(new EncryptionKeyUpdatedListener() {
                @Override
                public void onKeyUpdate() {
                    updateApplianceInDatabase(appliance);
                }
            });
            result.add(appliance);
        }
        return result;
    }

    // TODO DIComm refactor: improve interface
    public long insertApplianceToDatabase(T appliance) {
        long rowId = mNetworkNodeDatabase.save(appliance.getNetworkNode());
        mApplianceDatabase.save(appliance);

        return rowId;
    }

    // TODO DIComm refactor: improve interface
    public long updateApplianceInDatabase(T appliance) {
        if (!mNetworkNodeDatabase.contains(appliance.getNetworkNode())) {
            DICommLog.d(DICommLog.DISCOVERY, "Not updating NetworkNode database - not yet in database");
            return -1;
        }

        return insertApplianceToDatabase(appliance);
    }

    // TODO DIComm refactor: improve interface
    public int deleteApplianceFromDatabase(T appliance) {
        int rowsDeleted = mNetworkNodeDatabase.delete(appliance.getNetworkNode());
        mApplianceDatabase.delete(appliance);

        return rowsDeleted;
    }

    // ********** START TEST METHODS ************
    public static void setDummyDiscoveryManagerForTesting(DiscoveryManager<? extends DICommAppliance> dummyManager) {
        mInstance = dummyManager;
    }

    public void setDummyDiscoveryEventListenerForTesting(DiscoveryEventListener dummyListener) {
        mDiscoveryEventListenersList.add(dummyListener);
    }

    public void setDummyNetworkMonitorForTesting(NetworkMonitor dummyMonitor) {
        mNetwork = dummyMonitor;
    }

    public void setDummySsdpServiceHelperForTesting(SsdpServiceHelper dummyHelper) {
        mSsdpHelper = dummyHelper;
    }

    public void setAppliancesListForTesting(LinkedHashMap<String, T> testMap) {
        mAllAppliancesMap = testMap;
    }

    public Handler getDiscoveryTimeoutHandlerForTesting() {
        return mDiscoveryTimeoutHandler;
    }
    // ********** END TEST METHODS ************
}
