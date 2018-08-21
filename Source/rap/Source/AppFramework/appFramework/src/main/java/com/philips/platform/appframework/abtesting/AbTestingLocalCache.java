package com.philips.platform.appframework.abtesting;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.AppInfraLogEventID;
import com.philips.platform.appinfra.logging.LoggingInterface;

import java.util.Map;

class AbTestingLocalCache {

    private static final String ABTEST_PREFERENCE = "abTest_preference";
    private final SharedPreferences sharedPreferences;
    private CacheModel preferenceCacheModel;
    private String key = "abtestcachedobj";
    private AppInfraInterface appInfraInterface;
    private Gson gson;

    AbTestingLocalCache(Context context, Gson gson) {
        this.gson = gson;
        sharedPreferences = context.getSharedPreferences(ABTEST_PREFERENCE,
                Context.MODE_PRIVATE);
        this.preferenceCacheModel = gson.fromJson(fetchFromDisk(), CacheModel.class);
        if (preferenceCacheModel == null)
            preferenceCacheModel = new CacheModel();
    }

    private void saveToDisk(String cacheModel) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, cacheModel);
        editor.apply();
        appInfraInterface.getLogging().log(LoggingInterface.LogLevel.DEBUG, AppInfraLogEventID.AI_ABTEST_CLIENT,
                "save Cache to Preference " + cacheModel);
    }

    String fetchFromDisk() {
        return sharedPreferences.getString(key, null);
    }

    void initAppInfra(AppInfraInterface appInfraInterface) {
        this.appInfraInterface = appInfraInterface;
    }

    void removeFromDisk(String testName) {
        final CacheModel model = getCacheFromPreference();
        if (model != null) {
            final Map<String, CacheModel.ValueModel> cModel = model.getTestValues();
            if (cModel != null && cModel.containsKey(testName)) {
                cModel.remove(testName);
                saveToDisk(gson.toJson(model));
            }
        }
        appInfraInterface.getLogging().log(LoggingInterface.LogLevel.DEBUG, AppInfraLogEventID.AI_ABTEST_CLIENT,
                "remove cache for TestName");
    }

    /**
     * method to fetch model from the shared preference data.
     *
     * @return cachemodel object
     */
    CacheModel getCacheFromPreference() {
        return preferenceCacheModel;
    }


    void saveCacheToDisk() {
        saveToDisk(gson.toJson(preferenceCacheModel));
        appInfraInterface.getLogging().log(LoggingInterface.LogLevel.DEBUG, AppInfraLogEventID.AI_ABTEST_CLIENT,
                "updated Cache to Preference " + preferenceCacheModel);
    }

    void updatePreferenceCacheModel(Map<String, CacheModel.ValueModel> inMemoryCache) {
        this.preferenceCacheModel.setTestValues(inMemoryCache);
    }

    CacheModel getPreferenceCacheModel() {
        return preferenceCacheModel;
    }
}
