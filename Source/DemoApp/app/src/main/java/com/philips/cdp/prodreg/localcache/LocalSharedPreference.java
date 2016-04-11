package com.philips.cdp.prodreg.localcache;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class LocalSharedPreference {

    public static final String PRODUCT_REGISTRATION = "product_registration";
    private Context context;

    public LocalSharedPreference(Context context) {
        this.context = context;
    }

    public void storeData(String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PRODUCT_REGISTRATION, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String getData(String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PRODUCT_REGISTRATION, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, null);
    }
}
