package com.philips.cl.di.dev.pa.newpurifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.philips.cdp.dicomm.cpp.CPPController;
import com.philips.cdp.dicomm.util.DICommContext;
import com.philips.cdp.dicommclient.networknode.ConnectionState;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.networknode.NetworkNodeDatabase;
import com.philips.cdp.dicommclient.networknode.NetworkNode.EncryptionKeyUpdatedListener;
import com.philips.cl.di.common.ssdp.contants.DiscoveryMessageID;
import com.philips.cl.di.common.ssdp.controller.InternalMessage;
import com.philips.cl.di.common.ssdp.lib.SsdpService;
import com.philips.cl.di.common.ssdp.models.DeviceModel;
import com.philips.cl.di.common.ssdp.models.SSDPdevice;
import com.philips.cl.di.dev.pa.cpp.CppDiscoverEventListener;
import com.philips.cl.di.dev.pa.cpp.CppDiscoveryHelper;
import com.philips.cl.di.dev.pa.datamodel.DiscoverInfo;
import com.philips.cl.di.dev.pa.datamodel.FirmwarePortProperties.FirmwareState;
import com.philips.cl.di.dev.pa.newpurifier.NetworkMonitor.NetworkChangedCallback;
import com.philips.cl.di.dev.pa.newpurifier.NetworkMonitor.NetworkState;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dicomm.appliance.DICommApplianceDatabase;
import com.philips.cl.di.dicomm.appliance.DICommApplianceFactory;
import com.philips.cl.di.dicomm.appliance.NullApplianceDatabase;

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
public class DiscoveryManager implements Callback, NetworkChangedCallback, CppDiscoverEventListener {

	private static DiscoveryManager mInstance;

	private LinkedHashMap<String, DICommAppliance> mAllAppliancesMap;
	private List<NetworkNode> mAddedAppliances;

	private DICommApplianceFactory<DICommAppliance> mApplianceFactory;
	private NetworkNodeDatabase mNetworkNodeDatabase;
	private DICommApplianceDatabase<DICommAppliance> mApplianceDatabase;

	private static final Object mDiscoveryLock = new Object();
	private NetworkMonitor mNetwork;
	private SsdpServiceHelper mSsdpHelper;
	private CppDiscoveryHelper mCppHelper;

