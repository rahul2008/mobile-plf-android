package com.philips.pins.shinelib.utility;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DataMigrater {

    public static final String TAG = "DataMigrater";
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

        Integer migrationId = newRootStorage.get(TAG);
        if (migrationId != null && migrationId == MIGRATION_ID) {
            return;
        }

        PersistentStorage newUserStorage = storageFactory.getPersistentStorageForUser();
        for (final String oldUserPreferencesName : oldUserPreferencesNames) {
            PersistentStorage oldUserStorage = new PersistentStorage(context.getSharedPreferences(oldUserPreferencesName, Context.MODE_PRIVATE));
            moveData(oldUserStorage, newUserStorage);
        }

        for (final String oldShinePreferencesName : oldShinePreferencesNames) {
            PersistentStorage oldRootStorage = new PersistentStorage(context.getSharedPreferences(oldShinePreferencesName, Context.MODE_PRIVATE));
            Set<String> oldDevices = oldRootStorage.getStringSet(SHNPersistentStorage.ASSOCIATED_DEVICES, new HashSet<String>());
            moveData(oldRootStorage, newRootStorage);

            for (final String deviceAddress : oldDevices) {
                String fixedAddress = deviceAddress.replace("ASSOCIATED_DEVICES", "");
                PersistentStorage newDeviceStorage = storageFactory.getPersistentStorageForDevice(fixedAddress);

                for (final String oldDevicePreferencesSuffix : oldDevicePreferencesSuffixes) {
                    PersistentStorage oldDeviceStorage = new PersistentStorage(context.getSharedPreferences(fixedAddress + oldDevicePreferencesSuffix, Context.MODE_PRIVATE));
                    moveData(oldDeviceStorage, newDeviceStorage);
                }
            }
        }

        moveUserConfigKeyFromRootStorage(newRootStorage, newUserStorage);
        moveUserConfigKeyFromRootStorage(newUserStorage, newUserStorage);

        newRootStorage.put(TAG, MIGRATION_ID);
    }

    private void moveUserConfigKeyFromRootStorage(final PersistentStorage sourceStorage, final PersistentStorage destinationStorage) {
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
