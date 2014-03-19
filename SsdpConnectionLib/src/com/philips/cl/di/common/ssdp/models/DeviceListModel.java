package com.philips.cl.di.common.ssdp.models;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 310151556
 * @version $Revision: 1.0 $
 */
public class DeviceListModel {
	/**
	 * Field mAliveDevices.
	 */
	private final Set<DeviceModel> mAliveDevices;
	/**
	 * Field mAliveDevicesMap.
	 */
	private final Map<String, DeviceModel> mAliveDevicesMap;

	/**
	 * Constructor for DeviceListModel.
	 */
	public DeviceListModel() {
		mAliveDevicesMap = new ConcurrentHashMap<String, DeviceModel>();
		mAliveDevices = new HashSet<DeviceModel>();
	}

	/**
	 * Method addNewDevice.
	 * 
	 * @param usn
	 *            String
	 * @param pDeviceModel
	 *            DeviceModel
	 */
	public void addNewDevice(final String usn, final DeviceModel pDeviceModel) {
		if ((mAliveDevicesMap != null) && (pDeviceModel != null)) {
			if (pDeviceModel.getUsn() != null) {
				mAliveDevicesMap.put(pDeviceModel.getUsn(), pDeviceModel);
			}
			mAliveDevices.add(pDeviceModel);
		}
	}

	/**
	 * @return the mAliveDevices
	 */
	public Set<DeviceModel> getAliveDevices() {
		return mAliveDevices;
	}

	/**
	 * @return the mAliveDevicesMap
	 */
	public Map<String, DeviceModel> getAliveDevicesMap() {
		return mAliveDevicesMap;
	}

	/**
	 * Method removeDevice.
	 * 
	 * @param pDevice
	 *            DeviceModel
	 */
	public void removeDevice(final DeviceModel pDevice) {
		if ((mAliveDevicesMap != null) && (pDevice != null)) {
			if (pDevice.getUsn() != null) {
				mAliveDevicesMap.remove(pDevice.getUsn());
			}
			mAliveDevices.remove(pDevice);
		}
	}

	/**
	 * Method removeDevice.
	 * 
	 * @param pUsn
	 *            String
	 */
	public void removeDevice(final String pUsn) {
		if (mAliveDevicesMap != null) {
			mAliveDevicesMap.remove(pUsn);
		}
	}

	/**
	 * Method toString.
	 * 
	 * @return String
	 */
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("[Alive Devices :").append(mAliveDevices).append("][ mAliveDevicesMap");
		builder.append(mAliveDevicesMap).append(']');
		return builder.toString();
	}

}
