package com.philips.cl.di.dev.pa.newpurifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import android.content.Context;
import android.location.Location;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;

import com.philips.cl.di.common.ssdp.contants.DiscoveryMessageID;
import com.philips.cl.di.common.ssdp.controller.InternalMessage;
import com.philips.cl.di.common.ssdp.lib.SsdpService;
import com.philips.cl.di.common.ssdp.models.DeviceModel;
import com.philips.cl.di.common.ssdp.models.SSDPdevice;
import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.activity.MainActivity;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.cpp.CPPController;
import com.philips.cl.di.dev.pa.cpp.CppDiscoverEventListener;
import com.philips.cl.di.dev.pa.cpp.CppDiscoveryHelper;
import com.philips.cl.di.dev.pa.dashboard.OutdoorController;
import com.philips.cl.di.dev.pa.datamodel.DiscoverInfo;
import com.philips.cl.di.dev.pa.datamodel.FirmwarePortProperties.FirmwareState;
import com.philips.cl.di.dev.pa.datamodel.SessionDto;
import com.philips.cl.di.dev.pa.newpurifier.NetworkMonitor.NetworkChangedCallback;
import com.philips.cl.di.dev.pa.newpurifier.NetworkMonitor.NetworkState;
import com.philips.cl.di.dev.pa.newpurifier.NetworkNode.EncryptionKeyUpdatedListener;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.DataParser;
import com.philips.cl.di.dicomm.appliance.DICommApplianceDatabase;
import com.philips.cl.di.dicomm.appliance.DICommApplianceFactory;
import com.philips.cl.di.dicomm.appliance.NetworkNodeDatabase;
import com.philips.cl.di.dicomm.appliance.NullApplianceDatabase;
import com.philips.cl.di.dicomm.communication.CommunicationMarshal;
import com.philips.cl.di.dicomm.security.DISecurity;
import com.philips.cl.di.dicomm.util.DICommContext;

/**
 * Discovery of the device is managed by Discovery Manager. It is the main
 * interface to the User Interface. The output of Discovery Manager is the list
 * of PurAirDevice which is further handled by User Interface and Purifier
 * Manager. In order to build this list, the Discovery Manager makes use of
 * input from SSDP, a pairing database and network changes.
 *
 * @author Jeroen Mols
 * @date 30 Apr 2014
 */
public class DiscoveryManager implements Callback, NetworkChangedCallback, CppDiscoverEventListener {

	private static DiscoveryManager mInstance;

	private LinkedHashMap<String, DICommAppliance> mAllAppliancesMap;
	private List<DICommAppliance> mAddedAppliances;

	private DICommApplianceFactory<DICommAppliance> mApplianceFactory;
	private NetworkNodeDatabase mNetworkNodeDatabase;
	private DICommApplianceDatabase<DICommAppliance> mApplianceDatabase;

	private static final Object mDiscoveryLock = new Object();
	private NetworkMonitor mNetwork;
	private SsdpServiceHelper mSsdpHelper;
	private CppDiscoveryHelper mCppHelper;

