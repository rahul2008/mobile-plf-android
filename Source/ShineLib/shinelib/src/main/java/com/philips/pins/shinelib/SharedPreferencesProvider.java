/*
 * Copyright (c) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.pins.shinelib;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

public interface SharedPreferencesProvider {
    @NonNull
    SharedPreferences getSharedPreferences(String key, int mode);
    @NonNull
    String getSharedPreferencesPrefix();
}
