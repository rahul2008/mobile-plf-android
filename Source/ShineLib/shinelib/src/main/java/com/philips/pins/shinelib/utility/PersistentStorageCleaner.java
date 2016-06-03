package com.philips.pins.shinelib.utility;

import android.support.annotation.NonNull;

import com.philips.pins.shinelib.SHNDevice;

import java.util.HashSet;
import java.util.Set;

/**
 * Convenience class that provides functionality to clean BlueLib persistent storage.
 */
public class PersistentStorageCleaner {

    @NonNull
    private final PersistentStorageFactory factory;

    PersistentStorageCleaner(final @NonNull PersistentStorageFactory factory) {
        this.factory = factory;
    }

    /**
     * Clear all user specific data.
     */
    public void clearUserData() {
        factory.getPersistentStorageForUser().clear();
    }

    /**
     * Clear device specific data.
     *
     * @param device to clear the data for
     */
    public void clearDeviceData(@NonNull final SHNDevice device) {
        clearDeviceData(device.getAddress());
    }

    /**
     * Clear device specific data.
     *
     * @param deviceAddress of the device to clear the data for
     */
    public void clearDeviceData(@NonNull final String deviceAddress) {
        PersistentStorage persistentStorageForDevice = factory.getPersistentStorageForDevice(deviceAddress);
        persistentStorageForDevice.clear();

        PersistentStorage deviceAddressesStorage = factory.getPersistentStorageForDeviceAddresses();
        Set<String> deviceAddresses = deviceAddressesStorage.getStringSet(PersistentStorageFactory.DEVICE_ADDRESS_KEY, new HashSet<String>());
        deviceAddresses.remove(deviceAddress);
        deviceAddressesStorage.put(PersistentStorageFactory.DEVICE_ADDRESS_KEY, deviceAddresses);
    }

    /**
     * Clear data for all associated devices.
     */
    public void clearAllDevices() {
        PersistentStorage deviceAddressesStorage = factory.getPersistentStorageForDeviceAddresses();
        Set<String> deviceAddresses = deviceAddressesStorage.getStringSet(PersistentStorageFactory.DEVICE_ADDRESS_KEY, new HashSet<String>());

        deviceAddressesStorage.clear();
        for (final String deviceAddress : deviceAddresses) {
            PersistentStorage persistentStorage = factory.getPersistentStorageForDevice(deviceAddress);
            persistentStorage.clear();
        }
    }

    /**
     * Clear all BlueLib data including all user and devices' data.
     */
    public void clearAllData() {
        clearAllDevices();
        clearUserData();
        factory.getPersistentStorage().clear();
    }
}
