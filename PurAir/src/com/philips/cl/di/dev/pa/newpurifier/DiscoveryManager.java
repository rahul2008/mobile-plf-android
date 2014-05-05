package com.philips.cl.di.dev.pa.newpurifier;

import java.util.LinkedHashMap;

import android.os.Handler.Callback;
import android.os.Message;

import com.philips.cl.di.common.ssdp.contants.DiscoveryMessageID;
import com.philips.cl.di.common.ssdp.controller.InternalMessage;
import com.philips.cl.di.common.ssdp.lib.SsdpService;
import com.philips.cl.di.common.ssdp.models.DeviceModel;
import com.philips.cl.di.common.ssdp.models.SSDPdevice;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.constant.AppConstants.Port;
import com.philips.cl.di.dev.pa.datamodel.PurifierDetailDto;
import com.philips.cl.di.dev.pa.security.DISecurity;
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
public class DiscoveryManager implements Callback {

	private static DiscoveryManager mInstance;
	private LinkedHashMap<String, PurAirDevice> mDevicesMap;

	public static DiscoveryManager getInstance() {
		if (mInstance == null) {
			mInstance = new DiscoveryManager();
		}
		return mInstance;
	}

	private DiscoveryManager() {
		// Enforce Singleton
		mDevicesMap = new LinkedHashMap<String, PurAirDevice>();
		// TODO add paired devices to list
	}

	public void start(Callback callback) {
		SsdpService.getInstance().startDeviceDiscovery(callback);
	}
	
