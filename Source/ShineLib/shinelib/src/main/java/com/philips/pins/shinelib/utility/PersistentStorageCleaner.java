package com.philips.pins.shinelib.utility;

import android.support.annotation.NonNull;

import com.philips.pins.shinelib.SHNDevice;

import java.util.HashSet;
import java.util.Set;

public class PersistentStorageCleaner {

    @NonNull
    private final PersistentStorageFactory factory;

    PersistentStorageCleaner(final @NonNull PersistentStorageFactory factory) {
        this.factory = factory;
    }

    public void clearUserData() {
        factory.getPersistentStorageForUser().clear();
    }

    public void clearDeviceData(@NonNull final SHNDevice device) {
        clearDeviceData(device.getAddress());
    }

    public void clearDeviceData(@NonNull final String deviceAddress) {
        PersistentStorage persistentStorageForDevice = factory.getPersistentStorageForDevice(deviceAddress);
        persistentStorageForDevice.clear();

        PersistentStorage deviceAddressesStorage = factory.getPersistentStorageForDeviceAddresses();
        Set<String> deviceAddresses = deviceAddressesStorage.getStringSet(PersistentStorageFactory.DEVICE_ADDRESS_KEY, new HashSet<String>());
        deviceAddresses.remove(deviceAddress);
        deviceAddressesStorage.put(PersistentStorageFactory.DEVICE_ADDRESS_KEY, deviceAddresses);
    }

    public void clearAllDevices() {
        PersistentStorage deviceAddressesStorage = factory.getPersistentStorageForDeviceAddresses();
        Set<String> deviceAddresses = deviceAddressesStorage.getStringSet(PersistentStorageFactory.DEVICE_ADDRESS_KEY, new HashSet<String>());

        deviceAddressesStorage.clear();
        for (final String deviceAddress : deviceAddresses) {
            PersistentStorage persistentStorage = factory.getPersistentStorageForDevice(deviceAddress);
            persistentStorage.clear();
        }
    }

    public void clearAllData() {
        clearAllDevices();
        clearUserData();
        factory.getPersistentStorage().clear();
    }
}
