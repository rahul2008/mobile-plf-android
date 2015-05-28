package com.pins.philips.shinelib.utility;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by 310188215 on 19/05/15.
 */
public class ShinePreferenceWrapper {
    private static final String SHINELIB_PREFERENCES_FILE_KEY = ShinePreferenceWrapper.class.getCanonicalName() + ".SHINELIBPLUGINMOONSHINE_PREFERENCES_FILE_KEY";
    public static final String ASSOCIATED_DEVICES = "ASSOCIATED_DEVICES";
    private final SharedPreferences sharedPreferences;

    public static class AssociatedDeviceInfo {
        public final String macAddress;
        public final String deviceTypeName;

        public AssociatedDeviceInfo(String macAddress, String deviceTypeName) {
            this.macAddress = macAddress;
            this.deviceTypeName = deviceTypeName;
        }
    }

    public ShinePreferenceWrapper(Context context) {
        sharedPreferences = context.getSharedPreferences(SHINELIB_PREFERENCES_FILE_KEY, Context.MODE_PRIVATE);
    }

    public synchronized void storeAssociatedDeviceInfos(List<AssociatedDeviceInfo> associatedDeviceInfos) {
        // Get the current Associated devices
        List<AssociatedDeviceInfo> oldAssociatedDeviceInfos = readAssociatedDeviceInfos();

        // Create the list of new macAddressKeys
        Set<String> newMacAddressKeys = new HashSet<>();
        for (AssociatedDeviceInfo associatedDeviceInfo: associatedDeviceInfos) {
            newMacAddressKeys.add(createKeyFromMacAddress(associatedDeviceInfo.macAddress));
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(ASSOCIATED_DEVICES, newMacAddressKeys);
        for (AssociatedDeviceInfo associatedDeviceInfo : associatedDeviceInfos) {
            editor.putString(createKeyFromMacAddress(associatedDeviceInfo.macAddress), associatedDeviceInfo.deviceTypeName);
        }
        for (AssociatedDeviceInfo oldAssociatedDeviceInfo: oldAssociatedDeviceInfos) {
            if (!newMacAddressKeys.contains(oldAssociatedDeviceInfo.macAddress)) {
                editor.remove(oldAssociatedDeviceInfo.macAddress);
            }
        }
        editor.commit();
    }

    public synchronized List<AssociatedDeviceInfo> readAssociatedDeviceInfos() {
        List<AssociatedDeviceInfo> associatedDeviceInfos = new ArrayList<>();

        Set<String> macAddressKeys = sharedPreferences.getStringSet(ASSOCIATED_DEVICES, Collections.EMPTY_SET);
        for (String macAddressKey: macAddressKeys) {
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
}
