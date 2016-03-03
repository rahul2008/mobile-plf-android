/*
 * Copyright (c) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.pins.shinelib;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

public interface SharedPreferencesProvider {
    /**
     * see Context getSharedPreferences
     */
    @NonNull
    SharedPreferences getSharedPreferences(String name, int mode);

    /**
     * The shared preferences prefix is added to the key for each request to getSharedPreferences.
     * It is up to the implementation of this interface to make sure that each SharedPreferenceProvider
     * returns a unique non null and non empty string.
     * @return a unique non null non empty string.
     */
    @NonNull
    String getSharedPreferencesPrefix();
}
