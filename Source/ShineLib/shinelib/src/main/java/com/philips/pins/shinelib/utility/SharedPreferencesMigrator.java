package com.philips.pins.shinelib.utility;

import java.util.Map;
import java.util.Set;

public class SharedPreferencesMigrator {
    private final PersistentStorageFactory sourceFactory;
    private final PersistentStorageFactory destinationFactory;

    public SharedPreferencesMigrator(PersistentStorageFactory sourceFactory, PersistentStorageFactory destinationFactory) {
        this.sourceFactory = sourceFactory;
        this.destinationFactory = destinationFactory;
    }

    public boolean destinationPersistentStorageContainsData() {
        return destinationFactory.getPersistentStorage().contains(DataMigrater.MIGRATION_ID_KEY);
    }

    public void execute() {
        moveUserData();
        moveAssociatedDevices();
        movePersistentData();

        sourceFactory.getPersistentStorageCleaner().clearAllData();
    }

    private void moveUserData() {
        PersistentStorage sourcePersistentStorageForUser = sourceFactory.getPersistentStorageForUser();
        PersistentStorage destinationPersistentStorageForUser = destinationFactory.getPersistentStorageForUser();

        moveAllKey(sourcePersistentStorageForUser, destinationPersistentStorageForUser);
    }

    private void moveAssociatedDevices() {
        PersistentStorage sourcePersistentStorageForDeviceAddresses = sourceFactory.getPersistentStorageForDeviceAddresses();

        Map<String, ?> all = sourcePersistentStorageForDeviceAddresses.getAll();
        for (String key : all.keySet()) {
            Set<String> macAddresses = sourcePersistentStorageForDeviceAddresses.getStringSet(key, null);

            if (macAddresses != null) {
                for (String macAddress : macAddresses) {
                    PersistentStorage sourceDevicePersistentStorage = sourceFactory.getPersistentStorageForDevice(macAddress);
                    PersistentStorage destinationDevicePersistentStorage = destinationFactory.getPersistentStorageForDevice(macAddress);

                    moveAllKey(sourceDevicePersistentStorage, destinationDevicePersistentStorage);
                }
            }
        }
    }

    private void movePersistentData() {
        PersistentStorage sourcePersistentStorage = sourceFactory.getPersistentStorage();
        PersistentStorage destinationPersistentStorage = destinationFactory.getPersistentStorage();

        moveAllKey(sourcePersistentStorage, destinationPersistentStorage);
    }

    private void moveAllKey(PersistentStorage source, PersistentStorage destination) {
        Map<String, ?> all = source.getAll();

        for (String key : all.keySet()) {
            destination.put(key, source.get(key));
        }
    }
}
