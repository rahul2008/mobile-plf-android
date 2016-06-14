package com.philips.cdp.prodreg.localcache;

import android.content.Context;
import android.content.SharedPreferences;

/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
public class LocalSharedPreference {

    public static final String PRODUCT_REGISTRATION = "product_registration";
    private Context context;

    public LocalSharedPreference(Context context) {
        this.context = context;
    }

    public void storeData(String key, String value) {
        if (context != null) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(PRODUCT_REGISTRATION, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(key, value);
            editor.commit();
        }
    }

    public String getData(String key) {
        if (context != null) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(PRODUCT_REGISTRATION, Context.MODE_PRIVATE);
            return sharedPreferences.getString(key, null);
        }
        return null;
    }
}
