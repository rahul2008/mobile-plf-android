package com.philips.cdp.prodreg.localcache;

import android.content.Context;
import android.content.SharedPreferences;

import com.philips.cdp.prodreg.constants.ProdRegConstants;

/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
public class ProdRegCache {

    private Context context;

    public ProdRegCache(Context context) {
        this.context = context;
    }

    public void storeStringData(String key, String value) {
        if (context != null) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(ProdRegConstants.PRODUCT_REGISTRATION, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(key, value);
            editor.commit();
        }
    }

    public void storeIntData(String key, int value) {
        if (context != null) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(ProdRegConstants.PRODUCT_REGISTRATION, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(key, value);
            editor.commit();
        }
    }

    public String getStringData(String key) {
        if (context != null) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(ProdRegConstants.PRODUCT_REGISTRATION, Context.MODE_PRIVATE);
            return sharedPreferences.getString(key, null);
        }
        return null;
    }

    public int getIntData(String key) {
        if (context != null) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(ProdRegConstants.PRODUCT_REGISTRATION, Context.MODE_PRIVATE);
            return sharedPreferences.getInt(key, 0);
        }
        return 0;
    }
}
