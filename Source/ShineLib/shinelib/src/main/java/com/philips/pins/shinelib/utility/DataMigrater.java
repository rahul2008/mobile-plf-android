package com.philips.pins.shinelib.utility;

import android.content.Context;
import android.support.annotation.NonNull;

import com.philips.pins.shinelib.SHNDeviceAssociationHelper;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DataMigrater {

    public static final String oldShinePreferences = "com.philips.pins.shinelib.utility.ShinePreferenceWrapper.SHINELIBPLUGINMOONSHINE_PREFERENCES_FILE_KEY";
    public static final String oldDevicePreferencesSuffix = "com.philips.pins.shinelib.utility.ShinePreferenceWrapper_PREFERENCES_FILE_KEY";

    public void execute(@NonNull final Context context, final PersistentStorageFactory storageFactory) {
        PersistentStorage oldRootStorage = new PersistentStorage(context.getSharedPreferences(oldShinePreferences, Context.MODE_PRIVATE));
        PersistentStorage newRootStorage = storageFactory.getPersistentStorage();

        Set<String> oldDevices = oldRootStorage.getStringSet(SHNDeviceAssociationHelper.ASSOCIATED_DEVICES, new HashSet<String>());

        moveData(oldRootStorage, newRootStorage);

        for (final String deviceAddress : oldDevices) {
            String fixedAddress = deviceAddress.replace(SHNDeviceAssociationHelper.ASSOCIATED_DEVICES, "");
            PersistentStorage oldDeviceStorage = new PersistentStorage(context.getSharedPreferences(fixedAddress + oldDevicePreferencesSuffix, Context.MODE_PRIVATE));
            PersistentStorage newDeviceStorage = storageFactory.getPersistentStorageForDevice(fixedAddress);
            moveData(oldDeviceStorage, newDeviceStorage);
        }
    }

    private void moveData(final PersistentStorage oldStorage, final PersistentStorage newStorage) {
        Map<String, ?> oldStorageAll = oldStorage.getAll();
        for (final String key : oldStorageAll.keySet()) {
            newStorage.put(key, oldStorage.get(key));
        }
        oldStorage.clear();
    }
}
