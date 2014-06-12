package com.philips.cl.di.dev.pa.newpurifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;

import com.philips.cl.di.common.ssdp.contants.DiscoveryMessageID;
import com.philips.cl.di.common.ssdp.controller.InternalMessage;
import com.philips.cl.di.common.ssdp.lib.SsdpService;
import com.philips.cl.di.common.ssdp.models.DeviceModel;
import com.philips.cl.di.common.ssdp.models.SSDPdevice;
import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.constant.AppConstants.Port;
import com.philips.cl.di.dev.pa.cpp.CPPController;
import com.philips.cl.di.dev.pa.cpp.CppDiscoverEventListener;
import com.philips.cl.di.dev.pa.cpp.SignonListener;
import com.philips.cl.di.dev.pa.datamodel.DiscoverInfo;
import com.philips.cl.di.dev.pa.datamodel.SessionDto;
import com.philips.cl.di.dev.pa.newpurifier.NetworkMonitor.NetworkChangedCallback;
import com.philips.cl.di.dev.pa.newpurifier.NetworkMonitor.NetworkState;
import com.philips.cl.di.dev.pa.purifier.PurifierDatabase;
import com.philips.cl.di.dev.pa.purifier.SubscriptionHandler;
import com.philips.cl.di.dev.pa.security.DISecurity;
import com.philips.cl.di.dev.pa.security.KeyDecryptListener;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.DataParser;
import com.philips.cl.di.dev.pa.util.Utils;

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
public class DiscoveryManager implements Callback, KeyDecryptListener, NetworkChangedCallback, CppDiscoverEventListener {

	private static DiscoveryManager mInstance;
	private LinkedHashMap<String, PurAirDevice> mDevicesMap;
	private static final Object mDiscoveryLock = new Object();

	private PurifierDatabase mDatabase;
	private NetworkMonitor mNetwork;
	private DISecurity mSecurity;
	private SsdpServiceHelper mSsdpHelper;
	private CppDiscoveryHelper mCppHelper;
	
	private DiscoveryEventListener mListener;
	
