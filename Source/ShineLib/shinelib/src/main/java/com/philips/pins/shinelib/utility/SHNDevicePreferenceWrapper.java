/*
 * Copyright (c) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.pins.shinelib.utility;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class SHNDevicePreferenceWrapper {

    public static final String SHN_DEVICE_PREFERENCES_FILE_KEY = "_SHINELIB_DEVICE_PREFERENCES_FILE_KEY";

    @NonNull
    private final SharedPreferences sharedPreferences;

    public static SHNDevicePreferenceWrapper createNewSHNDevicePreferenceWrapper(@NonNull final Context context, @NonNull final String address) {
        return new SHNDevicePreferenceWrapper(context, address);
    }

    private SHNDevicePreferenceWrapper(@NonNull final Context context, @NonNull final String address) {
        sharedPreferences = context.getSharedPreferences(address + SHN_DEVICE_PREFERENCES_FILE_KEY, Context.MODE_PRIVATE);
    }

    public SharedPreferences.Editor edit() {
        return sharedPreferences.edit();
    }

    public int getInt(@NonNull final String key) {
        return sharedPreferences.getInt(key, -1);
    }

    public long getLong(@NonNull final String key) {
        return sharedPreferences.getLong(key, -1L);
    }

    @Nullable
    public String getString(@NonNull final String key) {
        return sharedPreferences.getString(key, null);
    }

    @SuppressLint("CommitPrefEdits")
    public void putString(@NonNull final String key, final String value) {
        sharedPreferences.edit().putString(key, value).commit();
    }

    @SuppressLint("CommitPrefEdits")
    public void putLong(@NonNull final String key, final long value) {
        sharedPreferences.edit().putLong(key, value).commit();
    }
}
