package com.philips.platform.appframework.abtesting;

import android.content.Context;
import android.content.SharedPreferences;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.AppInfraLogEventID;
import com.philips.platform.appinfra.logging.LoggingInterface;

import java.util.Map;

class AbTestingLocalCache {


    private static final String ABTEST_PREFERENCE = "abTest_preference";
    // TODO: Deepthi Use uniform coding standard, remove 'm' or use across all classes
    private final SharedPreferences mSharedPreferences;

    // TODO: Deepthi could name as abtestcachedobj
    private String key = "cacheobject";
    private AppInfraInterface appInfraInterface;


    AbTestingLocalCache(Context context) {
        mSharedPreferences = context.getSharedPreferences(ABTEST_PREFERENCE,
                Context.MODE_PRIVATE);
    }

    void saveToDisk(String value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
        appInfraInterface.getLogging().log(LoggingInterface.LogLevel.DEBUG, AppInfraLogEventID.AI_ABTEST_CLIENT,
                "save Cache to Preference " + value);
    }

    String fetchFromDisk() {
        return mSharedPreferences.getString(key, null);
    }

    public void initAppInfra(AppInfraInterface appInfraInterface) {
        this.appInfraInterface = appInfraInterface;
    }
}
