package com.philips.platform.appframework.abtesting;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.AppInfraLogEventID;
import com.philips.platform.appinfra.abtestclient.ABTestClientInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;

import java.util.HashMap;
import java.util.Map;

import static com.philips.platform.appframework.abtesting.AbTestingUtil.getAppVersion;


public class AbTestingImpl implements ABTestClientInterface {

    private AbTestingLocalCache abTestingLocalCache;
    private Map<String, CacheModel.ValueModel> inMemoryCache;
    private CacheModel cacheModel;
    private CACHESTATUSVALUES cachestatusvalues = CACHESTATUSVALUES.EXPERIENCE_NOT_UPDATED;
    private AbTestingHelper abTestingHelper;
    private FireBaseWrapper fireBaseWrapper;
    private AppInfraInterface appInfraInterface;

    public AbTestingImpl(Context context) {
        abTestingHelper = new AbTestingHelper(context);
        fireBaseWrapper = new FireBaseWrapper();
        init(context);
    }

    private void init(Context context) {
        abTestingLocalCache = new AbTestingLocalCache(context);
        //TODO (Deepthi) - should init firebase either during app launch or after appInfra init
        fireBaseWrapper.initFireBase();
        inMemoryCache = new HashMap<>();
        this.cacheModel = getCacheFromPreference();
        if (cacheModel != null)
            AbTestingUtil.syncInMemoryCache(inMemoryCache, cacheModel.getTestValues());
        else
            cacheModel = new CacheModel();

        Log.d(AppInfraLogEventID.AI_ABTEST_CLIENT, " disk cache size "+ cacheModel.getTestValues().size()+"");
        Log.d(AppInfraLogEventID.AI_ABTEST_CLIENT, " in-memory cache size "+ inMemoryCache.size()+"");
    }

    @Override
    public void updateCache(OnRefreshListener onRefreshListener) {
        fireBaseWrapper.fetchDataFromFireBase(new FetchDataHandler() {
            @Override
            public void fetchData(Map<String, CacheModel.ValueModel> data) {
                AbTestingUtil.syncInMemoryCache(inMemoryCache, data);
            }

            @Override
            public void updateCacheStatus(CACHESTATUSVALUES cachestatusvalues) {
                AbTestingImpl.this.cachestatusvalues = cachestatusvalues;
            }
        }, onRefreshListener);
    }

    @Override
    public void enableDeveloperMode(boolean state) {
        fireBaseWrapper.enableDeveloperMode(state);
    }

    @Override
    public CACHESTATUSVALUES getCacheStatus() {
        return cachestatusvalues;
    }

    //TODO (Deepthi)- can we remove params
    @Override
    public String getTestValue(@NonNull String requestNameKey, String defaultValue, UPDATETYPES updateType, Map<String, Object> parameters) {
        //fetching data from in-memory cache
        //TODO - need to discuss for empty and null key
        CacheModel.ValueModel valueModel = inMemoryCache.get(requestNameKey);
        String testValue;
        if (valueModel == null || valueModel.getTestValue() == null) {
            testValue = defaultValue;
        } else
            testValue = valueModel.getTestValue();

        updateDiskCacheForTestName(requestNameKey, testValue, updateType);
        return testValue;
    }

    private void updateDiskCacheForTestName(String requestNameKey, String testValue, UPDATETYPES updateType) {
        if (inMemoryCache.containsKey(requestNameKey)) {
            final CacheModel.ValueModel val = inMemoryCache.get(requestNameKey);
            if (val.getTestValue() == null || !updateType.name().equalsIgnoreCase(UPDATETYPES.EVERY_APP_START.name())) {
                cacheModel(requestNameKey, testValue, updateType.name());
            }
            //value is already there in cache ignoring the new value
        }
        if (updateType.equals(UPDATETYPES.EVERY_APP_START)) {
            removeCacheForTestName(requestNameKey);
        } else if (updateType.name().equals
                (UPDATETYPES.ONLY_AT_APP_UPDATE.name())) {
            saveCacheToPreference(cacheModel);
        }
        appInfraInterface.getLogging().log(LoggingInterface.LogLevel.INFO, AppInfraLogEventID.AI_ABTEST_CLIENT,
                "testValue " + testValue);
    }

    private void removeCacheForTestName(String testName) {
        final CacheModel model = getCacheFromPreference();
        if (model != null) {
            final Map<String, CacheModel.ValueModel> cModel = model.getTestValues();
            if (cModel != null && cModel.containsKey(testName)) {
                cModel.remove(testName);
                saveCacheToPreference(model);
            }
        }
        appInfraInterface.getLogging().log(LoggingInterface.LogLevel.DEBUG, AppInfraLogEventID.AI_ABTEST_CLIENT,
                "remove cache for TestName");
    }

    private void cacheModel(String requestNameKey, String testValue, String updateType) {
        final CacheModel.ValueModel valueModel = new CacheModel.ValueModel();
        valueModel.setTestValue(testValue);
        valueModel.setUpdateType(updateType);
        valueModel.setAppVersion(getAppVersion());
        inMemoryCache.put(requestNameKey, valueModel);
        this.cacheModel.setTestValues(inMemoryCache);
    }

    /**
     * method to save cachemodel object in preference.
     *
     * @param model cachemodel object
     */
    private void saveCacheToPreference(final CacheModel model) {
        final Gson gson = new Gson();
        String cacheToPreference = gson.toJson(model);
        appInfraInterface.getLogging().log(LoggingInterface.LogLevel.DEBUG, AppInfraLogEventID.AI_ABTEST_CLIENT,
                "save Cache to Preference " + cacheToPreference);
        abTestingLocalCache.saveToDisk(cacheToPreference);

    }

    /**
     * method to fetch from the shared preference.
     *
     * @return cachemodel object
     */
    private CacheModel getCacheFromPreference() {
        final Gson gson = new Gson();
        return gson.fromJson(abTestingLocalCache.fetchFromDisk(), CacheModel.class);
    }

    public void setAppInfraInterface(AppInfraInterface appInfraInterface) {
        this.appInfraInterface = appInfraInterface;
    }
}
