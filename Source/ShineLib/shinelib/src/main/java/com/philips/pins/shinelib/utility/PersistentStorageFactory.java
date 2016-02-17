package com.philips.pins.shinelib.utility;

import android.content.Context;
import android.support.annotation.NonNull;

import com.philips.pins.shinelib.SHNDevice;

import java.util.HashSet;
import java.util.Set;

public class PersistentStorageFactory {

    public static final String SHINELIB_KEY = "SHINELIB_PREFERENCES";
    public static final String DEVICE_KEY = "_SHINELIB_DEVICE_PREFERENCES_FILE_KEY";
    public static final String USER_KEY = "SHNUserConfigurationImpl_preferences";
    public static final String DEVICE_ADDRESS_KEY = "SHINELIB_DEVICE_ADDRESS";

    @NonNull
    private final Context context;

    public PersistentStorageFactory(@NonNull final Context context) {
        this.context = context;
    }

    @NonNull
    public PersistentStorage getPersistentStorage() {
        return createPersistentStorage(SHINELIB_KEY);
    }

    @NonNull
    public PersistentStorage getPersistentStorageForUser() {
        return createPersistentStorage(USER_KEY);
    }

    @NonNull
    public PersistentStorage getPersistentStorageForDevice(@NonNull final SHNDevice device) {
        return getPersistentStorageForDevice(device.getAddress());
    }

    @NonNull
    public PersistentStorage getPersistentStorageForDevice(@NonNull final String deviceAddress) {
        String key = getDeviceKey(deviceAddress);
        saveDeviceAddress(deviceAddress);
        return createPersistentStorage(key);
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
        return createPersistentStorage(DEVICE_ADDRESS_KEY);
    }

    @NonNull
    PersistentStorage createPersistentStorage(@NonNull final String key) {
        return new PersistentStorage(context.getSharedPreferences(key, Context.MODE_PRIVATE));
    }

    public PersistentStorageCleaner getPersistStorageCleaner() {
        return new PersistentStorageCleaner(this);
    }
}
