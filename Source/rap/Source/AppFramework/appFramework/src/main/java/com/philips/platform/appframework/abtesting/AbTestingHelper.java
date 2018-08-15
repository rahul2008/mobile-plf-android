package com.philips.platform.appframework.abtesting;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import com.google.gson.Gson;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.AppInfraLogEventID;
import com.philips.platform.appinfra.abtestclient.ABTestClientInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;

import java.util.HashMap;
import java.util.Map;

// TODO: Deepthi,  This class is not making sense
class AbTestingHelper {

    private FireBaseWrapper fireBaseWrapper;
    private AbTestingLocalCache abTestingLocalCache;
    private Map<String, CacheModel.ValueModel> inMemoryCache;
    private AppInfraInterface appInfraInterface;
    private CacheModel cacheModel;
    private ABTestClientInterface.CACHESTATUSVALUES cachestatusvalues = ABTestClientInterface.CACHESTATUSVALUES.EXPERIENCE_NOT_UPDATED;

    AbTestingHelper() {
        fireBaseWrapper = new FireBaseWrapper();
    }

    // TODO: Deepthi,This API is redundant, wrapper can be directly used.. need not have layers
    void initFireBase() {
        fireBaseWrapper.initFireBase();
    }

    void initAbTesting(AppInfraInterface appInfraInterface) {
        this.appInfraInterface = appInfraInterface;
        abTestingLocalCache = new AbTestingLocalCache(appInfraInterface.getAppInfraContext());
        abTestingLocalCache.initAppInfra(appInfraInterface);
        fireBaseWrapper.initAppInfra(appInfraInterface);
        inMemoryCache = new HashMap<>();
        this.cacheModel = getCacheFromPreference();
        if (cacheModel != null)
            syncInMemoryCache(inMemoryCache, cacheModel.getTestValues());
        else
            cacheModel = new CacheModel();

        appInfraInterface.getLogging().log(LoggingInterface.LogLevel.DEBUG, AppInfraLogEventID.AI_ABTEST_CLIENT, " in-memory cache size " + inMemoryCache.size() + "");
    }

    void updateCache(ABTestClientInterface.OnRefreshListener onRefreshListener) {
        if(!appInfraInterface.getRestClient().isInternetReachable()) {
            onRefreshListener.onError(ABTestClientInterface.OnRefreshListener.ERRORVALUES.NO_NETWORK,"No internet available");
            return;
        }
        fireBaseWrapper.fetchDataFromFireBase(new FetchDataHandler() {
            @Override
            public void fetchData(Map<String, CacheModel.ValueModel> data) {
                syncInMemoryCache(inMemoryCache, data);
            }

            @Override
            public void updateCacheStatus(ABTestClientInterface.CACHESTATUSVALUES cachestatusvalues) {
                AbTestingHelper.this.cachestatusvalues = cachestatusvalues;
            }
        }, onRefreshListener);
    }

    void enableDeveloperMode(boolean state) {
        fireBaseWrapper.enableDeveloperMode(state);
    }

    public ABTestClientInterface.CACHESTATUSVALUES getCacheStatus() {
        return cachestatusvalues;
    }

    String getTestValue(@NonNull String requestNameKey, String defaultValue, ABTestClientInterface.UPDATETYPES updateType) {

        if(TextUtils.isEmpty(requestNameKey))
            return null;

        //fetching data from in-memory cache
        CacheModel.ValueModel valueModel = inMemoryCache.get(requestNameKey);
        String testValue;
        if (valueModel == null || valueModel.getTestValue() == null) {
            testValue = defaultValue;
        } else
            testValue = valueModel.getTestValue();

        updateDiskCacheForTestName(requestNameKey, testValue, updateType);
        return testValue;
    }

