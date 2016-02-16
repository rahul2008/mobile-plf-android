package com.philips.pins.shinelib;

import android.support.annotation.NonNull;

import com.philips.pins.shinelib.utility.PersistentStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SHNDeviceAssociationHelper {

    public static final String ASSOCIATED_DEVICES = "ASSOCIATED_DEVICES";

    public static class AssociatedDeviceInfo {
        public final String macAddress;
        public final String deviceTypeName;

        public AssociatedDeviceInfo(String macAddress, String deviceTypeName) {
            this.macAddress = macAddress;
            this.deviceTypeName = deviceTypeName;
        }
    }

    @NonNull
    private final PersistentStorage persistentStorage;

    public SHNDeviceAssociationHelper(@NonNull final PersistentStorage persistentStorage) {
        this.persistentStorage = persistentStorage;
    }

    public void storeAssociatedDeviceInfos(List<AssociatedDeviceInfo> associatedDeviceInfos) {
        // Get the current Associated devices
        List<AssociatedDeviceInfo> oldAssociatedDeviceInfos = readAssociatedDeviceInfos();

        // Create the list of new macAddressKeys
        Set<String> newMacAddressKeys = new HashSet<>();
        for (AssociatedDeviceInfo associatedDeviceInfo : associatedDeviceInfos) {
            newMacAddressKeys.add(createKeyFromMacAddress(associatedDeviceInfo.macAddress));
        }

        persistentStorage.put(ASSOCIATED_DEVICES, newMacAddressKeys);
        for (AssociatedDeviceInfo associatedDeviceInfo : associatedDeviceInfos) {
            persistentStorage.put(createKeyFromMacAddress(associatedDeviceInfo.macAddress), associatedDeviceInfo.deviceTypeName);
        }
        for (AssociatedDeviceInfo oldAssociatedDeviceInfo : oldAssociatedDeviceInfos) {
            String oldKey = createKeyFromMacAddress(oldAssociatedDeviceInfo.macAddress);
            if (!newMacAddressKeys.contains(oldKey)) {
                persistentStorage.put(oldKey, null);
            }
        }
    }

    public synchronized List<AssociatedDeviceInfo> readAssociatedDeviceInfos() {
        List<AssociatedDeviceInfo> associatedDeviceInfos = new ArrayList<>();

        Set<String> macAddressKeys = persistentStorage.getStringSet(ASSOCIATED_DEVICES, new HashSet<String>());
        for (String macAddressKey : macAddressKeys) {
            String macAddress = createMacAddressFromKey(macAddressKey);
            String deviceTypeName = persistentStorage.getString(macAddressKey, null);
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
