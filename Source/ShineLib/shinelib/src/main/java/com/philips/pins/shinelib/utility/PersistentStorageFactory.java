package com.philips.pins.shinelib.utility;

import android.support.annotation.NonNull;

import com.philips.pins.shinelib.SHNDevice;

import java.util.HashSet;
import java.util.Set;

public class PersistentStorageFactory {

    public static final String SHINELIB_KEY = "SHINELIB_PREFERENCES";
    public static final String DEVICE_KEY = "_SHINELIB_DEVICE_PREFERENCES_FILE_KEY";
    public static final String USER_KEY = "SHINELIB_USER_PREFERENCES";
    public static final String DEVICE_ADDRESS_KEY = "SHINELIB_DEVICE_ADDRESS";

    public interface Extension{
        @NonNull
        PersistentStorage createPersistentStorage(@NonNull final String key);
    }

    @NonNull
    private final Extension extension;

    public PersistentStorageFactory(Extension extension) {
        this.extension = extension;
    }

    @NonNull
    public PersistentStorage getPersistentStorage() {
        return extension.createPersistentStorage(SHINELIB_KEY);
    }

    @NonNull
    public PersistentStorage getPersistentStorageForUser() {
        return extension.createPersistentStorage(USER_KEY);
    }

    @NonNull
    public PersistentStorage getPersistentStorageForDevice(@NonNull final SHNDevice device) {
        return getPersistentStorageForDevice(device.getAddress());
    }

    @NonNull
    public PersistentStorage getPersistentStorageForDevice(@NonNull final String deviceAddress) {
        String key = getDeviceKey(deviceAddress);
        saveDeviceAddress(deviceAddress);
        return extension.createPersistentStorage(key);
    }

    @NonNull
    private String getDeviceKey(@NonNull final String address) {
        return address + DEVICE_KEY;
    }

    private void saveDeviceAddress(@NonNull final String deviceAddress) {
        PersistentStorage deviceAddressStorage = getPersistentStorageForDeviceAddresses();
        Set<String> deviceAddresses = deviceAddressStorage.getStringSet(DEVICE_ADDRESS_KEY, new HashSet<String>());
        if (!deviceAddresses.contains(deviceAddress)) {
            deviceAddresses.add(deviceAddress);
            deviceAddressStorage.put(DEVICE_ADDRESS_KEY, deviceAddresses);
        }
    }

    @NonNull
    PersistentStorage getPersistentStorageForDeviceAddresses() {
        return extension.createPersistentStorage(DEVICE_ADDRESS_KEY);
    }
    
    public PersistentStorageCleaner getPersistentStorageCleaner() {
        return new PersistentStorageCleaner(this);
    }
}
