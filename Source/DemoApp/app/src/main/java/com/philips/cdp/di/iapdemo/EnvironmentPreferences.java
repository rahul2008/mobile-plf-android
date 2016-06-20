/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iapdemo;

import android.content.Context;
import android.content.SharedPreferences;

public class EnvironmentPreferences {

    private static final String PREF_FILE = "Environment";
    private static final String ENVIRONMENT_SELECTED = "environment_selected";

    private SharedPreferences mPrefs;

    public EnvironmentPreferences(Context context) {
        mPrefs = context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
    }

    int getSelectedEnvironmentIndex() {
        return mPrefs.getInt(ENVIRONMENT_SELECTED, 0);
    }

    void saveEnvironmentPrefrence(int country) {
        mPrefs.edit().putInt(ENVIRONMENT_SELECTED, country).commit();
    }

    void clearEnvironmentPreference() {
        mPrefs.edit().remove(ENVIRONMENT_SELECTED).commit();
    }
}
