package com.philips.pins.shinelib.utility;

import android.content.Context;
import android.support.annotation.NonNull;

import com.philips.pins.shinelib.SHNUserConfiguration;
import com.philips.pins.shinelib.SHNUserConfigurationImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DataMigrater {

    public static final String MIGRATION_ID_KEY = "MIGRATION_ID_KEY";
    public static final int MIGRATION_ID = 1;

    public static final List<String> oldShinePreferencesNames = new ArrayList<>();

    static {
        oldShinePreferencesNames.add("com.philips.pins.shinelib.utility.ShinePreferenceWrapper.SHINELIBPLUGINMOONSHINE_PREFERENCES_FILE_KEY");
        oldShinePreferencesNames.add("com.philips.a.a.h.k.SHINELIBPLUGINMOONSHINE_PREFERENCES_FILE_KEY");
    }

    public static final List<String> oldDevicePreferencesSuffixes = new ArrayList<>();

    static {
        oldDevicePreferencesSuffixes.add("com.philips.pins.shinelib.utility.ShinePreferenceWrapper_PREFERENCES_FILE_KEY");
        oldDevicePreferencesSuffixes.add("com.philips.a.a.h.k_PREFERENCES_FILE_KEY");
    }

    public static final List<String> oldUserPreferencesNames = new ArrayList<>();

    static {
        oldUserPreferencesNames.add("SHNUserConfigurationImpl_preferences");
        oldUserPreferencesNames.add("by_preferences");
    }

    public static final Map<String, String> userKeyMapping = new HashMap<>();

    static {
        userKeyMapping.put("ClockFormat", "CLOCK_FORMAT_KEY");
        userKeyMapping.put("USER_CONFIG_DATE_OF_BIRTH", "DATE_OF_BIRTH_KEY");
        userKeyMapping.put("USER_CONFIG_DECIMAL_SEPARATOR", "DECIMAL_SEPARATOR_KEY");
        userKeyMapping.put("USER_CONFIG_HANDEDNESS", "HANDEDNESS_KEY");
        userKeyMapping.put("USER_CONFIG_HEIGHT_IN_CM", "HEIGHT_IN_CM_KEY");
        userKeyMapping.put("USER_CONFIG_ISO_LANGUAGE_CODE", "ISO_LANGUAGE_CODE_KEY");
        userKeyMapping.put("ISO_COUNTRY_CODE_KEY", "ISO_COUNTRY_CODE_KEY");
        userKeyMapping.put("USER_CONFIG_MAX_HEART_RATE", "MAX_HEART_RATE_KEY");
        userKeyMapping.put("USER_CONFIG_RESTING_HEART_RATE", "RESTING_HEART_RATE_KEY");
        userKeyMapping.put("USER_CONFIG_SEX", "SEX_KEY");
        userKeyMapping.put("USER_CONFIG_USE_METRIC_SYSTEM", "USE_METRIC_SYSTEM_KEY");
        userKeyMapping.put("USER_CONFIG_WEIGHT_IN_KG", "WEIGHT_IN_KG_KEY");
        userKeyMapping.put("USER_CONFIG_INCREMENT", "CHANGE_INCREMENT_KEY");
    }

    public void execute(@NonNull final Context context, final PersistentStorageFactory storageFactory) {
        PersistentStorage newRootStorage = storageFactory.getPersistentStorage();

        Integer migrationId = newRootStorage.get(MIGRATION_ID_KEY);
        if (migrationId != null && migrationId == MIGRATION_ID) {
            return;
        }

        PersistentStorage newUserStorage = storageFactory.getPersistentStorageForUser();

        moveUserData(context, newUserStorage);
        moveShineAndDeviceData(context, storageFactory, newRootStorage);
        moveUserConfigData(newRootStorage, newUserStorage);
        moveUserConfigData(newUserStorage, newUserStorage);
        convertUserConfigValues(newUserStorage);

        newRootStorage.put(MIGRATION_ID_KEY, MIGRATION_ID);
    }

    private void moveUserData(final @NonNull Context context, final PersistentStorage newUserStorage) {
        for (final String oldUserPreferencesName : oldUserPreferencesNames) {
            PersistentStorage oldUserStorage = new PersistentStorage(context.getSharedPreferences(oldUserPreferencesName, Context.MODE_PRIVATE));
            moveData(oldUserStorage, newUserStorage);
        }
    }

    private void moveShineAndDeviceData(final @NonNull Context context, final PersistentStorageFactory storageFactory, final PersistentStorage newRootStorage) {
        for (final String oldShinePreferencesName : oldShinePreferencesNames) {
            PersistentStorage oldRootStorage = new PersistentStorage(context.getSharedPreferences(oldShinePreferencesName, Context.MODE_PRIVATE));
            Set<String> oldDevices = oldRootStorage.getStringSet(SHNPersistentStorage.ASSOCIATED_DEVICES, new HashSet<String>());
            moveData(oldRootStorage, newRootStorage);

            moveAllDevicesData(context, storageFactory, oldDevices);
        }
    }

    private void moveAllDevicesData(final @NonNull Context context, final PersistentStorageFactory storageFactory, final Set<String> oldDevices) {
        for (final String deviceAddress : oldDevices) {
            String fixedAddress = deviceAddress.replace(SHNPersistentStorage.ASSOCIATED_DEVICES, "");
            PersistentStorage newDeviceStorage = storageFactory.getPersistentStorageForDevice(fixedAddress);

            for (final String oldDevicePreferencesSuffix : oldDevicePreferencesSuffixes) {
                PersistentStorage oldDeviceStorage = new PersistentStorage(context.getSharedPreferences(fixedAddress + oldDevicePreferencesSuffix, Context.MODE_PRIVATE));
                moveData(oldDeviceStorage, newDeviceStorage);
            }
        }
    }

    private void convertUserConfigValues(final PersistentStorage newUserStorage) {
        try {
            String stringValue = newUserStorage.get(SHNUserConfigurationImpl.DECIMAL_SEPARATOR_KEY, null);
            if (stringValue != null && stringValue.length() > 0) {
                char charValue = stringValue.charAt(0);
                newUserStorage.put(SHNUserConfigurationImpl.DECIMAL_SEPARATOR_KEY, (int) charValue);
            }
        } catch (Exception ignored) {
        }

        try {
            String stringValue = newUserStorage.get(SHNUserConfigurationImpl.SEX_KEY, null);
            if (stringValue != null && stringValue.length() > 0) {
                SHNUserConfiguration.Sex sex = SHNUserConfiguration.Sex.valueOf(stringValue);
                newUserStorage.put(SHNUserConfigurationImpl.SEX_KEY, sex);
            }
        } catch (Exception ignored) {
        }

        try {
            String stringValue = newUserStorage.get(SHNUserConfigurationImpl.HANDEDNESS_KEY, null);
            if (stringValue != null && stringValue.length() > 0) {
                SHNUserConfiguration.Handedness Handedness = SHNUserConfiguration.Handedness.valueOf(stringValue);
                newUserStorage.put(SHNUserConfigurationImpl.HANDEDNESS_KEY, Handedness);
            }
        } catch (Exception ignored) {
        }

        try {
            Float floatValue = newUserStorage.get(SHNUserConfigurationImpl.WEIGHT_IN_KG_KEY, 0f);
            if (floatValue != null) {
                newUserStorage.put(SHNUserConfigurationImpl.WEIGHT_IN_KG_KEY, (double) (float) floatValue);
            }
        } catch (Exception ignored) {
        }
    }

    private void moveUserConfigData(final PersistentStorage sourceStorage, final PersistentStorage destinationStorage) {
        for (final String oldKey : userKeyMapping.keySet()) {
            if (sourceStorage.contains(oldKey)) {
                String newKey = userKeyMapping.get(oldKey);
                Object value = sourceStorage.get(oldKey);
                sourceStorage.put(oldKey, null);
                if (newKey != null) {
                    destinationStorage.put(newKey, value);
                }
            }
        }
    }

    private void moveData(final PersistentStorage sourceStorage, final PersistentStorage destinationStorage) {
        Map<String, ?> oldStorageAll = sourceStorage.getAll();
        for (final String key : oldStorageAll.keySet()) {
            destinationStorage.put(key, sourceStorage.get(key));
        }
        sourceStorage.clear();
    }
}
