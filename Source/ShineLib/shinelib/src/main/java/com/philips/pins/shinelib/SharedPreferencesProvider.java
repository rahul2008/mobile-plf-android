/*
 * Copyright (c) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.pins.shinelib;

import android.content.SharedPreferences;

public interface SharedPreferencesProvider {
    SharedPreferences getSharedPreferences(String key, int mode);
}
