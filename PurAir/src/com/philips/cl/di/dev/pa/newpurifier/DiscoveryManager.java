package com.philips.cl.di.dev.pa.newpurifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

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
import com.philips.cl.di.dev.pa.purifier.PurifierDatabase;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.DataParser;
import com.philips.cl.di.dicomm.communication.CommunicationMarshal;
import com.philips.cl.di.dicomm.security.DISecurity;

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
	private LinkedHashMap<String, AirPurifier> mDevicesMap;
	private static final Object mDiscoveryLock = new Object();

	private PurifierDatabase mDatabase;
	private NetworkMonitor mNetwork;
	private SsdpServiceHelper mSsdpHelper;
	private CppDiscoveryHelper mCppHelper;

	private DiscoveryEventListener mListener;
	private List<AirPurifier> storedDevices;
	private AddNewPurifierListener addNewPurifierListener;

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

	public static synchronized DiscoveryManager getInstance() {
		if (mInstance == null) {
			mInstance = new DiscoveryManager();
		}
		return mInstance;
	}

	private DiscoveryManager() {
		// Enforce Singleton
		mDatabase = new PurifierDatabase();
		initializeDevicesMapFromDataBase();
		mSsdpHelper = new SsdpServiceHelper(SsdpService.getInstance(), this);
		mCppHelper = new CppDiscoveryHelper(CPPController.getInstance(), this);

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

	public void setAddNewPurifierListener(AddNewPurifierListener addNewPurifierListener) {
		this.addNewPurifierListener = addNewPurifierListener;
	}

	public void removeAddNewPurifierListener() {
		this.addNewPurifierListener = null;
	}

	public ArrayList<AirPurifier> getDiscoveredDevices() {
		return new ArrayList<AirPurifier>(mDevicesMap.values());
	}

	public List<AirPurifier> getStoreDevices() {
		List<AirPurifier> purifiers = new ArrayList<AirPurifier>();
		for (AirPurifier purAirDevice : storedDevices) {
			purifiers.add(mDevicesMap.get(purAirDevice.getNetworkNode().getCppId()));
		}
		return purifiers;
	}

	public List<AirPurifier> updateStoreDevices() {
		storedDevices = mDatabase.getAllPurifiers(ConnectionState.DISCONNECTED);
		for (final AirPurifier airPurifier : storedDevices) {
		    airPurifier.getNetworkNode().setEncryptionKeyUpdatedListener(new EncryptionKeyUpdatedListener() {
                @Override
                public void onKeyUpdate() {
                    mDatabase.updatePurifierUsingUsn(airPurifier);
                }
            });
        }
		return storedDevices;
	}

	public void removeFromDiscoveredList(String eui64) {
		if (eui64 == null || eui64.isEmpty()) return;
		AirPurifier airPurifier = mDevicesMap.remove(eui64);
		if(airPurifier!=null){
		    airPurifier.getNetworkNode().setEncryptionKeyUpdatedListener(null);
		}
	}

	public void updatePairingStatus(String eui64, NetworkNode.PAIRED_STATUS state) {
		if (eui64 == null || eui64.isEmpty()) return;
		if (mDevicesMap.containsKey(eui64)) {
			mDevicesMap.get(eui64).getNetworkNode().setPairedState(state);
		}
	}

	public List<AirPurifier> getNewDevicesDiscovered() {
		boolean addToNewDeviceList = true ;
		List<AirPurifier> discoveredDevices = getDiscoveredDevices() ;
		List<AirPurifier> devicesInDataBase = getStoreDevices() ;
		List<AirPurifier> newDevices = new ArrayList<AirPurifier>() ;

		for(AirPurifier device: discoveredDevices) {
			for( AirPurifier deviceInDB: devicesInDataBase) {
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

	public AirPurifier getDeviceByEui64(String eui64) {
		if (eui64 == null || eui64.isEmpty()) return null;
		return mDevicesMap.get(eui64);
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

		AirPurifier purifier = getPurAirDevice(deviceModel);
		if (purifier == null) return false;
		ALog.i(ALog.SSDP, "Discovered device name: " + purifier.getNetworkNode().getName());
		if (mDevicesMap.containsKey(purifier.getNetworkNode().getCppId())) {
			updateExistingDevice(purifier);
		} else {
			addNewDevice(purifier);
		}

		return true;
	}

	private boolean onDeviceLost(DeviceModel deviceModel) {
		String lostDeviceUsn = deviceModel.getId();
		if (lostDeviceUsn == null || lostDeviceUsn.length() <= 0) return false;

		ArrayList<AirPurifier> devices = getDiscoveredDevices();
		for (AirPurifier purifier : devices) {
			if (purifier.getUsn().equals(lostDeviceUsn)) {
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

	private void updateExistingDevice(AirPurifier newPurifier) {
		AirPurifier existingPurifier = mDevicesMap.get(newPurifier.getNetworkNode().getCppId());
		boolean notifyListeners = true;

		if (newPurifier.getNetworkNode().getHomeSsid() != null &&
				!newPurifier.getNetworkNode().getHomeSsid().equals(existingPurifier.getNetworkNode().getHomeSsid())) {
			existingPurifier.getNetworkNode().setHomeSsid(newPurifier.getNetworkNode().getHomeSsid());
			mDatabase.updatePurifierUsingUsn(existingPurifier);
		}

		if (!newPurifier.getNetworkNode().getIpAddress().equals(existingPurifier.getNetworkNode().getIpAddress())) {
			existingPurifier.getNetworkNode().setIpAddress(newPurifier.getNetworkNode().getIpAddress());
		}

		if (!existingPurifier.getNetworkNode().getName().equals(newPurifier.getNetworkNode().getName())) {
			existingPurifier.getNetworkNode().setName(newPurifier.getNetworkNode().getName());
			mDatabase.updatePurifierUsingUsn(existingPurifier);
			notifyListeners = true;
		}

		//If current location latitude and longitude null, then update
		if (existingPurifier.getLatitude() == null && existingPurifier.getLongitude() == null) {
			Location location = OutdoorController.getInstance().getCurrentLocation();
			if (location != null) {
				existingPurifier.setLatitude(String.valueOf(location.getLatitude()));
				existingPurifier.setLongitude(String.valueOf(location.getLongitude()));
				mDatabase.updatePurifierUsingUsn(existingPurifier);
				notifyListeners = true;
			}
		}

		if (existingPurifier.getNetworkNode().getBootId() != newPurifier.getNetworkNode().getBootId() || existingPurifier.getNetworkNode().getEncryptionKey() == null) {
			existingPurifier.getNetworkNode().setEncryptionKey(null);
			existingPurifier.getNetworkNode().setBootId(newPurifier.getNetworkNode().getBootId());
			existingPurifier.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.NOT_PAIRED);
			// TODO DIComm Refactor - ConnectionState should be set to locally here (after changing pairing)
			ALog.d(ALog.PAIRING, "Discovery-Boot id changed pairing set to false");
		}
		
        if (existingPurifier.getNetworkNode().getConnectionState() != newPurifier.getNetworkNode().getConnectionState()) {
            existingPurifier.getNetworkNode().setConnectionState(newPurifier.getNetworkNode().getConnectionState());
            notifyListeners = true;
        }

		if (notifyListeners) {
			notifyDiscoveryListener();
		}
		ALog.d(ALog.DISCOVERY, "Successfully updated purifier: " + existingPurifier);
	}

	/**
	 * Completely new device - never seen before
	 */
	private void addNewDevice(AirPurifier purifier) {
		mDevicesMap.put(purifier.getNetworkNode().getCppId(), purifier);
		ALog.d(ALog.DISCOVERY, "Successfully added purifier: " + purifier);
		notifyDiscoveryListener();
	}

	public void markLostDevicesInBackgroundOfflineOrRemote() {
		ALog.d(ALog.DISCOVERY, "Syncing local list after app went to background");
		boolean statusUpdated = false;

		ArrayList<String> onlineCppIds = mSsdpHelper.getOnlineDevicesEui64();

		for (AirPurifier device : mDevicesMap.values()) {
			if (device.getNetworkNode().getConnectionState() == ConnectionState.DISCONNECTED) continue; // must be offline: not discovered
			if (device.getNetworkNode().getConnectionState() == ConnectionState.CONNECTED_REMOTELY) continue; // must be remote: not discovered
			if (onlineCppIds.contains(device.getNetworkNode().getCppId())) continue; // State is correct

			// Losing a device in the background means it is offline
			device.getNetworkNode().setConnectionState(ConnectionState.DISCONNECTED);
			ALog.v(ALog.DISCOVERY, "Marked non discovered DISCONNECTED: " + device.getNetworkNode().getName());

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
		for (AirPurifier device : mDevicesMap.values()) {
			if (device.getNetworkNode().getConnectionState() == ConnectionState.CONNECTED_LOCALLY) continue; // already discovered
			if (device.getNetworkNode().getConnectionState() == ConnectionState.CONNECTED_REMOTELY) continue; // already remote
			if (device.getNetworkNode().getHomeSsid() != null && device.getNetworkNode().getHomeSsid().equals(ssid)) continue; // will appear local on this network
			if (device.getNetworkNode().getPairedState() != NetworkNode.PAIRED_STATUS.PAIRED || !device.getNetworkNode().isOnlineViaCpp()) continue; // not paired or not online

			device.getNetworkNode().setConnectionState(ConnectionState.CONNECTED_REMOTELY);
			statusUpdated = true;
			ALog.v(ALog.DISCOVERY, "Marked other network REMOTE: " + device.getNetworkNode().getName());
		}
		if (!statusUpdated) return;
		notifyDiscoveryListener();
	}

	private void markNonDiscoveredDevicesRemote() {
		ALog.d(ALog.DISCOVERY, "Marking paired devices that where not discovered locally REMOTE");
		boolean statusUpdated = false;
		for (AirPurifier device : mDevicesMap.values()) {
			if (device.getNetworkNode().getConnectionState() == ConnectionState.CONNECTED_LOCALLY) continue; // already discovered
			if (device.getNetworkNode().getConnectionState() == ConnectionState.CONNECTED_REMOTELY) continue; // already remote
			if (device.getNetworkNode().getPairedState() != NetworkNode.PAIRED_STATUS.PAIRED || !device.getNetworkNode().isOnlineViaCpp()) continue; // not online via cpp

			device.getNetworkNode().setConnectionState(ConnectionState.CONNECTED_REMOTELY);
			statusUpdated = true;
			ALog.v(ALog.DISCOVERY, "Marked non discovered REMOTE: " + device.getNetworkNode().getName());
		}
		if (!statusUpdated) return;
		notifyDiscoveryListener();
	}

	private void markAllDevicesRemote() {
		ALog.d(ALog.DISCOVERY, "Marking all paired devices REMOTE");
		boolean statusUpdated = false;
		for (AirPurifier device : mDevicesMap.values()) {
			if (device.getNetworkNode().getPairedState()==NetworkNode.PAIRED_STATUS.PAIRED && device.getNetworkNode().isOnlineViaCpp()) {
				device.getNetworkNode().setConnectionState(ConnectionState.CONNECTED_REMOTELY);
				statusUpdated = true;
				ALog.v(ALog.DISCOVERY, "Marked paired/cpponline REMOTE: " + device.getNetworkNode().getName());
			} else {
				device.getNetworkNode().setConnectionState(ConnectionState.DISCONNECTED);
				statusUpdated = true;
				ALog.v(ALog.DISCOVERY, "Marked non paired/cppoffline DISCONNECTED: " + device.getNetworkNode().getName());
			}
		}
		if (!statusUpdated) return;
		notifyDiscoveryListener();
	}

	private void markAllDevicesOffline() {
		ALog.d(ALog.DISCOVERY, "Marking all devices OFFLINE");
		boolean statusUpdated = false;
		for (AirPurifier device : mDevicesMap.values()) {
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

		for (AirPurifier current : getDiscoveredDevices()) {
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
			AirPurifier current = getDeviceByEui64(eui64);
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

	private boolean updateConnectedStateOnlineViaCpp(AirPurifier purifier) {
		ALog.i(ALog.DISCOVERY, "updateConnectedStateOnlineViaCpp: "+purifier) ;
		if (purifier.getNetworkNode().getPairedState()==NetworkNode.PAIRED_STATUS.NOT_PAIRED) return false;
		if (purifier.getNetworkNode().getConnectionState() != ConnectionState.DISCONNECTED) return false;
		if (mNetwork.getLastKnownNetworkState() == NetworkState.NONE) return false;

		purifier.getNetworkNode().setConnectionState(ConnectionState.CONNECTED_REMOTELY);
		purifier.getNetworkNode().setOnlineViaCpp(true);
		ALog.v(ALog.DISCOVERY, "Marked Cpp online REMOTE: " + purifier.getNetworkNode().getName());
		return true;
	}

	private boolean updateConnectedStateOfflineViaCpp(AirPurifier purifier) {
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

	private AirPurifier getPurAirDevice(DeviceModel deviceModel) {
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

        final AirPurifier purifier = new AirPurifier(networkNode, communicationStrategy, usn);

        networkNode.setEncryptionKeyUpdatedListener(new EncryptionKeyUpdatedListener() {
            @Override
            public void onKeyUpdate() {
                mDatabase.updatePurifierUsingUsn(purifier);
            }
        });

		purifier.getNetworkNode().setHomeSsid(networkSsid);
		if (!isValidPurifier(purifier)) return null;

		return purifier;
	}

	private boolean isValidPurifier(AirPurifier purifier) {
		if (purifier.getNetworkNode().getCppId() == null || purifier.getNetworkNode().getCppId().isEmpty()) {
			ALog.d(ALog.DISCOVERY, "Not a valid purifier device - eui64 is null");
			return false;
		}
		if (purifier.getUsn() == null || purifier.getUsn().isEmpty()) {
			ALog.d(ALog.DISCOVERY, "Not a valid purifier device - usn is null");
			return false;
		}
		if (purifier.getNetworkNode().getIpAddress() == null || purifier.getNetworkNode().getIpAddress().isEmpty()) {
			ALog.d(ALog.DISCOVERY, "Not a valid purifier device - ipAddress is null");
			return false;
		}
		if (purifier.getNetworkNode().getName() == null || purifier.getNetworkNode().getName().isEmpty()) {
			ALog.d(ALog.DISCOVERY, "Not a valid purifier device - name is null");
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
		mDevicesMap = new LinkedHashMap<String, AirPurifier>();
		// Disconnected by default to allow SSDP to discover first and only after try cpp
		storedDevices = mDatabase.getAllPurifiers(ConnectionState.DISCONNECTED);

		for (final AirPurifier airPurifier : storedDevices) {
		    airPurifier.getNetworkNode().setEncryptionKeyUpdatedListener(new EncryptionKeyUpdatedListener() {
	            @Override
	            public void onKeyUpdate() {
	                mDatabase.updatePurifierUsingUsn(airPurifier);
	            }
	        });
            mDevicesMap.put(airPurifier.getNetworkNode().getCppId(), airPurifier);
        }
	}

	private void notifyDiscoveryListener() {
		if (mListener == null) return;
		mListener.onDiscoveredDevicesListChanged();

		notifyAddNewPurifier();
		ALog.v(ALog.DISCOVERY, "Notified listener of change event");
	}

	private void notifyAddNewPurifier() {
		Log.i("TEMP", "notifyAddNewPurifier datasetChanged: " + addNewPurifierListener);
		if (mListener instanceof MainActivity && addNewPurifierListener != null) {
			addNewPurifierListener.onNewPurifierDiscover();
		}
	}

	public void printDiscoveredDevicesInfo(String tag) {
		if (tag == null || tag.isEmpty()) {
			tag = ALog.DISCOVERY;
		}

		if (mDevicesMap.size() <= 0) {
			ALog.d(tag, "No devices discovered - map is 0");
			return;
		}

		String offline = "Offline devices %d: ";
		String local = "Local devices %d: ";
		String cpp = "Cpp devices %d: ";
		for (AirPurifier device : mDevicesMap.values()) {
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

	public void setPurifierListForTesting(LinkedHashMap<String, AirPurifier> testMap) {
		mDevicesMap = testMap;
	}

	public Handler getDiscoveryTimeoutHandlerForTesting() {
		return mDiscoveryTimeoutHandler;
	}
	// ********** END TEST METHODS ************
}