    private void updateDiskCacheForTestName(String requestNameKey, String testValue, ABTestClientInterface.UPDATETYPES updateType) {
        if (inMemoryCache.containsKey(requestNameKey)) {
            final CacheModel.ValueModel val = inMemoryCache.get(requestNameKey);
            // TODO: Deepthi, need to relook on below logic
            if (val.getTestValue() == null || !updateType.name().equalsIgnoreCase(ABTestClientInterface.UPDATETYPES.EVERY_APP_START.name())) {
                cacheModel(requestNameKey, testValue, updateType.name());
            }
            //else value is already there in cache ignoring the new value
            // TODO: Deepthi, what if only the app update type has changed for an existing test
        }
        if (updateType.equals(ABTestClientInterface.UPDATETYPES.EVERY_APP_START)) {
            removeCacheForTestName(requestNameKey);
        } else if (updateType.name().equals
                (ABTestClientInterface.UPDATETYPES.ONLY_AT_APP_UPDATE.name())) {
            abTestingLocalCache.saveToDisk(getGson().toJson(cacheModel));
        }
        appInfraInterface.getLogging().log(LoggingInterface.LogLevel.INFO, AppInfraLogEventID.AI_ABTEST_CLIENT,
                "testValue " + testValue);
    }

    @NonNull
    private Gson getGson() {
        return new Gson();
    }

    private void removeCacheForTestName(String testName) {
        final CacheModel model = getCacheFromPreference();
        if (model != null) {
            final Map<String, CacheModel.ValueModel> cModel = model.getTestValues();
            if (cModel != null && cModel.containsKey(testName)) {
                cModel.remove(testName);
                abTestingLocalCache.saveToDisk(getGson().toJson(model));
            }
        }
        appInfraInterface.getLogging().log(LoggingInterface.LogLevel.DEBUG, AppInfraLogEventID.AI_ABTEST_CLIENT,
                "remove cache for TestName");
    }

    // TODO: Deepthi method name is confusing, could be createCreateCacheModel
    private void cacheModel(String requestNameKey, String testValue, String updateType) {
        final CacheModel.ValueModel valueModel = new CacheModel.ValueModel();
        valueModel.setTestValue(testValue);
        valueModel.setUpdateType(updateType);
        valueModel.setAppVersion(getAppVersion());
        inMemoryCache.put(requestNameKey, valueModel);
        this.cacheModel.setTestValues(inMemoryCache);
    }

    /**
     * method to fetch from the shared preference.
     *
     * @return cachemodel object
     */
    private CacheModel getCacheFromPreference() {
        final Gson gson = getGson();
        return gson.fromJson(abTestingLocalCache.fetchFromDisk(), CacheModel.class);
    }

    private void syncInMemoryCache(Map<String, CacheModel.ValueModel> inMemoryCache, Map<String, CacheModel.ValueModel> cacheModel) {
        for (Map.Entry<String, CacheModel.ValueModel> entry : cacheModel.entrySet()) {
            appInfraInterface.getLogging().log(LoggingInterface.LogLevel.DEBUG, AppInfraLogEventID.AI_ABTEST_CLIENT, entry.getKey() + " Count : " + entry.getValue().getTestValue());
            if (inMemoryCache.containsKey(entry.getKey())) {
                CacheModel.ValueModel valueModel = inMemoryCache.get(entry.getKey());
                if (valueModel.getUpdateType() != null && valueModel.getUpdateType().equals(ABTestClientInterface.UPDATETYPES.EVERY_APP_START.name())) {
                    valueModel.setTestValue(entry.getValue().getTestValue());
                } else if (valueModel.getUpdateType() != null && valueModel.getUpdateType().equals(ABTestClientInterface.UPDATETYPES.ONLY_AT_APP_UPDATE.name())) {
                    if (getAppVersion() > valueModel.getAppVersion()) {
                        valueModel.setTestValue(entry.getValue().getTestValue());
                    }
                }
            } else
                // TODO: Deepthi, where are we assigning app update value as app restart
                inMemoryCache.put(entry.getKey(), entry.getValue());
        }
    }

    int getAppVersion() {
        return Integer.parseInt(appInfraInterface.getAppIdentity().getAppVersion());
    }
}
