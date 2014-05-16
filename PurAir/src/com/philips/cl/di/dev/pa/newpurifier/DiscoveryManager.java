package com.philips.cl.di.dev.pa.newpurifier;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

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
import com.philips.cl.di.dev.pa.newpurifier.NetworkMonitor.NetworkChangedCallback;
import com.philips.cl.di.dev.pa.newpurifier.NetworkMonitor.NetworkState;
import com.philips.cl.di.dev.pa.purifier.PurifierDatabase;
import com.philips.cl.di.dev.pa.security.DISecurity;
import com.philips.cl.di.dev.pa.security.KeyDecryptListener;
import com.philips.cl.di.dev.pa.util.ALog;
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
public class DiscoveryManager implements Callback, KeyDecryptListener, NetworkChangedCallback {

	private static DiscoveryManager mInstance;
	private LinkedHashMap<String, PurAirDevice> mDevicesMap;

	private PurifierDatabase mDatabase;
	private NetworkMonitor mNetwork;
	private DISecurity mSecurity;
	
	private DiscoveryEventListener mListener;

	public static synchronized DiscoveryManager getInstance() {
		if (mInstance == null) {
			mInstance = new DiscoveryManager();
		}
		return mInstance;
	}

	private DiscoveryManager() {
		// Enforce Singleton
		mDatabase = new PurifierDatabase();
		mNetwork = new NetworkMonitor(PurAirApplication.getAppContext(), this);
		mSecurity = new DISecurity(this);
		initializeDevicesMapFromDataBase();
		mNetwork.getLastKnownNetworkState(); // Callback will enforce state update
	}
	
	public void start(DiscoveryEventListener listener) { // TODO test
		if (mNetwork.getLastKnownNetworkState() == NetworkState.WIFI_WITH_INTERNET) {
			SsdpService.getInstance().startDeviceDiscovery(this);
			ALog.d(ALog.DISCOVERY, "Starting SSDP service - Start called (wifi_internet)");
		}
		mNetwork.startNetworkChangedReceiver(PurAirApplication.getAppContext());
		mListener = listener;
	}
	
	public void stop() {
		SsdpService.getInstance().stopDeviceDiscovery();
		ALog.d(ALog.DISCOVERY, "Stopping SSDP service - Stop called");
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
	
	@Override
	public void onNetworkChanged(NetworkState networkState) {
		// Assumption: Wifi switch will go through the none state
		switch(networkState) {
		case NONE : 
			markAllDevicesOffline();
			SsdpService.getInstance().stopDeviceDiscovery();
			ALog.d(ALog.DISCOVERY, "Stopping SSDP service - Network change (no network)");
			break;
		case MOBILE:
			markOnlyPairedDevicesOnline();
			SsdpService.getInstance().stopDeviceDiscovery();
			ALog.d(ALog.DISCOVERY, "Stopping SSDP service - Network change (mobile data)");
			break;
		case WIFI_WITH_INTERNET:
			markPairedNonLocalDevicesOnline();
			SsdpService.getInstance().startDeviceDiscovery(this);
			ALog.d(ALog.DISCOVERY, "Starting SSDP service - Network change (wifi internet)");
			break;
		default:
			break;
		}
		
	}
	
	private void markPairedNonLocalDevicesOnline() {
		ALog.d(ALog.DISCOVERY, "Marking paired devices offline");
		for (PurAirDevice device : mDevicesMap.values()) {
			if (device.isPaired() && device.getConnectionState() != ConnectionState.CONNECTED_LOCALLY) {
				device.setConnectionState(ConnectionState.CONNECTED_REMOTELY);
			} else {
				// NOP - Local devices will be managed by ssdp
			}
		}
		notifyDiscoveryListener();
	}

	private void markOnlyPairedDevicesOnline() {
		ALog.d(ALog.DISCOVERY, "Marking paired devices online");
		for (PurAirDevice device : mDevicesMap.values()) {
			if (device.isPaired()) {
				device.setConnectionState(ConnectionState.CONNECTED_REMOTELY);
			} else {
				device.setConnectionState(ConnectionState.DISCONNECTED);
			}
		}
		notifyDiscoveryListener();
	}

	private void markAllDevicesOffline() {
		ALog.d(ALog.DISCOVERY, "Marking all devices offline");
		for (PurAirDevice device : mDevicesMap.values()) {
			device.setConnectionState(ConnectionState.DISCONNECTED);
		}
		notifyDiscoveryListener();
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

		switch (messageID) {
			case DEVICE_DISCOVERED: return onDeviceDiscovered(device);
			case DEVICE_LOST: return onDeviceLost(device);
			default: break;
		}
		
		return false;
	}
			
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
				purifier.setConnectionState(ConnectionState.DISCONNECTED);
				ALog.d(ALog.DISCOVERY, "Lost purifier: " + purifier);
				notifyDiscoveryListener();
				return true;
			}
		}
		
		return false;
	}
	
	private void updateExistingDevice(PurAirDevice newPurifier) {
		PurAirDevice existingPurifier = mDevicesMap.get(newPurifier.getEui64());
		boolean notifyListeners = false;
		
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
		Long bootId = -1l;
		try {
			bootId = Long.parseLong(deviceModel.getBootID());
		} catch (NumberFormatException e) {
			// NOP
		}
		
		PurAirDevice purifier = new PurAirDevice(eui64, usn, ipAddress, name, bootId, ConnectionState.CONNECTED_LOCALLY);
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
		// TODO requirements for BootID???
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
		List<PurAirDevice> storedDevices = mDatabase.getAllPurifiers(ConnectionState.CONNECTED_REMOTELY);
		// TODO set default connectionState to CPP?
		if (storedDevices == null) return;
		
		ALog.d(ALog.DISCOVERY, "Successfully loaded stored devices: " + storedDevices.size());
		for (PurAirDevice device : storedDevices) {
			mDevicesMap.put(device.getEui64(), device);
		}
	}
	
	private void notifyDiscoveryListener() {
		if (mListener == null) return;
		mListener.onDiscoveredDevicesListChanged();
		ALog.v(ALog.DISCOVERY, "Notified listeners of change event");
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
		
		device.setEncryptionKey(key);
		mDatabase.insertPurAirDevice(device);
		notifyDiscoveryListener();
	}
	
	public static void setDummyDiscoveryManagerForTesting(DiscoveryManager dummyManager) {
		mInstance = dummyManager;
	}
	
}
