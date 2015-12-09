/*
 * Copyright (c) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.pins.shinelib.utility;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class SHNDevicePreferenceWrapper {

    public static final String SHN_DEVICE_PREFERENCES_FILE_KEY = "_SHINELIB_DEVICE_PREFERENCES_FILE_KEY";

    private final SharedPreferences sharedPreferences;

    public static SHNDevicePreferenceWrapper createNewSHNDevicePreferenceWrapper(Context context, String address) {
        return new SHNDevicePreferenceWrapper(context, address);
    }

    private SHNDevicePreferenceWrapper(Context context, String address) {
        sharedPreferences = context.getSharedPreferences(address + SHN_DEVICE_PREFERENCES_FILE_KEY, Context.MODE_PRIVATE);
    }

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public SharedPreferences.Editor edit() {
        return sharedPreferences.edit();
    }

    public int getInt(String key) {
        return sharedPreferences.getInt(key, -1);
    }

    public String getString(String key) {
        return sharedPreferences.getString(key, null);
    }
}
