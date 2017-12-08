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

/**
 * Deprecated. Use {@link com.philips.pins.shinelib.utility.PersistentStorage} instead.
 */
@Deprecated
public class SHNDevicePersistentStorage {

    public static final String SHN_DEVICE_PREFERENCES_FILE_KEY = "_SHINELIB_DEVICE_PREFERENCES_FILE_KEY";

    @NonNull
    private final SharedPreferences sharedPreferences;

    /**
     * Deprecated. Obtain the proper device storage via {@link com.philips.pins.shinelib.utility.PersistentStorageFactory#getPersistentStorageForDevice(String)}
     */
    @Deprecated
    public static SHNDevicePersistentStorage createNewSHNDevicePersistentStorage(@NonNull final Context context, @NonNull final String address) {
        return new SHNDevicePersistentStorage(context, address);
    }

    @Deprecated
    public SHNDevicePersistentStorage(@NonNull final Context context, @NonNull final String address) {
        sharedPreferences = context.getSharedPreferences(address + SHN_DEVICE_PREFERENCES_FILE_KEY, Context.MODE_PRIVATE);
    }

    @NonNull
    @Deprecated
    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    @NonNull
    @Deprecated
    public SharedPreferences.Editor edit() {
        return sharedPreferences.edit();
    }

    @Deprecated
    public boolean getBoolean(@NonNull final String key) {
        return sharedPreferences.getBoolean(key, false);
    }

    @Deprecated
    public int getInt(@NonNull final String key) {
        return sharedPreferences.getInt(key, -1);
    }

    @Deprecated
    public long getLong(@NonNull final String key) {
        return sharedPreferences.getLong(key, -1L);
    }

    @Nullable
    @Deprecated
    public String getString(@NonNull final String key) {
        return sharedPreferences.getString(key, null);
    }

    @SuppressLint("CommitPrefEdits")
    @Deprecated
    public void putString(@NonNull final String key, final String value) {
        sharedPreferences.edit().putString(key, value).commit();
    }

    @SuppressLint("CommitPrefEdits")
    @Deprecated
    public void putLong(@NonNull final String key, final long value) {
        sharedPreferences.edit().putLong(key, value).commit();
    }

    @SuppressLint("CommitPrefEdits")
    @Deprecated
    public void putInt(@NonNull final String key, final int value) {
        sharedPreferences.edit().putInt(key, value).commit();
    }

    @SuppressLint("CommitPrefEdits")
    @Deprecated
    public void putBoolean(@NonNull final String key, final boolean value) {
        sharedPreferences.edit().putBoolean(key, value).commit();
    }
}
