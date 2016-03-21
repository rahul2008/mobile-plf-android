package com.philips.pins.shinelib.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.philips.pins.shinelib.SHNDevice;
import com.philips.pins.shinelib.SharedPreferencesProvider;

import java.util.HashSet;
import java.util.Set;

public class PersistentStorageFactory {

    public static final String SHINELIB_KEY = "SHINELIB_PREFERENCES";
    public static final String DEVICE_KEY = "_SHINELIB_DEVICE_PREFERENCES_FILE_KEY";
    public static final String USER_KEY = "SHINELIB_USER_PREFERENCES";
    public static final String DEVICE_ADDRESS_KEY = "SHINELIB_DEVICE_ADDRESS";

    @NonNull
    private final SharedPreferencesProvider sharedPreferencesProvider;

    public PersistentStorageFactory(SharedPreferencesProvider sharedPreferencesProvider) {
        this.sharedPreferencesProvider = sharedPreferencesProvider;
    }

    @NonNull
    public PersistentStorage getPersistentStorage() {
        return new PersistentStorage(getSharedPreferences(SHINELIB_KEY));
    }

    @NonNull
    public PersistentStorage getPersistentStorageForUser() {
        return new PersistentStorage(getSharedPreferences(USER_KEY));
    }

    @NonNull
    public PersistentStorage getPersistentStorageForDevice(@NonNull final SHNDevice device) {
        return getPersistentStorageForDevice(device.getAddress());
    }

    @NonNull
    public PersistentStorage getPersistentStorageForDevice(@NonNull final String deviceAddress) {
        String key = getDeviceKey(deviceAddress);
        saveDeviceAddress(deviceAddress);
        return new PersistentStorage(getSharedPreferences(key));
    }

    @NonNull
    private String getDeviceKey(@NonNull final String address) {
        return address + DEVICE_KEY;
    }

    private void saveDeviceAddress(@NonNull final String deviceAddress) {
        PersistentStorage deviceAddressStorage = getPersistentStorageForDeviceAddresses();
        Set<String> deviceAddresses = deviceAddressStorage.getStringSet(DEVICE_ADDRESS_KEY, new HashSet<String>());
        deviceAddresses.add(deviceAddress);
        deviceAddressStorage.put(DEVICE_ADDRESS_KEY, deviceAddresses);
    }

    @NonNull
    PersistentStorage getPersistentStorageForDeviceAddresses() {
        return new PersistentStorage(getSharedPreferences(DEVICE_ADDRESS_KEY));
    }

    public PersistentStorageCleaner getPersistentStorageCleaner() {
        return new PersistentStorageCleaner(this);
    }

    @NonNull
    private SharedPreferences getSharedPreferences(String key) {
        key = String.format("%s%s", sharedPreferencesProvider.getSharedPreferencesPrefix(), key);
        return sharedPreferencesProvider.getSharedPreferences(key, Context.MODE_PRIVATE);
    }
}
