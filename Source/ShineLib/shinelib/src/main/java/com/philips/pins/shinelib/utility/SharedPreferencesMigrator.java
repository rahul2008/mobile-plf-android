package com.philips.pins.shinelib.utility;

import com.philips.pins.shinelib.SharedPreferencesProvider;

public class SharedPreferencesMigrator {
    private final SharedPreferencesProvider sourceProvider;
    private final SharedPreferencesProvider destinationProvider;

    public SharedPreferencesMigrator(SharedPreferencesProvider sourceProvider, SharedPreferencesProvider destinationProvider) {
        this.sourceProvider = sourceProvider;
        this.destinationProvider = destinationProvider;
    }

    public void execute() {

    }

    public boolean destinationContainsData() {
        PersistentStorageFactory persistentStorageFactory = new PersistentStorageFactory(destinationProvider);
        return persistentStorageFactory.getPersistentStorage().contains(DataMigrater.MIGRATION_ID_KEY);
    }
}
