package com.philips.pins.shinelib.utility;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class SHNDevicePreferenceWrapper {

    private static final String SHN_DEVICE_PREFERENCES_FILE_KEY = ShinePreferenceWrapper.class.getCanonicalName() + "_PREFERENCES_FILE_KEY";

    private final SharedPreferences sharedPreferences;

    public SHNDevicePreferenceWrapper(Context context, String address) {
        sharedPreferences = context.getSharedPreferences(address + SHN_DEVICE_PREFERENCES_FILE_KEY, Context.MODE_PRIVATE);
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