	private DiscoveryEventListener mListener;
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
					DiscoveryManager.getInstance().markNonDiscoveredDevicesRemote();
				}
			} else if(msg.what == DISCOVERY_SYNCLOCAL_MESSAGE) {
				synchronized (mDiscoveryLock) {
					DiscoveryManager.getInstance().markLostDevicesInBackgroundOfflineOrRemote();
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
		initializeDevicesMapFromDataBase();

		mSsdpHelper = new SsdpServiceHelper(SsdpService.getInstance(), this);
		mCppHelper = new CppDiscoveryHelper(CPPController.getInstance(PurAirApplication.getAppContext()), this);

		// Starting network monitor will ensure a fist callback.
		mNetwork = new NetworkMonitor(PurAirApplication.getAppContext(), this);
	}

	public void start(DiscoveryEventListener listener) {
		if (mNetwork.getLastKnownNetworkState() == NetworkState.WIFI_WITH_INTERNET) {
			mSsdpHelper.startDiscoveryAsync();
			ALog.d(ALog.DISCOVERY, "Starting SSDP service - Start called (wifi_internet)");
		}
		mCppHelper.startDiscoveryViaCpp();
		mNetwork.startNetworkChangedReceiver(PurAirApplication.getAppContext());
		mListener = listener;
	}

	public void stop() {
		mSsdpHelper.stopDiscoveryAsync();
		ALog.d(ALog.DISCOVERY, "Stopping SSDP service - Stop called");
		mCppHelper.stopDiscoveryViaCpp();
		mNetwork.stopNetworkChangedReceiver(PurAirApplication.getAppContext());
		mListener = null;
	}

	public void setAddNewPurifierListener(NewApplianceDiscoveredListener addNewPurifierListener) {
		this.mNewApplianceDiscoveredListener = addNewPurifierListener;
	}

	public void removeAddNewPurifierListener() {
		this.mNewApplianceDiscoveredListener = null;
	}

	public ArrayList<DICommAppliance> getDiscoveredDevices() {
		return new ArrayList<DICommAppliance>(mAllAppliancesMap.values());
	}

	public List<DICommAppliance> getStoreDevices() {
		List<DICommAppliance> appliances = new ArrayList<DICommAppliance>();
		for (DICommAppliance purAirDevice : mAddedAppliances) {
			appliances.add(mAllAppliancesMap.get(purAirDevice.getNetworkNode().getCppId()));
		}
		return appliances;
	}

	// TODO DIComm Refactor never return different object for same appliance, should return getStoreDevices()
	public List<DICommAppliance> updateStoreDevices() {
		mAddedAppliances = getAllAirPurifiers();
		for (final DICommAppliance appliance : mAddedAppliances) {
		    appliance.getNetworkNode().setEncryptionKeyUpdatedListener(new EncryptionKeyUpdatedListener() {
                @Override
                public void onKeyUpdate() {
                	updateApplianceInDatabase(appliance);
                }
            });
        }
		return mAddedAppliances;
	}

	public void removeFromDiscoveredList(String eui64) {
		if (eui64 == null || eui64.isEmpty()) return;
		DICommAppliance appliance = mAllAppliancesMap.remove(eui64);
		if(appliance!=null){
		    appliance.getNetworkNode().setEncryptionKeyUpdatedListener(null);
		}
	}

	public void updatePairingStatus(String eui64, NetworkNode.PAIRED_STATUS state) {
		if (eui64 == null || eui64.isEmpty()) return;
		if (mAllAppliancesMap.containsKey(eui64)) {
			mAllAppliancesMap.get(eui64).getNetworkNode().setPairedState(state);
		}
	}

	public List<DICommAppliance> getNewDevicesDiscovered() {
		boolean addToNewDeviceList = true ;
		List<DICommAppliance> discoveredDevices = getDiscoveredDevices() ;
		List<DICommAppliance> devicesInDataBase = getStoreDevices() ;
		List<DICommAppliance> newDevices = new ArrayList<DICommAppliance>() ;

		for(DICommAppliance device: discoveredDevices) {
			for( DICommAppliance deviceInDB: devicesInDataBase) {
				if( device.getNetworkNode().getCppId().equals(deviceInDB.getNetworkNode().getCppId())) {
					addToNewDeviceList = false;
					break;
				}
			}
			if(addToNewDeviceList) {
				//Add connected device only, ignore disconnected
				if (device.getNetworkNode().getConnectionState() != ConnectionState.DISCONNECTED) {
					device.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.NOT_PAIRED);
					newDevices.add(device) ;
				}
			}
			addToNewDeviceList = true ;
		}
		return newDevices ;
	}

	public DICommAppliance getDeviceByEui64(String eui64) {
		if (eui64 == null || eui64.isEmpty()) return null;
		return mAllAppliancesMap.get(eui64);
	}

	@Override
	public void onNetworkChanged(NetworkState networkState, String networkSsid) {
		// REMARK: Wifi switch will go through the none state
		cancelConnectViaCppAfterLocalAttempt();
		synchronized (mDiscoveryLock) {
			switch(networkState) {
			case NONE :
				markAllDevicesOffline();
				mSsdpHelper.stopDiscoveryImmediate();
				ALog.d(ALog.DISCOVERY, "Stopping SSDP service - Network change (no network)");
				break;
			case MOBILE:
				markAllDevicesRemote();
				mSsdpHelper.stopDiscoveryImmediate();;
				ALog.d(ALog.DISCOVERY, "Stopping SSDP service - Network change (mobile data)");
				break;
			case WIFI_WITH_INTERNET:
				markOtherNetworkDevicesRemote(networkSsid);
				connectViaCppAfterLocalAttemptDelayed();
				mSsdpHelper.startDiscoveryAsync();
				ALog.d(ALog.DISCOVERY, "Starting SSDP service - Network change (wifi internet)");
				break;
			default:
				break;
			}
		}

	}

	@Override
	public void onSignedOnViaCpp() {
		ALog.v(ALog.DISCOVERY, "Signed on to CPP - setting all purifiers online via cpp");
		//updateAllPurifiersOnlineViaCpp();
	}

	@Override
	public void onSignedOffViaCpp() {
		ALog.v(ALog.DISCOVERY, "Signed on to CPP - setting all purifiers offline via cpp");
//		updateAllPurifiersOfflineViaCpp();
	}

	@Override
	public void onDiscoverEventReceived(String data, boolean isResponseToRequest) {
		DiscoverInfo info = DataParser.parseDiscoverInfo(data);
		if (info == null) return;
		ALog.v(ALog.DISCOVERY, "Received discover event from CPP: " + (isResponseToRequest ? "requested" : "change"));

		boolean notifyListeners = false;
		synchronized (mDiscoveryLock) {
			if (isResponseToRequest) {
				ALog.v(ALog.DISCOVERY, "Received connected devices list via cpp");
				notifyListeners = updateConnectedStateViaCppAllPurifiers(info);
			} else {
				ALog.v(ALog.DISCOVERY, "Received connection update via CPP");
				notifyListeners = updateConnectedStateViaCppReturnedPurifiers(info);
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
			case DEVICE_DISCOVERED: return onDeviceDiscovered(device);
			case DEVICE_LOST: return onDeviceLost(device);
			default: break;
			}
		}
		return false;
	}


	// ********** START SSDP METHODS ************
	private boolean onDeviceDiscovered(DeviceModel deviceModel) {

		DICommAppliance purifier = getPurAirDevice(deviceModel);
		if (purifier == null) return false;
		ALog.i(ALog.SSDP, "Discovered device name: " + purifier.getNetworkNode().getName());
		if (mAllAppliancesMap.containsKey(purifier.getNetworkNode().getCppId())) {
			updateExistingDevice(purifier);
		} else {
			addNewDevice(purifier);
		}

		return true;
	}

	private boolean onDeviceLost(DeviceModel deviceModel) {
		String lostDeviceUsn = deviceModel.getId();
		if (lostDeviceUsn == null || lostDeviceUsn.length() <= 0) return false;

		ArrayList<DICommAppliance> devices = getDiscoveredDevices();
		for (DICommAppliance purifier : devices) {
			if (((AirPurifier)purifier).getNetworkNode().getCppId().equals(lostDeviceUsn)) {
				ALog.d(ALog.DISCOVERY, "Lost purifier - marking as DISCONNECTED: " + purifier);
				if(purifier.getFirmwarePort().getPortProperties() != null && FirmwareState.IDLE != purifier.getFirmwarePort().getPortProperties().getState()) {
					return false;
				}
				purifier.getNetworkNode().setConnectionState(ConnectionState.DISCONNECTED);
				//Clear indoor AQI historic data
				SessionDto.getInstance().setIndoorTrendDto(purifier.getNetworkNode().getCppId(), null);
				notifyDiscoveryListener();
				return true;
			}
		}
		return false;
	}

	private void updateExistingDevice(DICommAppliance newAppliance) {
		DICommAppliance existingAppliance = mAllAppliancesMap.get(newAppliance.getNetworkNode().getCppId());
		boolean notifyListeners = true;

		if (newAppliance.getNetworkNode().getHomeSsid() != null &&
				!newAppliance.getNetworkNode().getHomeSsid().equals(existingAppliance.getNetworkNode().getHomeSsid())) {
			existingAppliance.getNetworkNode().setHomeSsid(newAppliance.getNetworkNode().getHomeSsid());
			updateApplianceInDatabase(existingAppliance);
		}

		if (!newAppliance.getNetworkNode().getIpAddress().equals(existingAppliance.getNetworkNode().getIpAddress())) {
			existingAppliance.getNetworkNode().setIpAddress(newAppliance.getNetworkNode().getIpAddress());
		}

		if (!existingAppliance.getNetworkNode().getName().equals(newAppliance.getNetworkNode().getName())) {
			existingAppliance.getNetworkNode().setName(newAppliance.getNetworkNode().getName());
			updateApplianceInDatabase(existingAppliance);
			notifyListeners = true;
		}

		//If current location latitude and longitude null, then update
		if (((AirPurifier) existingAppliance).getLatitude() == null && ((AirPurifier) existingAppliance).getLongitude() == null) {
			Location location = OutdoorController.getInstance().getCurrentLocation();
			if (location != null) {
				((AirPurifier) existingAppliance).setLatitude(String.valueOf(location.getLatitude()));
				((AirPurifier) existingAppliance).setLongitude(String.valueOf(location.getLongitude()));
				updateApplianceInDatabase(existingAppliance);
				notifyListeners = true;
			}
		}

		if (existingAppliance.getNetworkNode().getBootId() != newAppliance.getNetworkNode().getBootId() || existingAppliance.getNetworkNode().getEncryptionKey() == null) {
			existingAppliance.getNetworkNode().setEncryptionKey(null);
			existingAppliance.getNetworkNode().setBootId(newAppliance.getNetworkNode().getBootId());
			existingAppliance.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.NOT_PAIRED);
			ALog.d(ALog.PAIRING, "Discovery-Boot id changed pairing set to false");
		}

        if (existingAppliance.getNetworkNode().getConnectionState() != newAppliance.getNetworkNode().getConnectionState()) {
            existingAppliance.getNetworkNode().setConnectionState(newAppliance.getNetworkNode().getConnectionState());
            notifyListeners = true;
        }

		if (notifyListeners) {
			notifyDiscoveryListener();
		}
		ALog.d(ALog.DISCOVERY, "Successfully updated purifier: " + existingAppliance);
	}

	/**
	 * Completely new device - never seen before
	 */
	private void addNewDevice(DICommAppliance purifier) {
		mAllAppliancesMap.put(purifier.getNetworkNode().getCppId(), purifier);
		ALog.d(ALog.DISCOVERY, "Successfully added purifier: " + purifier);
		notifyDiscoveryListener();
	}

	public void markLostDevicesInBackgroundOfflineOrRemote() {
		ALog.d(ALog.DISCOVERY, "Syncing local list after app went to background");
		boolean statusUpdated = false;

		ArrayList<String> onlineCppIds = mSsdpHelper.getOnlineDevicesEui64();

		for (DICommAppliance appliance : mAllAppliancesMap.values()) {
			if (appliance.getNetworkNode().getConnectionState() == ConnectionState.DISCONNECTED) continue; // must be offline: not discovered
			if (appliance.getNetworkNode().getConnectionState() == ConnectionState.CONNECTED_REMOTELY) continue; // must be remote: not discovered
			if (onlineCppIds.contains(appliance.getNetworkNode().getCppId())) continue; // State is correct

			// Losing a device in the background means it is offline
			appliance.getNetworkNode().setConnectionState(ConnectionState.DISCONNECTED);
			ALog.v(ALog.DISCOVERY, "Marked non discovered DISCONNECTED: " + appliance.getNetworkNode().getName());

			statusUpdated = true;
		}
		if (!statusUpdated) return;
		notifyDiscoveryListener();
	}
	// ********** END SSDP METHODS ************


	// ********** START NETWORK METHODS ************
	private void markOtherNetworkDevicesRemote(String ssid) {
		ALog.d(ALog.DISCOVERY, "Marking all paired devices REMOTE that will not appear on network: " + ssid);
		boolean statusUpdated = false;
		for (DICommAppliance appliance : mAllAppliancesMap.values()) {
			if (appliance.getNetworkNode().getConnectionState() == ConnectionState.CONNECTED_LOCALLY) continue; // already discovered
			if (appliance.getNetworkNode().getConnectionState() == ConnectionState.CONNECTED_REMOTELY) continue; // already remote
			if (appliance.getNetworkNode().getHomeSsid() != null && appliance.getNetworkNode().getHomeSsid().equals(ssid)) continue; // will appear local on this network
			if (appliance.getNetworkNode().getPairedState() != NetworkNode.PAIRED_STATUS.PAIRED || !appliance.getNetworkNode().isOnlineViaCpp()) continue; // not paired or not online

			appliance.getNetworkNode().setConnectionState(ConnectionState.CONNECTED_REMOTELY);
			statusUpdated = true;
			ALog.v(ALog.DISCOVERY, "Marked other network REMOTE: " + appliance.getNetworkNode().getName());
		}
		if (!statusUpdated) return;
		notifyDiscoveryListener();
	}

	private void markNonDiscoveredDevicesRemote() {
		ALog.d(ALog.DISCOVERY, "Marking paired devices that where not discovered locally REMOTE");
		boolean statusUpdated = false;
		for (DICommAppliance appliance : mAllAppliancesMap.values()) {
			if (appliance.getNetworkNode().getConnectionState() == ConnectionState.CONNECTED_LOCALLY) continue; // already discovered
			if (appliance.getNetworkNode().getConnectionState() == ConnectionState.CONNECTED_REMOTELY) continue; // already remote
			if (appliance.getNetworkNode().getPairedState() != NetworkNode.PAIRED_STATUS.PAIRED || !appliance.getNetworkNode().isOnlineViaCpp()) continue; // not online via cpp

			appliance.getNetworkNode().setConnectionState(ConnectionState.CONNECTED_REMOTELY);
			statusUpdated = true;
			ALog.v(ALog.DISCOVERY, "Marked non discovered REMOTE: " + appliance.getNetworkNode().getName());
		}
		if (!statusUpdated) return;
		notifyDiscoveryListener();
	}

	private void markAllDevicesRemote() {
		ALog.d(ALog.DISCOVERY, "Marking all paired devices REMOTE");
		boolean statusUpdated = false;
		for (DICommAppliance appliance : mAllAppliancesMap.values()) {
			if (appliance.getNetworkNode().getPairedState()==NetworkNode.PAIRED_STATUS.PAIRED && appliance.getNetworkNode().isOnlineViaCpp()) {
				appliance.getNetworkNode().setConnectionState(ConnectionState.CONNECTED_REMOTELY);
				statusUpdated = true;
				ALog.v(ALog.DISCOVERY, "Marked paired/cpponline REMOTE: " + appliance.getNetworkNode().getName());
			} else {
				appliance.getNetworkNode().setConnectionState(ConnectionState.DISCONNECTED);
				statusUpdated = true;
				ALog.v(ALog.DISCOVERY, "Marked non paired/cppoffline DISCONNECTED: " + appliance.getNetworkNode().getName());
			}
		}
		if (!statusUpdated) return;
		notifyDiscoveryListener();
	}

	private void markAllDevicesOffline() {
		ALog.d(ALog.DISCOVERY, "Marking all devices OFFLINE");
		boolean statusUpdated = false;
		for (DICommAppliance device : mAllAppliancesMap.values()) {
			if (device.getNetworkNode().getConnectionState().equals(ConnectionState.DISCONNECTED)) continue;

			device.getNetworkNode().setConnectionState(ConnectionState.DISCONNECTED);
			statusUpdated = true;
			ALog.v(ALog.DISCOVERY, "Marked OFFLINE: " + device.getNetworkNode().getName());
		}
		if (!statusUpdated) return;
		notifyDiscoveryListener();
	}
	// ********** END NETWORK METHODS ************


	// ********** START CPP METHODS ************
	public boolean updateConnectedStateViaCppAllPurifiers(DiscoverInfo info) {
		ALog.i(ALog.DISCOVERY, "updateConnectedState") ;
		boolean connected = info.isConnected();
		boolean notifyListeners = false;

		List<String> eui64s = Arrays.asList(info.getClientIds());

		ALog.i(ALog.DISCOVERY, "List: "+eui64s) ;

		for (DICommAppliance current : getDiscoveredDevices()) {
			boolean updatedState = false ;
			boolean currentOnlineViaCpp = connected;
			if( eui64s.isEmpty() ) {
				notifyListeners = updateConnectedStateOfflineViaCpp(current);
				continue ;
			}
			if (!eui64s.contains(current.getNetworkNode().getCppId())) {
				currentOnlineViaCpp = !connected;
			}

			if (currentOnlineViaCpp)
			{
				updatedState = updateConnectedStateOnlineViaCpp(current);
			}
			else
			{
				updatedState = updateConnectedStateOfflineViaCpp(current);
			}
			if( updatedState ) {
				notifyListeners = true ;
			}
		}
		return notifyListeners;
	}

	public boolean updateConnectedStateViaCppReturnedPurifiers(DiscoverInfo info) {
		boolean connected = info.isConnected();
		boolean notifyListeners = false;

		for (String eui64 : info.getClientIds()) {
			DICommAppliance current = getDeviceByEui64(eui64);
			if (current == null) {
				ALog.v(ALog.DISCOVERY, "Received discover event for unknown purifier: " + eui64);
				continue;
			}
			boolean isUpdated = false;
			if (connected) {
				isUpdated = updateConnectedStateOnlineViaCpp(current) ;
			} else {
				isUpdated = updateConnectedStateOfflineViaCpp(current) ;
			}
			if( isUpdated ) {
				notifyListeners = true ;
			}
		}
		return notifyListeners;
	}

	private boolean updateConnectedStateOnlineViaCpp(DICommAppliance purifier) {
		ALog.i(ALog.DISCOVERY, "updateConnectedStateOnlineViaCpp: "+purifier) ;
		if (purifier.getNetworkNode().getPairedState()==NetworkNode.PAIRED_STATUS.NOT_PAIRED) return false;
		if (purifier.getNetworkNode().getConnectionState() != ConnectionState.DISCONNECTED) return false;
		if (mNetwork.getLastKnownNetworkState() == NetworkState.NONE) return false;

		purifier.getNetworkNode().setConnectionState(ConnectionState.CONNECTED_REMOTELY);
		purifier.getNetworkNode().setOnlineViaCpp(true);
		ALog.v(ALog.DISCOVERY, "Marked Cpp online REMOTE: " + purifier.getNetworkNode().getName());
		return true;
	}

	private boolean updateConnectedStateOfflineViaCpp(DICommAppliance purifier) {
		ALog.i(ALog.DISCOVERY, "updateConnectedStateOfflineViaCpp: "+purifier) ;
		if (purifier.getNetworkNode().getConnectionState() != ConnectionState.CONNECTED_REMOTELY) return false;

		purifier.getNetworkNode().setConnectionState(ConnectionState.DISCONNECTED);
		purifier.getNetworkNode().setOnlineViaCpp(false);
		ALog.v(ALog.DISCOVERY, "Marked Cpp offline DISCONNECTED: " + purifier.getNetworkNode().getName());
		return true;
	}

	// ********** END CPP METHODS ************

	// ********** START ASYNC METHODS ************
	/**
	 * Only needs to be done after a network change to Wifi network
	 */
	private void connectViaCppAfterLocalAttemptDelayed() {
		ALog.i(ALog.DISCOVERY, "START delayed job to connect via cpp to devices that did not appear local");
		mDiscoveryTimeoutHandler.sendEmptyMessageDelayed(DISCOVERY_WAITFORLOCAL_MESSAGE, DISCOVERY_WAITFORLOCAL_TIMEOUT);
	}

	private void cancelConnectViaCppAfterLocalAttempt() {
		if (mDiscoveryTimeoutHandler.hasMessages(DISCOVERY_WAITFORLOCAL_MESSAGE)) {
			mDiscoveryTimeoutHandler.removeMessages(DISCOVERY_WAITFORLOCAL_MESSAGE);
			ALog.i(ALog.DISCOVERY, "CANCEL delayed job to connect via cpp to devices");
		}
	}

	/**
	 * Only needs to be done after the SSDP service has actually stopped.
	 * (device could have gone offline during the stopped period)
	 */
	public void syncLocalDevicesWithSsdpStackDelayed() {
		ALog.i(ALog.DISCOVERY, "START delayed job to mark local devices offline that did not reappear after ssdp restart");
		mDiscoveryTimeoutHandler.sendEmptyMessageDelayed(DISCOVERY_SYNCLOCAL_MESSAGE, DISCOVERY_SYNCLOCAL_TIMEOUT);
	}

	public void cancelSyncLocalDevicesWithSsdpStack() {
		if (mDiscoveryTimeoutHandler.hasMessages(DISCOVERY_SYNCLOCAL_MESSAGE)) {
			mDiscoveryTimeoutHandler.removeMessages(DISCOVERY_SYNCLOCAL_MESSAGE);
			ALog.i(ALog.DISCOVERY, "CANCEL delayed job to mark local devices offline");
		}
	}
	// ********** END ASYNC METHODS ************

	private DICommAppliance getPurAirDevice(DeviceModel deviceModel) {
		SSDPdevice ssdpDevice = deviceModel.getSsdpDevice();
		if (ssdpDevice == null) return null;

		String modelName = ssdpDevice.getModelName();
		if (modelName == null || !modelName.contains(AppConstants.MODEL_NAME)) {
			ALog.d(ALog.DISCOVERY, "Not a valid purifier device - model: " + modelName);
			return null;
		}
		ALog.i(ALog.DISCOVERY, "Air Purifier Device Discovered USN: " + deviceModel.getUsn());
		String eui64 = ssdpDevice.getCppId();
		String usn = deviceModel.getUsn();
		String ipAddress = deviceModel.getIpAddress();
		String name = ssdpDevice.getFriendlyName();
		String networkSsid = mNetwork.getLastKnownNetworkSsid();
		Long bootId = -1l;
		try {
			bootId = Long.parseLong(deviceModel.getBootID());
		} catch (NumberFormatException e) {
			// NOP
		}

		DISecurity diSecurity = new DISecurity();
        CommunicationMarshal communicationStrategy = new CommunicationMarshal(diSecurity);
        NetworkNode networkNode = new NetworkNode();
        networkNode.setBootId(bootId);
        networkNode.setCppId(eui64);
        networkNode.setIpAddress(ipAddress);
        networkNode.setName(name);
        networkNode.setConnectionState(ConnectionState.CONNECTED_LOCALLY);

        final DICommAppliance purifier = new AirPurifier(networkNode, communicationStrategy);

        networkNode.setEncryptionKeyUpdatedListener(new EncryptionKeyUpdatedListener() {
            @Override
            public void onKeyUpdate() {
            	updateApplianceInDatabase(purifier);
            }
        });

		purifier.getNetworkNode().setHomeSsid(networkSsid);
		if (!isValidNetworkNode(purifier.getNetworkNode())) return null;

		return purifier;
	}

	private boolean isValidNetworkNode(NetworkNode networkNode) {
		if (networkNode.getCppId() == null || networkNode.getCppId().isEmpty()) {
			ALog.d(ALog.DISCOVERY, "Not a valid networkNode - eui64 is null");
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
		return true;
	}

	private DeviceModel getDeviceModelFromMessage(Message msg) {
		if (msg == null) return null;

		try {
			DeviceModel device = (DeviceModel) ((InternalMessage) msg.obj).obj;
			return device;
		} catch (Exception e) {
			ALog.d(ALog.DISCOVERY, "Invalid device detected: " + "Error: " + e.getMessage());
		}
		return null;
	}

	private void initializeDevicesMapFromDataBase() {
		ALog.i(ALog.DISCOVERY, "initializeDevicesMap") ;
		mAllAppliancesMap = new LinkedHashMap<String, DICommAppliance>();

		List<DICommAppliance> allAirPurifiers = getAllAirPurifiers();
		for (DICommAppliance airPurifier : allAirPurifiers) {
			mAllAppliancesMap.put(airPurifier.getNetworkNode().getCppId(), airPurifier);
		}
		mAddedAppliances = allAirPurifiers;
	}

	private void notifyDiscoveryListener() {
		if (mListener == null) return;
		mListener.onDiscoveredDevicesListChanged();

		notifyAddNewPurifier();
		ALog.v(ALog.DISCOVERY, "Notified listener of change event");
	}

	private void notifyAddNewPurifier() {
		Log.i("TEMP", "notifyAddNewPurifier datasetChanged: " + mNewApplianceDiscoveredListener);
		if (mListener instanceof MainActivity && mNewApplianceDiscoveredListener != null) {
			mNewApplianceDiscoveredListener.onNewApplianceDiscovered();
		}
	}

	public void printDiscoveredDevicesInfo(String tag) {
		if (tag == null || tag.isEmpty()) {
			tag = ALog.DISCOVERY;
		}

		if (mAllAppliancesMap.size() <= 0) {
			ALog.d(tag, "No devices discovered - map is 0");
			return;
		}

		String offline = "Offline devices %d: ";
		String local = "Local devices %d: ";
		String cpp = "Cpp devices %d: ";
		for (DICommAppliance device : mAllAppliancesMap.values()) {
			switch (device.getNetworkNode().getConnectionState()) {
			case DISCONNECTED: offline += device.getNetworkNode().getName() + ", "; break;
			case CONNECTED_LOCALLY: local += device.getNetworkNode().getName() + ", "; break;
			case CONNECTED_REMOTELY: cpp += device.getNetworkNode().getName() + ", "; break;
			}
		}
		ALog.d(tag, String.format(offline, offline.length() - offline.replace(",", "").length()));
		ALog.d(tag, String.format(local, local.length() - local.replace(",", "").length()));
		ALog.d(tag, String.format(cpp, cpp.length() - cpp.replace(",", "").length()));
	}

	public List<DICommAppliance> getAllAirPurifiers() {
		List<DICommAppliance> result = new ArrayList<DICommAppliance>();

		List<NetworkNode> networkNodes = mNetworkNodeDatabase.getAll();

		for (NetworkNode networkNode : networkNodes) {
			DISecurity diSecurity = new DISecurity();
			CommunicationMarshal communicationStrategy = new CommunicationMarshal(diSecurity);
			final DICommAppliance airPurifier = new AirPurifier(networkNode, communicationStrategy);

			mApplianceDatabase.loadDataForAppliance(airPurifier);
			networkNode.setEncryptionKeyUpdatedListener(new EncryptionKeyUpdatedListener() {
				@Override
				public void onKeyUpdate() {
					updateApplianceInDatabase(airPurifier);
				}
			});
			result.add(airPurifier);
		}
		return result;
	}

	// TODO DIComm refactor: improve interface
	public long insertApplianceToDatabase(DICommAppliance airPurifier) {
		long rowId = mNetworkNodeDatabase.save(airPurifier.getNetworkNode());
		mApplianceDatabase.save(airPurifier);

		return rowId;
	}

	// TODO DIComm refactor: improve interface
	public long updateApplianceInDatabase(DICommAppliance airPurifier) {
		long rowId = mNetworkNodeDatabase.save(airPurifier.getNetworkNode());
		mApplianceDatabase.save(airPurifier);

		return rowId;
	}

	// TODO DIComm refactor: improve interface
	public int deleteApplianceFromDatabase(DICommAppliance airPurifier) {
		int rowsDeleted = mNetworkNodeDatabase.delete(airPurifier.getNetworkNode());
		mApplianceDatabase.delete(airPurifier);

		return rowsDeleted;
	}

	// ********** START TEST METHODS ************
	public static void setDummyDiscoveryManagerForTesting(DiscoveryManager dummyManager) {
		mInstance = dummyManager;
	}

	public void setDummyDiscoveryEventListenerForTesting(DiscoveryEventListener dummyListener) {
		mListener = dummyListener;
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

	public void setPurifierListForTesting(LinkedHashMap<String, DICommAppliance> testMap) {
		mAllAppliancesMap = testMap;
	}

	public Handler getDiscoveryTimeoutHandlerForTesting() {
		return mDiscoveryTimeoutHandler;
	}
	// ********** END TEST METHODS ************
}
