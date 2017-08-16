package com.philips.cl.di.common.ssdp.models;

import android.support.annotation.NonNull;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author 310151556
 * @version $Revision: 1.0 $
 */
@Deprecated
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
        mAliveDevicesMap = new ConcurrentHashMap<>();
        mAliveDevices = new CopyOnWriteArraySet<>();
    }

    /**
     * Method addNewDevice.
     *
     * @param usn          String
     * @param pDeviceModel DeviceModel
     */
    public void addNewDevice(final String usn, final @NonNull DeviceModel pDeviceModel) {
        if (pDeviceModel.getUsn() != null) {
            mAliveDevicesMap.put(pDeviceModel.getUsn(), pDeviceModel);
        }
        mAliveDevices.add(pDeviceModel);
    }

    /**
     * @return the mAliveDevices
     */
    public Set<DeviceModel> getAliveDevices() {
        return mAliveDevices;
    }

    public DeviceModel getDevice(String usn) {
        return mAliveDevicesMap.get(usn);
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
     * @param device DeviceModel
     */
    public void removeDevice(final DeviceModel device) {
        if (device != null) {
            if (device.getUsn() != null) {
                mAliveDevicesMap.remove(device.getUsn());
            }
            mAliveDevices.remove(device);
        }
    }

    /**
     * Method removeDevice.
     *
     * @param usn String
     */
    public void removeDevice(final String usn) {
        mAliveDevicesMap.remove(usn);
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
