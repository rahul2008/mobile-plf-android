package com.philips.pins.shinelib;

import android.content.SharedPreferences;
import android.os.Handler;
import android.support.annotation.NonNull;

class ThreadGuardedSharedPreferencesProvidedWrapper implements SharedPreferencesProvider {

    private SharedPreferencesProvider sharedPreferencesProvider;
    private Handler handler;
    private long internalThreadID;

    public ThreadGuardedSharedPreferencesProvidedWrapper(SharedPreferencesProvider sharedPreferencesProvider, Handler handler, long internalThreadID) {
        this.sharedPreferencesProvider = sharedPreferencesProvider;
        this.handler = handler;
        this.internalThreadID = internalThreadID;
    }

    @NonNull
    @Override
    public SharedPreferences getSharedPreferences(String name, int mode) {
        return new ThreadGuardedSharedPreferencesWrapper(sharedPreferencesProvider.getSharedPreferences(name, mode), handler, internalThreadID);
    }

    @NonNull
    @Override
    public String getSharedPreferencesPrefix() {
        return sharedPreferencesProvider.getSharedPreferencesPrefix();
    }
}
