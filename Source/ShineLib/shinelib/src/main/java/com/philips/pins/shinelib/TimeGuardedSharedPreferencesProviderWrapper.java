/*
 * Copyright (c) Koninklijke Philips N.V., 2015, 2016, 2017.
 * All rights reserved.
 */

package com.philips.pins.shinelib;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.philips.pins.shinelib.utility.SHNLogger;

class TimeGuardedSharedPreferencesProviderWrapper implements SharedPreferencesProvider {

    public static final int DELAY_MILLIS = 50;
    private static final String TAG = "TimeGuardedSharedPrefProvider";

    private SharedPreferencesProvider sharedPreferencesProvider;
    private long internalThreadID;

    public TimeGuardedSharedPreferencesProviderWrapper(SharedPreferencesProvider sharedPreferencesProvider, long internalThreadID) {
        this.sharedPreferencesProvider = sharedPreferencesProvider;
        this.internalThreadID = internalThreadID;
    }

    @NonNull
    @Override
    public SharedPreferences getSharedPreferences(String name, int mode) {
        long startTime = getCurrentTimeInMillis();

        SharedPreferences sharedPreferences = sharedPreferencesProvider.getSharedPreferences(name, mode);
        long dif = getCurrentTimeInMillis() - startTime;

        if (dif > DELAY_MILLIS) {
            final String msg = "The internal thread is not responding! Custom SharedPreference's execution time has exceeded expected execution time of 50 ms! Execution time is " + dif;
            SHNLogger.wtf(TAG, msg);
        }

        return new TimeGuardedSharedPreferencesWrapper(sharedPreferences, internalThreadID);
    }

    @NonNull
    @Override
    public String getSharedPreferencesPrefix() {
        return sharedPreferencesProvider.getSharedPreferencesPrefix();
    }

    protected long getCurrentTimeInMillis() {
        return System.currentTimeMillis();
    }
}
