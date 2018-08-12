package com.philips.platform.aildemo.abtesting;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

class AbTestingLocalCache {


    private static final String ABTEST_PREFERENCE = "abTest_preference";

    private final SharedPreferences mSharedPreferences;
    private String key = "cacheobject";

    AbTestingLocalCache(Context context) {
        mSharedPreferences = context.getSharedPreferences(ABTEST_PREFERENCE,
                Context.MODE_PRIVATE);
    }

    void saveToDisk(String value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(key,value);
        editor.apply();
    }

    String fetchFromDisk() {
        return mSharedPreferences.getString(key,null);
    }

    Map<String, ?> fetchAllValues() {
        return mSharedPreferences.getAll();
    }


}