	public void stop() {
		SsdpService.getInstance().stopDeviceDiscovery();
	}
	
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
			
//		if (device.getSsdpDevice() == null 
//				|| device.getSsdpDevice().getCppId() == null
//				|| device.getSsdpDevice().getModelName() == null
//				|| device.getSsdpDevice().getFriendlyName() == null) {
//				return false;
//			}
//			ALog.i(ALog.MAINACTIVITY,
//					"Device discovered usn: " + device.getUsn() + ", Ip: "
//							+ device.getIpAddress()
//							+ ", isDeviceDiscovered: " + isDeviceDiscovered
//							+ ", isEWSStarted: " + isEWSStarted);
//			long newDiscoveredBootId = 0L;
//			try {
//				newDiscoveredBootId = Long.parseLong(device.getBootID());
//			} catch (NumberFormatException e) {
//				// NOP
//				e.printStackTrace();
//			}
//			
//			if (newDiscoveredBootId != ssdpDiscoveredBootId) {
//				isDeviceDiscovered = false;
//			}
//			
//			if (device.getSsdpDevice().getModelName().contains(AppConstants.MODEL_NAME)
//					&& !isDeviceDiscovered && !isEWSStarted) {
//				onFirstDeviceDiscovered(device);
//			}
		return true;
	}
	
	private void updateExistingDevice(PurAirDevice newPurifier) {
		PurAirDevice existingPurifier = mDevicesMap.get(newPurifier.getEui64());
		
		if (existingPurifier.getConnectionState() != newPurifier.getConnectionState()) {
			existingPurifier.setConnectionState(newPurifier.getConnectionState());
			// TODO notify UI
		}

		if (!existingPurifier.getName().equals(newPurifier.getName())) {
			existingPurifier.setName(newPurifier.getName());
			// TODO notify UI
		}
		
		if (existingPurifier.getBootId() != newPurifier.getBootId()) {
			existingPurifier.setEncryptionKey(null);
			existingPurifier.setBootId(newPurifier.getBootId());
			// TODO initialize key exchange
		}
		
		ALog.d(ALog.DISCOVERY, "Successfully updated purifier: " + existingPurifier);
	}
	
	private void addNewDevice(PurAirDevice purifier) {
		
	}
	
	private PurAirDevice getPurAirDevice(DeviceModel deviceModel) {
		SSDPdevice ssdpDevice = deviceModel.getSsdpDevice();
		if (ssdpDevice == null) return null;
		
		String modelName = ssdpDevice.getModelName();
		if (modelName == null || modelName.contains(AppConstants.MODEL_NAME)) {
			ALog.d(ALog.DISCOVERY, "Not a valid purifier device - model: " + modelName);
			return null;
		}
		
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
	
	
	
	
	
//	private boolean onFirstDeviceDiscovered(DeviceModel deviceModel) {
//
//		isDeviceDiscovered = true;
//		
//		Long bootId = -1l;
//		if (deviceModel.getBootID() != null && deviceModel.getBootID().length() > 0) {
//			bootId = Long.parseLong(deviceModel.getBootID());
//		}
//		
//		PurAirDevice purifier = new PurAirDevice(deviceModel.getSsdpDevice().getCppId(), deviceModel.getUsn(), deviceModel.getIpAddress(), deviceModel.getSsdpDevice().getFriendlyName(), bootId, ConnectionState.CONNECTED_LOCALLY);
//		PurifierManager.getInstance().setCurrentPurifier(purifier);
//		
//		updatePurifierName();
//		String ssdpDiscoveredUsn = purifier.getUsn();
//		if (ssdpDiscoveredUsn == null || ssdpDiscoveredUsn.length() <= 0) {
//			return true;
//		}
//
//		//used to fetch the MAC address of purifier then save it
//		localDeviceUsn = ssdpDiscoveredUsn;
//		
//		try {
//			ssdpDiscoveredBootId = purifier.getBootId();
//		} catch (NumberFormatException e) {
//			// NOP
//			e.printStackTrace();
//		}
//
//		if (ssdpDiscoveredBootId == 0L) {
//			startKeyExchange(purifier);
//		}
//		if (dbPurifierDetailDtoList != null) {
//			boolean isDeviceInDb = false;
//			for (PurifierDetailDto infoDto : dbPurifierDetailDtoList) {
//				String dbUsn = infoDto.getUsn();
//				if (dbUsn == null || dbUsn.length() <= 0) {
//					continue;
//				}
//				if (dbUsn.equalsIgnoreCase(ssdpDiscoveredUsn)) {
//					isDeviceInDb = true;
//					long dbBootId = infoDto.getBootId();
//					if (dbBootId == ssdpDiscoveredBootId
//							&& infoDto.getDeviceKey() != null) {
//						ALog.i(ALog.MAINACTIVITY, "Device boot id is same: "
//								+ dbBootId + " ssdp bootid: "
//								+ ssdpDiscoveredBootId);
//						String cppId = infoDto.getCppId();
//						secretKey = infoDto.getDeviceKey();
//						DISecurity.setKeyIntoSecurityHashTable(cppId, secretKey);
//						DISecurity.setUrlIntoUrlsTable(
//								cppId,
//								Utils.getPortUrl(Port.SECURITY,	purifier.getIpAddress()));
//						toggleConnection(true);
//					} else {
//						startKeyExchange(purifier);
//					}
//					break;
//				}
//			}
//
//			if (!isDeviceInDb) {
//				startKeyExchange(purifier);
//			}
//		}
//
//		return true;
//	}
	
	private boolean onDeviceLost(DeviceModel device) {
		return false;
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
	
	
	
//	@Override
//	public boolean handleMessage(Message msg) {
//		DeviceModel device = null;
//		if (null != msg) {
//			final DiscoveryMessageID message = DiscoveryMessageID
//					.getID(msg.what);
//			final InternalMessage internalMessage = (InternalMessage) msg.obj;
//			if (null != internalMessage
//					&& internalMessage.obj instanceof DeviceModel) {
//				device = (DeviceModel) internalMessage.obj;
//			}
//			if (device == null) {
//				return false;
//			}
//
//			switch (message) {
//			case DEVICE_DISCOVERED:
//				if (device.getSsdpDevice() == null 
//					|| device.getSsdpDevice().getCppId() == null
//					|| device.getSsdpDevice().getModelName() == null
//					|| device.getSsdpDevice().getFriendlyName() == null) {
//					return false;
//				}
//				ALog.i(ALog.MAINACTIVITY,
//						"Device discovered usn: " + device.getUsn() + ", Ip: "
//								+ device.getIpAddress()
//								+ ", isDeviceDiscovered: " + isDeviceDiscovered
//								+ ", isEWSStarted: " + isEWSStarted);
//				long newDiscoveredBootId = 0L;
//				try {
//					newDiscoveredBootId = Long.parseLong(device.getBootID());
//				} catch (NumberFormatException e) {
//					// NOP
//					e.printStackTrace();
//				}
//				
//				if (newDiscoveredBootId != ssdpDiscoveredBootId) {
//					isDeviceDiscovered = false;
//				}
//				
//				if (device.getSsdpDevice().getModelName().contains(AppConstants.MODEL_NAME)
//						&& !isDeviceDiscovered && !isEWSStarted) {
//					onFirstDeviceDiscovered(device);
//				}
//				break;
//			case DEVICE_LOST:
//				onDeviceLost(device);
//				break;
//			default:
//				break;
//			}
//			return false;
//		}
//		return false;
//	}
}
