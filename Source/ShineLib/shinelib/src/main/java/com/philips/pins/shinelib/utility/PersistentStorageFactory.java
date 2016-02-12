package com.philips.pins.shinelib.utility;

import android.content.Context;
import android.support.annotation.NonNull;

import com.philips.pins.shinelib.SHNCapabilityType;
import com.philips.pins.shinelib.SHNDevice;

import java.util.HashSet;
import java.util.Set;

public class PersistentStorageFactory {

    public static final String SHINELIB = "SHINELIB_PREFERENCES_";
    public static final String SANDBOX_KEY = "SANDBOX";
    public static final String USER_KEY = "USER";
    public static final String DEVICE_KEY = "DEVICE";

    @NonNull
    private final Context context;

    public PersistentStorageFactory(@NonNull final Context context) {
        this.context = context;
    }

    public PersistentStorage getPersistentStorage() {
        return createPersistentStorage(SHINELIB + SANDBOX_KEY);
    }

    public PersistentStorage getPersistentStorageForUser() {
        return createPersistentStorage(SHINELIB + USER_KEY);
    }

    public PersistentStorage getPersistentStorageForDevice(@NonNull final SHNDevice device) {
        String key = SHINELIB + DEVICE_KEY + device.getAddress();
        saveKey(key);
        return createPersistentStorage(key);
    }

    public PersistentStorage getPersistentStorageForDeviceCapability(@NonNull final SHNDevice device, @NonNull final SHNCapabilityType capabilityType) {
        String key = SHINELIB + DEVICE_KEY + device.getAddress() + capabilityType.name();
        saveKey(key);
        return createPersistentStorage(key);
    }

    private void saveKey(@NonNull final String key) {
        PersistentStorage keyStorePersistentStorage = createPersistentStorage(SHINELIB + DEVICE_KEY);
        Set<String> keyStore = keyStorePersistentStorage.getStringSet(DEVICE_KEY, new HashSet<String>());
        if (!keyStore.contains(key)) {
            keyStore.add(key);
            keyStorePersistentStorage.put(DEVICE_KEY, keyStore);
        }
    }

    @NonNull
    protected PersistentStorage createPersistentStorage(@NonNull final String key) {
        return new PersistentStorage(context.getSharedPreferences(key, Context.MODE_PRIVATE));
    }

    public void clearUserData() {
        getPersistentStorageForUser().clear();
    }

    public void clearDeviceData(@NonNull final SHNDevice device) {
        String address = device.getAddress();
        PersistentStorage keyStorePersistentStorage = createPersistentStorage(SHINELIB + DEVICE_KEY);

        Set<String> keyStore = keyStorePersistentStorage.getStringSet(DEVICE_KEY, new HashSet<String>());
        HashSet<String> keysToRemove = new HashSet<>();
        for (final String key : keyStore) {
            if (key.contains(address)) {
                keysToRemove.add(key);
                PersistentStorage persistentStorage = createPersistentStorage(key);
                persistentStorage.clear();
            }
        }

        for (final String key : keysToRemove) {
            keyStore.remove(key);
        }

        keyStorePersistentStorage.put(DEVICE_KEY, keyStore);
    }

    public void clearAllDevices() {
        PersistentStorage keyStorePersistentStorage = createPersistentStorage(SHINELIB + DEVICE_KEY);
        Set<String> keyStore = keyStorePersistentStorage.getStringSet(DEVICE_KEY, new HashSet<String>());
        keyStorePersistentStorage.clear();

        for (final String key : keyStore) {
            PersistentStorage persistentStorage = createPersistentStorage(key);
            persistentStorage.clear();
        }
    }

    public void clearAllData() {
        clearAllDevices();
        clearUserData();
        getPersistentStorage().clear();
    }
}