	private static final int DISCOVERYTIMEOUT_MESSAGE = 987654321;
	private static final int DISCOVERYTIMEOUT = 10000;
	private static Handler mDiscoveryTimeoutHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == DISCOVERYTIMEOUT_MESSAGE) {
				synchronized (mDiscoveryLock) {
					DiscoveryManager.getInstance().markNonDiscoveredDevicesRemote();
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
		mSecurity = new DISecurity(this);
		mSsdpHelper = new SsdpServiceHelper(SsdpService.getInstance(), this);
		mCppHelper = new CppDiscoveryHelper(CPPController.getInstance(PurAirApplication.getAppContext()), SubscriptionHandler.getInstance(), this);

		// Starting network monitor will ensure a fist callback.
		mNetwork = new NetworkMonitor(PurAirApplication.getAppContext(), this);
	}
	
	public void start(DiscoveryEventListener listener) { // TODO test
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
		mDiscoveryTimeoutHandler.removeMessages(DISCOVERYTIMEOUT_MESSAGE);
		mCppHelper.stopDiscoveryViaCpp();
		mNetwork.stopNetworkChangedReceiver(PurAirApplication.getAppContext());
		mListener = null;
	}
	
	public ArrayList<PurAirDevice> getDiscoveredDevices() {
		return new ArrayList<PurAirDevice>(mDevicesMap.values());
	}
	
	public PurAirDevice getDeviceByEui64(String eui64) {
		if (eui64 == null) return null;
		return mDevicesMap.get(eui64);
	}
	
	@Override
	public void onNetworkChanged(NetworkState networkState, String networkSsid) {
		// Assumption: Wifi switch will go through the none state
		mDiscoveryTimeoutHandler.removeMessages(DISCOVERYTIMEOUT_MESSAGE);
		synchronized (mDiscoveryLock) {
			switch(networkState) {
			case NONE : 
				markAllDevicesOffline();
				mSsdpHelper.stopDiscoveryAsync();
				ALog.d(ALog.DISCOVERY, "Stopping SSDP service - Network change (no network)");
				break;
			case MOBILE:
				markAllDevicesRemote();
				mSsdpHelper.stopDiscoveryAsync();
				ALog.d(ALog.DISCOVERY, "Stopping SSDP service - Network change (mobile data)");
				break;
			case WIFI_WITH_INTERNET:
				markOtherNetworkDevicesRemote(networkSsid);
				mDiscoveryTimeoutHandler.sendEmptyMessageDelayed(DISCOVERYTIMEOUT_MESSAGE, DISCOVERYTIMEOUT);
				mSsdpHelper.startDiscoveryAsync();
				ALog.d(ALog.DISCOVERY, "Starting SSDP service - Network change (wifi internet)");
				break;
			default:
				break;
			}
		}
		
	}
	
	@Override
	public void onDiscoverEventReceived(String data, boolean isResponseToRequest) {
		DiscoverInfo info = DataParser.parseDiscoverInfo(data);
		if (info == null) return;
		ALog.v(ALog.DISCOVERY, "Received discover event from CPP: " + (isResponseToRequest ? "requested" : "change"));
		
		boolean notifyListeners = false;
		synchronized (mDiscoveryLock) {
			if (isResponseToRequest) {
				notifyListeners = updateConnectedStateViaCppAllPurifiers(info);
			} else {
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
		PurAirDevice purifier = getPurAirDevice(deviceModel);
		if (purifier == null) return false;
		
		if (mDevicesMap.containsKey(purifier.getEui64())) {
			updateExistingDevice(purifier);
		} else {
			addNewDevice(purifier);
		}
			
		return true;
	}
	
	private boolean onDeviceLost(DeviceModel deviceModel) {
		String lostDeviceUsn = deviceModel.getId();
		if (lostDeviceUsn == null || lostDeviceUsn.length() <= 0) return false;

		ArrayList<PurAirDevice> devices = getDiscoveredDevices();
		for (PurAirDevice purifier : devices) {
			if (purifier.getUsn().equals(lostDeviceUsn)) {
				ALog.d(ALog.DISCOVERY, "Lost purifier - marking as DISCONNECTED: " + purifier);
				purifier.setConnectionState(ConnectionState.DISCONNECTED);
				notifyDiscoveryListener();
				return true;
			}
		}
		return false;
	}
	
	private void updateExistingDevice(PurAirDevice newPurifier) {
		PurAirDevice existingPurifier = mDevicesMap.get(newPurifier.getEui64());
		boolean notifyListeners = false;
		
		if (!newPurifier.getLastKnownNetworkSsid().equals(existingPurifier.getLastKnownNetworkSsid())) {
			existingPurifier.setLastKnownNetworkSsid(newPurifier.getLastKnownNetworkSsid());
			mDatabase.insertPurAirDevice(existingPurifier);
		}
		
		if (!newPurifier.getIpAddress().equals(existingPurifier.getIpAddress())) {
			existingPurifier.setIpAddress(newPurifier.getIpAddress());
		}
		
		if (existingPurifier.getConnectionState() != newPurifier.getConnectionState()) {
			existingPurifier.setConnectionState(newPurifier.getConnectionState());
			notifyListeners = true;
		}

		if (!existingPurifier.getName().equals(newPurifier.getName())) {
			existingPurifier.setName(newPurifier.getName());
			mDatabase.insertPurAirDevice(existingPurifier);
			notifyListeners = true;
		}
		
		if (existingPurifier.getBootId() != newPurifier.getBootId()) {
			existingPurifier.setEncryptionKey(null);
			existingPurifier.setBootId(newPurifier.getBootId());
			startKeyExchange(existingPurifier);
			notifyListeners = false; // only sent events when we have new key
		}
		
		if (notifyListeners) {
			notifyDiscoveryListener();
		}
		ALog.d(ALog.DISCOVERY, "Successfully updated purifier: " + existingPurifier);
	}

	/**
	 * Completely new device - never seen before
	 */
	private void addNewDevice(PurAirDevice purifier) {
		mDevicesMap.put(purifier.getEui64(), purifier);
		startKeyExchange(purifier);
		
		// Listener notified when key is exchanged
		ALog.d(ALog.DISCOVERY, "Successfully added purifier: " + purifier);
	}
// ********** END SSDP METHODS ************
	
	
// ********** START NETWORK METHODS ************
	private void markOtherNetworkDevicesRemote(String ssid) {
		ALog.d(ALog.DISCOVERY, "Marking all paired devices REMOTE that will not appear on network: " + ssid);
		boolean statusUpdated = false;
		for (PurAirDevice device : mDevicesMap.values()) {
			if (device.getConnectionState() == ConnectionState.CONNECTED_LOCALLY) continue; // already discovered
			if (device.getConnectionState() == ConnectionState.CONNECTED_REMOTELY) continue; // already remote
			if (device.getLastKnownNetworkSsid().equals(ssid)) continue; // will appear local on this network
			if (!device.isPaired()) continue; // not paired
			
			device.setConnectionState(ConnectionState.CONNECTED_REMOTELY);
			statusUpdated = true;
			ALog.v(ALog.DISCOVERY, "Marked other network REMOTE: " + device.getName());
		}
		if (!statusUpdated) return;
		notifyDiscoveryListener();
	}
	
	private void markNonDiscoveredDevicesRemote() {
		ALog.d(ALog.DISCOVERY, "Marking paired devices that where not discovered locally REMOTE");
		boolean statusUpdated = false;
		for (PurAirDevice device : mDevicesMap.values()) {
			if (device.getConnectionState() == ConnectionState.CONNECTED_LOCALLY) continue; // already discovered
			if (device.getConnectionState() == ConnectionState.CONNECTED_REMOTELY) continue; // already remote
			if (!device.isPaired() || !device.isOnlineViaCpp()) continue; // not online via cpp
			
			device.setConnectionState(ConnectionState.CONNECTED_REMOTELY);
			statusUpdated = true;
			ALog.v(ALog.DISCOVERY, "Marked non discovered REMOTE: " + device.getName());
		}
		if (!statusUpdated) return;
		notifyDiscoveryListener();
	}

	private void markAllDevicesRemote() {
		ALog.d(ALog.DISCOVERY, "Marking all paired devices REMOTE");
		boolean statusUpdated = false;
		for (PurAirDevice device : mDevicesMap.values()) {
			if (device.isPaired() && device.isOnlineViaCpp()) {
				device.setConnectionState(ConnectionState.CONNECTED_REMOTELY);
				statusUpdated = true;
				ALog.v(ALog.DISCOVERY, "Marked paired/cpponline REMOTE: " + device.getName());
			} else {
				device.setConnectionState(ConnectionState.DISCONNECTED);
				statusUpdated = true;
				ALog.v(ALog.DISCOVERY, "Marked non paired/cppoffline DISCONNECTED: " + device.getName());
			}
		}
		if (!statusUpdated) return;
		notifyDiscoveryListener();
	}

	private void markAllDevicesOffline() {
		ALog.d(ALog.DISCOVERY, "Marking all devices OFFLINE");
		boolean statusUpdated = false;
		for (PurAirDevice device : mDevicesMap.values()) {
			if (device.getConnectionState().equals(ConnectionState.DISCONNECTED)) continue;
			
			device.setConnectionState(ConnectionState.DISCONNECTED);
			statusUpdated = true;
			ALog.v(ALog.DISCOVERY, "Marked OFFLINE: " + device.getName());
		}
		if (!statusUpdated) return;
		notifyDiscoveryListener();
	}
// ********** END NETWORK METHODS ************
	

// ********** START CPP METHODS ************
	public boolean updateConnectedStateViaCppAllPurifiers(DiscoverInfo info) {
		ALog.v(ALog.DISCOVERY, "Received connected devices list via cpp");
		boolean connected = info.isConnected();
		boolean notifyListeners = false;
		
		List<String> eui64s = Arrays.asList(info.getClientIds());
		for (PurAirDevice current : getDiscoveredDevices()) {
			boolean currentOnlineViaCpp = connected;
			if (!eui64s.contains(current.getEui64())) {
				currentOnlineViaCpp = !connected;
			}
			
			if (current.isOnlineViaCpp() != currentOnlineViaCpp) {
				current.setOnlineViaCpp(currentOnlineViaCpp);
				if (currentOnlineViaCpp) {
					notifyListeners = updateConnectedStateOnlineViaCpp(current) ? true : notifyListeners;
				} else {
					notifyListeners = updateConnectedStateOfflineViaCpp(current) ? true : notifyListeners;
				}
			}
		}
		return notifyListeners;
	}
	
	public boolean updateConnectedStateViaCppReturnedPurifiers(DiscoverInfo info) {
		ALog.v(ALog.DISCOVERY, "Received connection update via CPP");
		boolean connected = info.isConnected();
		boolean notifyListeners = false;
		
		for (String eui64 : info.getClientIds()) {
			PurAirDevice current = getDeviceByEui64(eui64);
			if (current == null) {
				ALog.v(ALog.DISCOVERY, "Received discover event for unknown purifier: " + eui64);
				continue;
			}

			if (current.isOnlineViaCpp() != connected) {
				current.setOnlineViaCpp(connected);
				if (connected) {
					notifyListeners = updateConnectedStateOnlineViaCpp(current) ? true : notifyListeners;
				} else {
					notifyListeners = updateConnectedStateOfflineViaCpp(current) ? true : notifyListeners;
				}
			}
		}
		return notifyListeners;
	}
	
	private boolean updateConnectedStateOnlineViaCpp(PurAirDevice purifier) {
		if (!purifier.isPaired()) return false;
		if (purifier.getConnectionState() != ConnectionState.DISCONNECTED) return false;
		if (mNetwork.getLastKnownNetworkState() == NetworkState.NONE) return false;

		purifier.setConnectionState(ConnectionState.CONNECTED_REMOTELY);
		ALog.v(ALog.DISCOVERY, "Marked Cpp online REMOTE: " + purifier.getName());
		return true;
	}
	
	private boolean updateConnectedStateOfflineViaCpp(PurAirDevice purifier) {
		if (purifier.getConnectionState() != ConnectionState.CONNECTED_REMOTELY) return false;

		purifier.setConnectionState(ConnectionState.DISCONNECTED);
		ALog.v(ALog.DISCOVERY, "Marked Cpp offline DISCONNECTED: " + purifier.getName());
		return true;
	}
// ********** END CPP METHODS ************
	
	private PurAirDevice getPurAirDevice(DeviceModel deviceModel) {
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
		
		PurAirDevice purifier = new PurAirDevice(eui64, usn, ipAddress, name, bootId, ConnectionState.CONNECTED_LOCALLY);
		purifier.setLastKnownNetworkSsid(networkSsid);
		if (!isValidPurifier(purifier)) return null;
		
		return purifier;
	}
	
	private boolean isValidPurifier(PurAirDevice purifier) {
		if (purifier.getEui64() == null || purifier.getEui64().isEmpty()) {
			ALog.d(ALog.DISCOVERY, "Not a valid purifier device - eui64 is null");
			return false;
		}
		if (purifier.getUsn() == null || purifier.getUsn().isEmpty()) {
			ALog.d(ALog.DISCOVERY, "Not a valid purifier device - usn is null");
			return false;
		}
		if (purifier.getIpAddress() == null || purifier.getIpAddress().isEmpty()) {
			ALog.d(ALog.DISCOVERY, "Not a valid purifier device - ipAddress is null");
			return false;
		}
		if (purifier.getName() == null || purifier.getName().isEmpty()) {
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
			ALog.d(ALog.DISCOVERY, "Invalid device detected: " + e.getMessage());
		}
		return null;
	}
	
	private void initializeDevicesMapFromDataBase() {
		mDevicesMap = new LinkedHashMap<String, PurAirDevice>();
		// Disconnected by default to allow SSDP to discover first and only after try cpp
		List<PurAirDevice> storedDevices = mDatabase.getAllPurifiers(ConnectionState.DISCONNECTED);
		if (storedDevices == null) return;
		
		ALog.d(ALog.DISCOVERY, "Successfully loaded stored devices: " + storedDevices.size());
		for (PurAirDevice device : storedDevices) {
			mDevicesMap.put(device.getEui64(), device);
		}
	}
	
	private void notifyDiscoveryListener() {
		if (mListener == null) return;
		mListener.onDiscoveredDevicesListChanged();

		ALog.v(ALog.DISCOVERY, "Notified listener of change event");
	}

	private void startKeyExchange(PurAirDevice purifier) {
		ALog.d(ALog.DISCOVERY, "Starting key exchange: " + purifier);

		mSecurity.initializeExchangeKeyCounter(purifier.getEui64());
		mSecurity.exchangeKey(Utils.getPortUrl(Port.SECURITY,	purifier.getIpAddress()), purifier.getEui64());
	}
	
	@Override
	public void keyDecrypt(String key, String deviceEui64) {
		PurAirDevice device = mDevicesMap.get(deviceEui64);
		if (device == null || key == null) return;
		
		ALog.v(ALog.DISCOVERY, "Updated key for purifier: " + device);
		device.setEncryptionKey(key);
		mDatabase.insertPurAirDevice(device);
		notifyDiscoveryListener();
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
		for (PurAirDevice device : mDevicesMap.values()) {
			switch (device.getConnectionState()) {
			case DISCONNECTED: offline += device.getName() + ", "; break;
			case CONNECTED_LOCALLY: local += device.getName() + ", "; break;
			case CONNECTED_REMOTELY: cpp += device.getName() + ", "; break;
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
	
	public void setPurifierListForTesting(LinkedHashMap<String, PurAirDevice> testMap) {
		mDevicesMap = testMap;
	}
// ********** END TEST METHODS ************

}
