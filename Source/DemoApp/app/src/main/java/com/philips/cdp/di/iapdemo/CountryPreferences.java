/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iapdemo;

import android.content.Context;
import android.content.SharedPreferences;

public class CountryPreferences {

    private static final String PREF_FILE = "country";
    private static final String COUNTRY_SELECTED = "country_selected";

    private SharedPreferences mPrefs;

    public CountryPreferences(Context context) {
        mPrefs = context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
    }

    int getSelectedCountryIndex() {
        return mPrefs.getInt(COUNTRY_SELECTED, 0);
    }

    void saveCountryPrefrence(int country) {
        mPrefs.edit().putInt(COUNTRY_SELECTED, country).commit();
    }

    void clearCountryPreference() {
        mPrefs.edit().remove(COUNTRY_SELECTED).commit();
    }
}
