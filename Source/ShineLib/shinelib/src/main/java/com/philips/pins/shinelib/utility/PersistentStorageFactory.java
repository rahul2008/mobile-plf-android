package com.philips.pins.shinelib.utility;

import android.content.Context;
import android.support.annotation.NonNull;

import com.philips.pins.shinelib.SHNDevice;

import java.util.HashSet;
import java.util.Set;

public class PersistentStorageFactory {

    public static final String SHINELIB = "SHINELIB_PREFERENCES_";
    public static final String SANDBOX_KEY = "SANDBOX";
    public static final String USER_KEY = "USER";
    public static final String DEVICE_KEY = "DEVICE_";
    public static final String DEVICE_ADDRESS_KEY = "DEVICE_ADDRESS";

    @NonNull
    private final Context context;

    public PersistentStorageFactory(@NonNull final Context context) {
        this.context = context;
    }

    @NonNull
    public PersistentStorage getPersistentStorage() {
        return createPersistentStorage(SHINELIB + SANDBOX_KEY);
    }

    @NonNull
    public PersistentStorage getPersistentStorageForUser() {
        return createPersistentStorage(SHINELIB + USER_KEY);
    }

    @NonNull
    public PersistentStorage getPersistentStorageForDevice(@NonNull final SHNDevice device) {
        return getPersistentStorageForDevice(device.getAddress());
    }

    @NonNull
    public PersistentStorage getPersistentStorageForDevice(@NonNull final String deviceAddress) {
        String key = getDeviceKey(deviceAddress);
        saveAddress(deviceAddress);
        return createPersistentStorage(key);
    }

    @NonNull
    private String getDeviceKey(@NonNull final String address) {
        return SHINELIB + DEVICE_KEY + address;
    }

    private void saveAddress(@NonNull final String deviceAddress) {
        PersistentStorage deviceAddressStorage = getPersistentStorageForDeviceAddresses();
        Set<String> deviceAddresses = deviceAddressStorage.getStringSet(DEVICE_ADDRESS_KEY, new HashSet<String>());
        if (!deviceAddresses.contains(deviceAddress)) {
            deviceAddresses.add(deviceAddress);
            deviceAddressStorage.put(DEVICE_ADDRESS_KEY, deviceAddresses);
        }
    }

    @NonNull
    PersistentStorage getPersistentStorageForDeviceAddresses() {
        return createPersistentStorage(SHINELIB + DEVICE_ADDRESS_KEY);
    }

    @NonNull
    PersistentStorage createPersistentStorage(@NonNull final String key) {
        return new PersistentStorage(context.getSharedPreferences(key, Context.MODE_PRIVATE));
    }

    public PersistencyClearing getPersistencyClearing() {
        return new PersistencyClearing(this);
    }
}
