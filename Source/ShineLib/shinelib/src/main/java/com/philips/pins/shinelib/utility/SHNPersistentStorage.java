/*
 * Copyright (c) Koninklijke Philips N.V., 2015, 2016.
 * All rights reserved.
 */

package com.philips.pins.shinelib.utility;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Deprecated. Use {@link com.philips.pins.shinelib.utility.PersistentStorage} instead.
 */
@Deprecated
public class SHNPersistentStorage {
    public static final String SHINELIB_PREFERENCES_FILE_KEY = "SHINELIB_PREFERENCES";
    public static final String ASSOCIATED_DEVICES = "ASSOCIATED_DEVICES";

    private final SharedPreferences sharedPreferences;

    @Deprecated
    public static class AssociatedDeviceInfo {
        public final String macAddress;
        public final String deviceTypeName;

        @Deprecated
        public AssociatedDeviceInfo(String macAddress, String deviceTypeName) {
            this.macAddress = macAddress;
            this.deviceTypeName = deviceTypeName;
        }
    }

    @Deprecated
    public SHNPersistentStorage(Context context) {
        sharedPreferences = context.getSharedPreferences(SHINELIB_PREFERENCES_FILE_KEY, Context.MODE_PRIVATE);
    }

    @Deprecated
    public synchronized void storeAssociatedDeviceInfos(List<AssociatedDeviceInfo> associatedDeviceInfos) {
        // Get the current Associated devices
        List<AssociatedDeviceInfo> oldAssociatedDeviceInfos = readAssociatedDeviceInfos();

        // Create the list of new macAddressKeys
        Set<String> newMacAddressKeys = new HashSet<>();
        for (AssociatedDeviceInfo associatedDeviceInfo : associatedDeviceInfos) {
            newMacAddressKeys.add(createKeyFromMacAddress(associatedDeviceInfo.macAddress));
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(ASSOCIATED_DEVICES, newMacAddressKeys);
        for (AssociatedDeviceInfo associatedDeviceInfo : associatedDeviceInfos) {
            editor.putString(createKeyFromMacAddress(associatedDeviceInfo.macAddress), associatedDeviceInfo.deviceTypeName);
        }
        for (AssociatedDeviceInfo oldAssociatedDeviceInfo : oldAssociatedDeviceInfos) {
            String oldKey = createKeyFromMacAddress(oldAssociatedDeviceInfo.macAddress);
            if (!newMacAddressKeys.contains(oldKey)) {
                editor.remove(oldKey);
            }
        }
        editor.apply();
    }

    @Deprecated
    public synchronized List<AssociatedDeviceInfo> readAssociatedDeviceInfos() {
        List<AssociatedDeviceInfo> associatedDeviceInfos = new ArrayList<>();

        Set<String> macAddressKeys = sharedPreferences.getStringSet(ASSOCIATED_DEVICES, Collections.EMPTY_SET);
        for (String macAddressKey : macAddressKeys) {
            String macAddress = createMacAddressFromKey(macAddressKey);
            String deviceTypeName = sharedPreferences.getString(macAddressKey, null);
            if (deviceTypeName != null) {
                associatedDeviceInfos.add(new AssociatedDeviceInfo(macAddress, deviceTypeName));
            }
        }

        return associatedDeviceInfos;
    }

    private String createMacAddressFromKey(String macAddressKey) {
        return macAddressKey.substring(ASSOCIATED_DEVICES.length());
    }

    private String createKeyFromMacAddress(String macAddress) {
        return ASSOCIATED_DEVICES + macAddress;
    }

    @Deprecated
    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    @Deprecated
    public SharedPreferences.Editor edit() {
        return sharedPreferences.edit();
    }

    @Deprecated
    public long getLong(String key) {
        return sharedPreferences.getLong(key, -1);
    }

    @Deprecated
    public int getInt(String key) {
        return sharedPreferences.getInt(key, -1);
    }

    @Deprecated
    public float getFloat(String key) {
        return sharedPreferences.getFloat(key, Float.NaN);
    }

    @Deprecated
    public String getString(String key) {
        return sharedPreferences.getString(key, null);
    }

    @Deprecated
    public Boolean getBoolean(String key) {
        if (sharedPreferences.contains(key)) {
            return sharedPreferences.getBoolean(key, false);
        }
        return null;
    }
}
