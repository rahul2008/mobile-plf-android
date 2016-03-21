package com.philips.pins.shinelib;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

class ThreadGuardedSharedPreferencesProvidedWrapper implements SharedPreferencesProvider {

    private SharedPreferencesProvider sharedPreferencesProvider;
    private long internalThreadID;

    public ThreadGuardedSharedPreferencesProvidedWrapper(SharedPreferencesProvider sharedPreferencesProvider, long internalThreadID) {
        this.sharedPreferencesProvider = sharedPreferencesProvider;
        this.internalThreadID = internalThreadID;
    }

    @NonNull
    @Override
    public SharedPreferences getSharedPreferences(String name, int mode) {
        return new ThreadGuardedSharedPreferencesWrapper(sharedPreferencesProvider.getSharedPreferences(name, mode), internalThreadID);
    }

    @NonNull
    @Override
    public String getSharedPreferencesPrefix() {
        return sharedPreferencesProvider.getSharedPreferencesPrefix();
    }
}
