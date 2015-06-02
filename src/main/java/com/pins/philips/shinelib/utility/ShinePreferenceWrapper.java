package com.pins.philips.shinelib.utility;

import android.content.Context;
import android.content.SharedPreferences;

import com.pins.philips.shinelib.datatypes.SHNUserConfiguration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by 310188215 on 19/05/15.
 */
public class ShinePreferenceWrapper {
    private static final String SHINELIB_PREFERENCES_FILE_KEY = ShinePreferenceWrapper.class.getCanonicalName() + ".SHINELIBPLUGINMOONSHINE_PREFERENCES_FILE_KEY";
    public static final String ASSOCIATED_DEVICES = "ASSOCIATED_DEVICES";
    private static final String USER_CONFIG_DATE_OF_BIRTH = "USER_CONFIG_DATE_OF_BIRTH";
    private static final String USER_CONFIG_MAX_HEART_RATE = "USER_CONFIG_MAX_HEART_RATE";
    private static final String USER_CONFIG_RESTING_HEART_RATE = "USER_CONFIG_RESTING_HEART_RATE";
    private static final String USER_CONFIG_WEIGHT_IN_KG = "USER_CONFIG_WEIGHT_IN_KG";
    private static final String USER_CONFIG_HEIGHT_IN_CM = "USER_CONFIG_HEIGHT_IN_CM";
    private static final String USER_CONFIG_SEX = "USER_CONFIG_SEX";

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

    public synchronized void storeUserConfiguration(SHNUserConfiguration shnUserConfiguration) {
        SharedPreferences.Editor edit = sharedPreferences.edit();

        if (shnUserConfiguration.getDateOfBirth() != null) {
            edit.putLong(USER_CONFIG_DATE_OF_BIRTH, shnUserConfiguration.getDateOfBirth().getTime());
        } else {
            edit.remove(USER_CONFIG_DATE_OF_BIRTH);
        }

        if (shnUserConfiguration.getHeightInCm() != null) {
            edit.putInt(USER_CONFIG_HEIGHT_IN_CM, shnUserConfiguration.getHeightInCm());
        } else {
            edit.remove(USER_CONFIG_HEIGHT_IN_CM);
        }

        if (shnUserConfiguration.getMaxHeartRate() != null) {
            edit.putInt(USER_CONFIG_MAX_HEART_RATE, shnUserConfiguration.getMaxHeartRate());
        } else {
            edit.remove(USER_CONFIG_MAX_HEART_RATE);
        }

        if (shnUserConfiguration.getRestingHeartRate() != null) {
            edit.putInt(USER_CONFIG_RESTING_HEART_RATE, shnUserConfiguration.getRestingHeartRate());
        } else {
            edit.remove(USER_CONFIG_RESTING_HEART_RATE);
        }

        if (shnUserConfiguration.getWeightInKg() != null) {
            edit.putFloat(USER_CONFIG_WEIGHT_IN_KG, (float) (double) shnUserConfiguration.getWeightInKg());
        } else {
            edit.remove(USER_CONFIG_WEIGHT_IN_KG);
        }

        if (shnUserConfiguration.getSex() != null) {
            edit.putString(USER_CONFIG_SEX, shnUserConfiguration.getSex().name());
        } else {
            edit.remove(USER_CONFIG_SEX);
        }
        edit.commit();
    }

    public synchronized void readUserConfiguration(SHNUserConfiguration shnUserConfiguration) {
        long longValue = sharedPreferences.getLong(USER_CONFIG_DATE_OF_BIRTH, -1l);
        shnUserConfiguration.setDateOfBirth(null);
        if (longValue != -1l) {
            shnUserConfiguration.setDateOfBirth(new Date(longValue));
        }

        int intValue = sharedPreferences.getInt(USER_CONFIG_HEIGHT_IN_CM, -1);
        shnUserConfiguration.setHeightInCm(null);
        if (intValue != -1) {
            shnUserConfiguration.setHeightInCm(intValue);
        }

        intValue = sharedPreferences.getInt(USER_CONFIG_MAX_HEART_RATE, -1);
        shnUserConfiguration.setMaxHeartRate(null);
        if (intValue != -1) {
            shnUserConfiguration.setMaxHeartRate(intValue);
        }

        intValue = sharedPreferences.getInt(USER_CONFIG_RESTING_HEART_RATE, -1);
        shnUserConfiguration.setRestingHeartRate(null);
        if (intValue != -1) {
            shnUserConfiguration.setRestingHeartRate(intValue);
        }

        float floatValue = sharedPreferences.getFloat(USER_CONFIG_WEIGHT_IN_KG, Float.NaN);
        shnUserConfiguration.setWeightInKg(null);
        if (floatValue != Float.NaN) {
            shnUserConfiguration.setWeightInKg((double) floatValue);
        }

        String stringValue = sharedPreferences.getString(USER_CONFIG_SEX, null);
        shnUserConfiguration.setSex(null);
        if (stringValue != null) {
            shnUserConfiguration.setSex(SHNUserConfiguration.Sex.valueOf(stringValue));
        }
    }
}