	private DiscoveryEventListener mDiscoveryEventListener;
	private NewApplianceDiscoveredListener mNewApplianceDiscoveredListener;

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
			} else if(msg.what == DISCOVERY_SYNCLOCAL_MESSAGE) {
				synchronized (mDiscoveryLock) {
					DiscoveryManager.getInstance().markLostAppliancesInBackgroundOfflineOrRemote();
				}
			}
		};
	};

	public static synchronized void createSharedInstance(Context applicationContext, DICommApplianceFactory<? extends DICommAppliance> applianceFactory) {
		if (mInstance != null) {
			throw new RuntimeException("DiscoveryManager can only be initialized once");
		}
		DICommContext.initialize(applicationContext);
		mInstance = new DiscoveryManager(applianceFactory, new NullApplianceDatabase());
	}

	public static synchronized void createSharedInstance(Context applicationContext, DICommApplianceFactory<? extends DICommAppliance> applianceFactory, DICommApplianceDatabase<? extends DICommAppliance> applianceDatabase) {
		if (mInstance != null) {
			throw new RuntimeException("CPPController can only be initialized once");
		}
		DICommContext.initialize(applicationContext);
		mInstance = new DiscoveryManager(applianceFactory, applianceDatabase);
	}

	public static synchronized DiscoveryManager getInstance() {
		return mInstance;
	}

	@SuppressWarnings("unchecked")
	private DiscoveryManager(DICommApplianceFactory<? extends DICommAppliance> applianceFactory, DICommApplianceDatabase<? extends DICommAppliance> applianceDatabase) {
		mApplianceFactory = (DICommApplianceFactory<DICommAppliance>) applianceFactory;

		mApplianceDatabase = (DICommApplianceDatabase<DICommAppliance>) applianceDatabase;
		mNetworkNodeDatabase = new NetworkNodeDatabase();
		initializeAppliancesMapFromDataBase();

		mSsdpHelper = new SsdpServiceHelper(SsdpService.getInstance(), this);
		mCppHelper = new CppDiscoveryHelper(CPPController.getInstance(), this);

		// Starting network monitor will ensure a fist callback.
		mNetwork = new NetworkMonitor(DICommContext.getContext(), this);
	}

	public void start(DiscoveryEventListener listener) {
		if (mNetwork.getLastKnownNetworkState() == NetworkState.WIFI_WITH_INTERNET) {
			mSsdpHelper.startDiscoveryAsync();
			ALog.d(ALog.DISCOVERY, "Starting SSDP service - Start called (wifi_internet)");
		}
		mCppHelper.startDiscoveryViaCpp();
		mNetwork.startNetworkChangedReceiver(DICommContext.getContext());
		mDiscoveryEventListener = listener;
	}

	public void stop() {
		mSsdpHelper.stopDiscoveryAsync();
		ALog.d(ALog.DISCOVERY, "Stopping SSDP service - Stop called");
		mCppHelper.stopDiscoveryViaCpp();
		mNetwork.stopNetworkChangedReceiver(DICommContext.getContext());
		mDiscoveryEventListener = null;
	}

	public void setNewApplianceDiscoveredListener(NewApplianceDiscoveredListener newApplianceDiscoveredListener) {
		this.mNewApplianceDiscoveredListener = newApplianceDiscoveredListener;
	}

	public void clearNewApplianceDiscoveredListener() {
		this.mNewApplianceDiscoveredListener = null;
	}

	public ArrayList<DICommAppliance> getAllDiscoveredAppliances() {
		return new ArrayList<DICommAppliance>(mAllAppliancesMap.values());
	}

	public List<DICommAppliance> getAddedAppliances() {
		List<DICommAppliance> appliances = new ArrayList<DICommAppliance>();
		for (NetworkNode addedAppliance : mAddedAppliances) {
			appliances.add(mAllAppliancesMap.get(addedAppliance.getCppId()));
		}
		return appliances;
	}

	// TODO DIComm refactor: this method should be removed completely
	public List<DICommAppliance> updateAddedAppliances() {
		mAddedAppliances = mNetworkNodeDatabase.getAll();
		return getAddedAppliances();
	}

	// TODO DIComm refactor: this method should be removed from public interface
	public void removeFromDiscoveredList(String cppId) {
		if (cppId == null || cppId.isEmpty()) return;
		DICommAppliance appliance = mAllAppliancesMap.remove(cppId);
		if(appliance!=null){
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

	public List<DICommAppliance> getNewAppliancesDiscovered() {
		boolean addToNewApplianceList = true ;
		List<DICommAppliance> discoveredAppliances = getAllDiscoveredAppliances() ;
		List<DICommAppliance> addedAppliances = getAddedAppliances() ;
		List<DICommAppliance> newAppliances = new ArrayList<DICommAppliance>() ;

		for(DICommAppliance appliance: discoveredAppliances) {
			for( DICommAppliance addedAppliance: addedAppliances) {
				if( appliance.getNetworkNode().getCppId().equals(addedAppliance.getNetworkNode().getCppId())) {
					addToNewApplianceList = false;
					break;
				}
			}
			if(addToNewApplianceList) {
				//Add connected appliance only, ignore disconnected
				if (appliance.getNetworkNode().getConnectionState() != ConnectionState.DISCONNECTED) {
					appliance.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.NOT_PAIRED);
					newAppliances.add(appliance) ;
				}
			}
			addToNewApplianceList = true ;
		}
		return newAppliances ;
	}

	public DICommAppliance getApplianceByCppId(String cppId) {
		if (cppId == null || cppId.isEmpty()) return null;
		return mAllAppliancesMap.get(cppId);
	}

	@Override
	public void onNetworkChanged(NetworkState networkState, String networkSsid) {
		// REMARK: Wifi switch will go through the none state
		cancelConnectViaCppAfterLocalAttempt();
		synchronized (mDiscoveryLock) {
			switch(networkState) {
			case NONE :
				markAllAppliancesOffline();
				mSsdpHelper.stopDiscoveryImmediate();
				ALog.d(ALog.DISCOVERY, "Stopping SSDP service - Network change (no network)");
				break;
			case MOBILE:
				markAllAppliancesRemote();
				mSsdpHelper.stopDiscoveryImmediate();;
				ALog.d(ALog.DISCOVERY, "Stopping SSDP service - Network change (mobile data)");
				break;
			case WIFI_WITH_INTERNET:
				markOtherNetworkAppliancesRemote(networkSsid);
				connectViaCppAfterLocalAttemptDelayed();
				mSsdpHelper.startDiscoveryAsync();
				ALog.d(ALog.DISCOVERY, "Starting SSDP service - Network change (wifi internet)");
				break;
			default:
				break;
			}
		}

	}

	// TODO DIComm Refactor: investigate and fix commented code
	@Override
	public void onSignedOnViaCpp() {
		ALog.v(ALog.DISCOVERY, "Signed on to CPP - setting all appliances online via cpp");
		//updateAllAppliancesOnlineViaCpp();
	}

	// TODO DIComm Refactor: investigate and fix commented code
	@Override
	public void onSignedOffViaCpp() {
		ALog.v(ALog.DISCOVERY, "Signed on to CPP - setting all appliances offline via cpp");
//		updateAllAppliancesOfflineViaCpp();
	}

	@Override
	public void onDiscoverEventReceived(DiscoverInfo info, boolean isResponseToRequest) {
		if (info == null) return;
		ALog.v(ALog.DISCOVERY, "Received discover event from CPP: " + (isResponseToRequest ? "requested" : "change"));

		boolean notifyListeners = false;
		synchronized (mDiscoveryLock) {
			if (isResponseToRequest) {
				ALog.v(ALog.DISCOVERY, "Received connected appliances list via cpp");
				notifyListeners = updateConnectedStateViaCppAllAppliances(info);
			} else {
				ALog.v(ALog.DISCOVERY, "Received connected appliance update via CPP");
				notifyListeners = updateConnectedStateViaCppReturnedAppliances(info);
			}
		}

		if (notifyListeners) {
			notifyDiscoveryListener();
		}
	}

	/**
	 * Callback from SSDP service
	 */
	@Override
	public boolean handleMessage(Message msg) {
		if (msg == null) return false;

		final DiscoveryMessageID messageID = DiscoveryMessageID.getID(msg.what);
		DeviceModel device = getDeviceModelFromMessage(msg);
		if (device == null) return false;

		synchronized (mDiscoveryLock) {
			switch (messageID) {
			case DEVICE_DISCOVERED: return onApplianceDiscovered(device);
			case DEVICE_LOST: return onApplianceLost(device);
			default: break;
			}
		}
		return false;
	}


	// ********** START SSDP METHODS ************
	private boolean onApplianceDiscovered(DeviceModel deviceModel) {

		NetworkNode networkNode = createNetworkNode(deviceModel);
		if (networkNode == null) return false;
		ALog.i(ALog.SSDP, "Discovered appliance name: " + networkNode.getName());
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

		ArrayList<DICommAppliance> discoveredAppliances = getAllDiscoveredAppliances();
		for (DICommAppliance appliance : discoveredAppliances) {
			if (appliance.getNetworkNode().getCppId().equals(lostApplianceCppId)) {
				ALog.d(ALog.DISCOVERY, "Lost appliance - marking as DISCONNECTED: " + appliance);
				// TODO: DIComm Refactor check if can be removed
				if(appliance.getFirmwarePort().getPortProperties() != null && FirmwareState.IDLE != appliance.getFirmwarePort().getPortProperties().getState()) {
					return false;
				}
				appliance.getNetworkNode().setConnectionState(ConnectionState.DISCONNECTED);
				notifyDiscoveryListener();
				return true;
			}
		}
		return false;
	}

	private void updateExistingAppliance(NetworkNode networkNode) {
		DICommAppliance existingAppliance = mAllAppliancesMap.get(networkNode.getCppId());
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
			ALog.d(ALog.PAIRING, "Discovery-Boot id changed pairing set to false");
		}

        if (existingAppliance.getNetworkNode().getConnectionState() != networkNode.getConnectionState()) {
            existingAppliance.getNetworkNode().setConnectionState(networkNode.getConnectionState());
            notifyListeners = true;
        }

		if (notifyListeners) {
			notifyDiscoveryListener();
		}
		ALog.d(ALog.DISCOVERY, "Successfully updated appliance: " + existingAppliance);
	}

	/**
	 * Completely new appliance - never seen before
	 */
	private void addNewAppliance(NetworkNode networkNode) {
		if (!mApplianceFactory.canCreateApplianceForNode(networkNode)) {
			ALog.d(ALog.DISCOVERY, "Cannot create appliance for networknode: " + networkNode);
			return;
		}
		final DICommAppliance appliance = mApplianceFactory.createApplianceForNode(networkNode);
		appliance.getNetworkNode().setEncryptionKeyUpdatedListener(new EncryptionKeyUpdatedListener() {
	    	@Override
	    	public void onKeyUpdate() {
	    		updateApplianceInDatabase(appliance);
	    	}
		});

		mAllAppliancesMap.put(appliance.getNetworkNode().getCppId(), appliance);
		ALog.d(ALog.DISCOVERY, "Successfully added appliance: " + appliance);
		notifyDiscoveryListener();
	}

	public void markLostAppliancesInBackgroundOfflineOrRemote() {
		ALog.d(ALog.DISCOVERY, "Syncing appliances list for lost appliances in background");
		boolean statusUpdated = false;

		ArrayList<String> onlineCppIds = mSsdpHelper.getOnlineDevicesCppId();

		for (DICommAppliance appliance : mAllAppliancesMap.values()) {
			if (appliance.getNetworkNode().getConnectionState() == ConnectionState.DISCONNECTED) continue; // must be offline: not discovered
			if (appliance.getNetworkNode().getConnectionState() == ConnectionState.CONNECTED_REMOTELY) continue; // must be remote: not discovered
			if (onlineCppIds.contains(appliance.getNetworkNode().getCppId())) continue; // State is correct

			// Losing an appliance in the background means it is offline
			appliance.getNetworkNode().setConnectionState(ConnectionState.DISCONNECTED);
			ALog.v(ALog.DISCOVERY, "Marked non discovered DISCONNECTED: " + appliance.getName());

			statusUpdated = true;
		}
		if (!statusUpdated) return;
		notifyDiscoveryListener();
	}
	// ********** END SSDP METHODS ************


	// ********** START NETWORK METHODS ************
	private void markOtherNetworkAppliancesRemote(String ssid) {
		ALog.d(ALog.DISCOVERY, "Marking all paired appliances REMOTE that will not appear on network: " + ssid);
		boolean statusUpdated = false;
		for (DICommAppliance appliance : mAllAppliancesMap.values()) {
			if (appliance.getNetworkNode().getConnectionState() == ConnectionState.CONNECTED_LOCALLY) continue; // already discovered
			if (appliance.getNetworkNode().getConnectionState() == ConnectionState.CONNECTED_REMOTELY) continue; // already remote
			if (appliance.getNetworkNode().getHomeSsid() != null && appliance.getNetworkNode().getHomeSsid().equals(ssid)) continue; // will appear local on this network
			if (appliance.getNetworkNode().getPairedState() != NetworkNode.PAIRED_STATUS.PAIRED || !appliance.getNetworkNode().isOnlineViaCpp()) continue; // not paired or not online

			appliance.getNetworkNode().setConnectionState(ConnectionState.CONNECTED_REMOTELY);
			statusUpdated = true;
			ALog.v(ALog.DISCOVERY, "Marked other network REMOTE: " + appliance.getName());
		}
		if (!statusUpdated) return;
		notifyDiscoveryListener();
	}

	private void markNonDiscoveredAppliancesRemote() {
		ALog.d(ALog.DISCOVERY, "Marking paired appliances that where not discovered locally REMOTE");
		boolean statusUpdated = false;
		for (DICommAppliance appliance : mAllAppliancesMap.values()) {
			if (appliance.getNetworkNode().getConnectionState() == ConnectionState.CONNECTED_LOCALLY) continue; // already discovered
			if (appliance.getNetworkNode().getConnectionState() == ConnectionState.CONNECTED_REMOTELY) continue; // already remote
			if (appliance.getNetworkNode().getPairedState() != NetworkNode.PAIRED_STATUS.PAIRED || !appliance.getNetworkNode().isOnlineViaCpp()) continue; // not online via cpp

			appliance.getNetworkNode().setConnectionState(ConnectionState.CONNECTED_REMOTELY);
			statusUpdated = true;
			ALog.v(ALog.DISCOVERY, "Marked non discovered REMOTE: " + appliance.getName());
		}
		if (!statusUpdated) return;
		notifyDiscoveryListener();
	}

	private void markAllAppliancesRemote() {
		ALog.d(ALog.DISCOVERY, "Marking all paired appliances REMOTE");
		boolean statusUpdated = false;
		for (DICommAppliance appliance : mAllAppliancesMap.values()) {
			if (appliance.getNetworkNode().getPairedState()==NetworkNode.PAIRED_STATUS.PAIRED && appliance.getNetworkNode().isOnlineViaCpp()) {
				appliance.getNetworkNode().setConnectionState(ConnectionState.CONNECTED_REMOTELY);
				statusUpdated = true;
				ALog.v(ALog.DISCOVERY, "Marked paired/cpponline REMOTE: " + appliance.getName());
			} else {
				appliance.getNetworkNode().setConnectionState(ConnectionState.DISCONNECTED);
				statusUpdated = true;
				ALog.v(ALog.DISCOVERY, "Marked non paired/cppoffline DISCONNECTED: " + appliance.getName());
			}
		}
		if (!statusUpdated) return;
		notifyDiscoveryListener();
	}

	private void markAllAppliancesOffline() {
		ALog.d(ALog.DISCOVERY, "Marking all appliances OFFLINE");
		boolean statusUpdated = false;
		for (DICommAppliance appliance : mAllAppliancesMap.values()) {
			if (appliance.getNetworkNode().getConnectionState().equals(ConnectionState.DISCONNECTED)) continue;

			appliance.getNetworkNode().setConnectionState(ConnectionState.DISCONNECTED);
			statusUpdated = true;
			ALog.v(ALog.DISCOVERY, "Marked OFFLINE: " + appliance.getName());
		}
		if (!statusUpdated) return;
		notifyDiscoveryListener();
	}
	// ********** END NETWORK METHODS ************


	// ********** START CPP METHODS ************
	public boolean updateConnectedStateViaCppAllAppliances(DiscoverInfo info) {
		ALog.i(ALog.DISCOVERY, "updateConnectedState") ;
		boolean connected = info.isConnected();
		boolean notifyListeners = false;

		List<String> cppIds = Arrays.asList(info.getClientIds());

		ALog.i(ALog.DISCOVERY, "List: " + cppIds) ;

		for (DICommAppliance appliances : getAllDiscoveredAppliances()) {
			boolean updatedState = false ;
			boolean currentOnlineViaCpp = connected;
			if( cppIds.isEmpty() ) {
				notifyListeners = updateConnectedStateOfflineViaCpp(appliances);
				continue ;
			}
			if (!cppIds.contains(appliances.getNetworkNode().getCppId())) {
				currentOnlineViaCpp = !connected;
			}

			if (currentOnlineViaCpp)
			{
				updatedState = updateConnectedStateOnlineViaCpp(appliances);
			}
			else
			{
				updatedState = updateConnectedStateOfflineViaCpp(appliances);
			}
			if( updatedState ) {
				notifyListeners = true ;
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
				ALog.v(ALog.DISCOVERY, "Received discover event for unknown appliance: " + cppIds);
				continue;
			}
			boolean isUpdated = false;
			if (connected) {
				isUpdated = updateConnectedStateOnlineViaCpp(appliance) ;
			} else {
				isUpdated = updateConnectedStateOfflineViaCpp(appliance) ;
			}
			if( isUpdated ) {
				notifyListeners = true ;
			}
		}
		return notifyListeners;
	}

	private boolean updateConnectedStateOnlineViaCpp(DICommAppliance appliance) {
		ALog.i(ALog.DISCOVERY, "updateConnectedStateOnlineViaCpp: " + appliance.getName()) ;
		if (appliance.getNetworkNode().getPairedState()==NetworkNode.PAIRED_STATUS.NOT_PAIRED) return false;
		if (appliance.getNetworkNode().getConnectionState() != ConnectionState.DISCONNECTED) return false;
		if (mNetwork.getLastKnownNetworkState() == NetworkState.NONE) return false;

		appliance.getNetworkNode().setConnectionState(ConnectionState.CONNECTED_REMOTELY);
		appliance.getNetworkNode().setOnlineViaCpp(true);
		ALog.v(ALog.DISCOVERY, "Marked Cpp online REMOTE: " + appliance.getName());
		return true;
	}

	private boolean updateConnectedStateOfflineViaCpp(DICommAppliance appliance) {
		ALog.i(ALog.DISCOVERY, "updateConnectedStateOfflineViaCpp: " + appliance.getName()) ;
		if (appliance.getNetworkNode().getConnectionState() != ConnectionState.CONNECTED_REMOTELY) return false;

		appliance.getNetworkNode().setConnectionState(ConnectionState.DISCONNECTED);
		appliance.getNetworkNode().setOnlineViaCpp(false);
		ALog.v(ALog.DISCOVERY, "Marked Cpp offline DISCONNECTED: " + appliance.getName());
		return true;
	}

	// ********** END CPP METHODS ************

	// ********** START ASYNC METHODS ************
	/**
	 * Only needs to be done after a network change to Wifi network
	 */
	private void connectViaCppAfterLocalAttemptDelayed() {
		ALog.i(ALog.DISCOVERY, "START delayed job to connect via cpp to appliances that did not appear local");
		mDiscoveryTimeoutHandler.sendEmptyMessageDelayed(DISCOVERY_WAITFORLOCAL_MESSAGE, DISCOVERY_WAITFORLOCAL_TIMEOUT);
	}

	private void cancelConnectViaCppAfterLocalAttempt() {
		if (mDiscoveryTimeoutHandler.hasMessages(DISCOVERY_WAITFORLOCAL_MESSAGE)) {
			mDiscoveryTimeoutHandler.removeMessages(DISCOVERY_WAITFORLOCAL_MESSAGE);
			ALog.i(ALog.DISCOVERY, "CANCEL delayed job to connect via cpp to appliances");
		}
	}

	/**
	 * Only needs to be done after the SSDP service has actually stopped.
	 * (appliances could have gone offline during the stopped period)
	 */
	public void syncLocalAppliancesWithSsdpStackDelayed() {
		ALog.i(ALog.DISCOVERY, "START delayed job to mark local appliances offline that did not reappear after ssdp restart");
		mDiscoveryTimeoutHandler.sendEmptyMessageDelayed(DISCOVERY_SYNCLOCAL_MESSAGE, DISCOVERY_SYNCLOCAL_TIMEOUT);
	}

	public void cancelSyncLocalAppliancesWithSsdpStack() {
		if (mDiscoveryTimeoutHandler.hasMessages(DISCOVERY_SYNCLOCAL_MESSAGE)) {
			mDiscoveryTimeoutHandler.removeMessages(DISCOVERY_SYNCLOCAL_MESSAGE);
			ALog.i(ALog.DISCOVERY, "CANCEL delayed job to mark local appliances offline");
		}
	}
	// ********** END ASYNC METHODS ************

	private NetworkNode createNetworkNode(DeviceModel deviceModel) {
		SSDPdevice ssdpDevice = deviceModel.getSsdpDevice();
		if (ssdpDevice == null) return null;

		ALog.i(ALog.DISCOVERY, "Appliance discovered - name: " + ssdpDevice.getFriendlyName());
		String cppId = ssdpDevice.getCppId();
		String ipAddress = deviceModel.getIpAddress();
		String name = ssdpDevice.getFriendlyName();
		String modelName = ssdpDevice.getModelName();
		String networkSsid = mNetwork.getLastKnownNetworkSsid();
		Long bootId = -1l;
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
        networkNode.setModelName(modelName);
        networkNode.setConnectionState(ConnectionState.CONNECTED_LOCALLY);
		networkNode.setHomeSsid(networkSsid);


		if (!isValidNetworkNode(networkNode)) return null;

		return networkNode;
	}

	private boolean isValidNetworkNode(NetworkNode networkNode) {
		if (networkNode.getCppId() == null || networkNode.getCppId().isEmpty()) {
			ALog.d(ALog.DISCOVERY, "Not a valid networkNode - cppId is null");
			return false;
		}
		if (networkNode.getIpAddress() == null || networkNode.getIpAddress().isEmpty()) {
			ALog.d(ALog.DISCOVERY, "Not a valid networkNode - ipAddress is null");
			return false;
		}
		if (networkNode.getName() == null || networkNode.getName().isEmpty()) {
			ALog.d(ALog.DISCOVERY, "Not a valid networkNode - name is null");
			return false;
		}
		if (networkNode.getModelName() == null || networkNode.getModelName().isEmpty()) {
			ALog.d(ALog.DISCOVERY, "Not a valid networkNode - modelName is null");
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
			ALog.d(ALog.DISCOVERY, "Invalid appliance detected: " + "Error: " + e.getMessage());
		}
		return null;
	}

	private void initializeAppliancesMapFromDataBase() {
		ALog.i(ALog.DISCOVERY, "Initializing appliances from database") ;
		mAllAppliancesMap = new LinkedHashMap<String, DICommAppliance>();

		List<DICommAppliance> allAppliances = loadAllAddedAppliancesFromDatabase();
		List<NetworkNode> addedAppliances = new ArrayList<NetworkNode>();
		for (DICommAppliance appliance : allAppliances) {
			mAllAppliancesMap.put(appliance.getNetworkNode().getCppId(), appliance);
			addedAppliances.add(appliance.getNetworkNode());
		}
		mAddedAppliances = addedAppliances;
	}

	private void notifyDiscoveryListener() {
		if (mDiscoveryEventListener == null) return;
		mDiscoveryEventListener.onDiscoveredAppliancesListChanged();

		notifyNewApplianceDiscoveredListener();
		printDiscoveredAppliances(ALog.DISCOVERY);
		ALog.v(ALog.DISCOVERY, "Notified listener of change event");
	}

	private void notifyNewApplianceDiscoveredListener() {
		if (mNewApplianceDiscoveredListener != null) {
			mNewApplianceDiscoveredListener.onNewApplianceDiscovered();
		}
	}

	public void printDiscoveredAppliances(String tag) {
		if (tag == null || tag.isEmpty()) {
			tag = ALog.DISCOVERY;
		}

		if (mAllAppliancesMap.size() <= 0) {
			ALog.d(tag, "No appliances discovered - map is 0");
			return;
		}

		String offline = "Offline appliances %d: ";
		String local = "Local appliances %d: ";
		String cpp = "Cpp appliances %d: ";
		for (DICommAppliance appliances : mAllAppliancesMap.values()) {
			switch (appliances.getNetworkNode().getConnectionState()) {
			case DISCONNECTED: offline += appliances.getName() + ", "; break;
			case CONNECTED_LOCALLY: local += appliances.getName() + ", "; break;
			case CONNECTED_REMOTELY: cpp += appliances.getName() + ", "; break;
			}
		}
		ALog.d(tag, String.format(offline, offline.length() - offline.replace(",", "").length()));
		ALog.d(tag, String.format(local, local.length() - local.replace(",", "").length()));
		ALog.d(tag, String.format(cpp, cpp.length() - cpp.replace(",", "").length()));
	}

	private List<DICommAppliance> loadAllAddedAppliancesFromDatabase() {
		List<DICommAppliance> result = new ArrayList<DICommAppliance>();

		List<NetworkNode> networkNodes = mNetworkNodeDatabase.getAll();

		for (NetworkNode networkNode : networkNodes) {
			if (!mApplianceFactory.canCreateApplianceForNode(networkNode)) {
				ALog.e(ALog.DISCOVERY, "Did not load appliance from database - factory cannot create appliance");
				continue;
			}

			final DICommAppliance appliance = mApplianceFactory.createApplianceForNode(networkNode);
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
	public long insertApplianceToDatabase(DICommAppliance appliance) {
		long rowId = mNetworkNodeDatabase.save(appliance.getNetworkNode());
		mApplianceDatabase.save(appliance);

		return rowId;
	}

	// TODO DIComm refactor: improve interface
	public long updateApplianceInDatabase(DICommAppliance appliance) {
		if (!mNetworkNodeDatabase.contains(appliance.getNetworkNode())) {
			ALog.d(ALog.DISCOVERY, "Not updating NetworkNode database - not yet in database");
			return -1;
		}

		long rowId = mNetworkNodeDatabase.save(appliance.getNetworkNode());
		mApplianceDatabase.save(appliance);

		return rowId;
	}

	// TODO DIComm refactor: improve interface
	public int deleteApplianceFromDatabase(DICommAppliance appliance) {
		int rowsDeleted = mNetworkNodeDatabase.delete(appliance.getNetworkNode());
		mApplianceDatabase.delete(appliance);

		return rowsDeleted;
	}

	// ********** START TEST METHODS ************
	public static void setDummyDiscoveryManagerForTesting(DiscoveryManager dummyManager) {
		mInstance = dummyManager;
	}

	public void setDummyDiscoveryEventListenerForTesting(DiscoveryEventListener dummyListener) {
		mDiscoveryEventListener = dummyListener;
	}

	public void setDummyNetworkMonitorForTesting(NetworkMonitor dummyMonitor) {
		mNetwork = dummyMonitor;
	}

	public void setDummySsdpServiceHelperForTesting(SsdpServiceHelper dummyHelper) {
		mSsdpHelper = dummyHelper;
	}
	public void setDummyCppDiscoveryHelperForTesting(CppDiscoveryHelper dummyHelper) {
		mCppHelper = dummyHelper;
	}

	public void setAppliancesListForTesting(LinkedHashMap<String, DICommAppliance> testMap) {
		mAllAppliancesMap = testMap;
	}

	public Handler getDiscoveryTimeoutHandlerForTesting() {
		return mDiscoveryTimeoutHandler;
	}
	// ********** END TEST METHODS ************
}
