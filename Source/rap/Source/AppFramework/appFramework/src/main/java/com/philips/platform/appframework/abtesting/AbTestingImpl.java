package com.philips.platform.appframework.abtesting;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.AppInfraLogEventID;
import com.philips.platform.appinfra.BuildConfig;
import com.philips.platform.appinfra.abtestclient.ABTestClientInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;

import java.util.HashMap;
import java.util.Map;

public class AbTestingImpl implements ABTestClientInterface {

    private FireBaseWrapper fireBaseWrapper;
    private AbTestingLocalCache abTestingLocalCache;
    private Map<String, CacheModel.ValueModel> inMemoryCache;
    private AppInfraInterface appInfraInterface;
    private ABTestClientInterface.CACHESTATUSVALUES cachestatusvalues = ABTestClientInterface.CACHESTATUSVALUES.EXPERIENCE_NOT_UPDATED;

    /**
     * To be invoked first before calling any api of this class
     * @param appInfraInterface - pass appInfra reference
     */
    public void initAbTesting(AppInfraInterface appInfraInterface) {
        this.appInfraInterface = appInfraInterface;
        abTestingLocalCache = new AbTestingLocalCache(appInfraInterface.getAppInfraContext(), new Gson());
        fireBaseWrapper = new FireBaseWrapper();
        fireBaseWrapper.initFireBase();
        abTestingLocalCache.initAppInfra(appInfraInterface);
        fireBaseWrapper.initAppInfra(appInfraInterface);
        inMemoryCache = new HashMap<>();
        if (abTestingLocalCache.getCacheFromPreference().getTestValues() != null)
            syncInMemoryCache(inMemoryCache, abTestingLocalCache.getCacheFromPreference().getTestValues());
        appInfraInterface.getLogging().log(LoggingInterface.LogLevel.DEBUG, AppInfraLogEventID.AI_ABTEST_CLIENT, " in-memory cache size " + inMemoryCache.size() + "");
    }

    @Override
    public CACHESTATUSVALUES getCacheStatus() {
        return cachestatusvalues;
    }

    @Override
    public String getTestValue(@NonNull String requestNameKey, @NonNull String defaultValue, UPDATETYPES updateType) {
        if(TextUtils.isEmpty(requestNameKey))
            return null;
        //fetching data from in-memory cache
        CacheModel.ValueModel valueModel = inMemoryCache.get(requestNameKey);
        String testValue;
        if (valueModel == null || valueModel.getTestValue() == null) {
            testValue = defaultValue;
        } else
            testValue = valueModel.getTestValue();

        updateCachesForTestName(requestNameKey, testValue, updateType);
        return testValue;
    }

    @Override
    public void updateCache(OnRefreshListener onRefreshListener) {
        if(!appInfraInterface.getRestClient().isInternetReachable()) {
            onRefreshListener.onError(ABTestClientInterface.OnRefreshListener.ERRORVALUES.NO_NETWORK);
            return;
        }
        fireBaseWrapper.fetchDataFromFireBase(new FetchDataHandler() {
            @Override
            public void fetchData(Map<String, CacheModel.ValueModel> data) {
                syncInMemoryCache(inMemoryCache, data);
            }

            @Override
            public void updateCacheStatus(ABTestClientInterface.CACHESTATUSVALUES cachestatusvalues) {
                AbTestingImpl.this.cachestatusvalues = cachestatusvalues;
            }
        }, onRefreshListener);
    }

    @Override
    public void enableDeveloperMode(boolean state) {
        fireBaseWrapper.enableDeveloperMode(state);
    }

    private void updateCachesForTestName(String requestNameKey, String testValue, ABTestClientInterface.UPDATETYPES updateType) {
        if (inMemoryCache.containsKey(requestNameKey)) {
            final CacheModel.ValueModel val = inMemoryCache.get(requestNameKey);
            if (val.getTestValue() == null || !updateType.name().equalsIgnoreCase(ABTestClientInterface.UPDATETYPES.EVERY_APP_START.name())) {
                updateInMemoryCache(requestNameKey, testValue, updateType.name());
                abTestingLocalCache.updatePreferenceCacheModel(inMemoryCache);
            }
            //else value is already there in cache ignoring the new value
        }
        if (updateType.equals(ABTestClientInterface.UPDATETYPES.EVERY_APP_START)) {
            abTestingLocalCache.removeFromDisk(requestNameKey);
        } else if (updateType.name().equals
                (ABTestClientInterface.UPDATETYPES.ONLY_AT_APP_UPDATE.name())) {
            abTestingLocalCache.saveCacheToDisk();
        }
        appInfraInterface.getLogging().log(LoggingInterface.LogLevel.INFO, AppInfraLogEventID.AI_ABTEST_CLIENT,
                "testValue " + testValue);
    }

    private void updateInMemoryCache(String requestNameKey, String testValue, String updateType) {
        final CacheModel.ValueModel valueModel = new CacheModel.ValueModel();
        valueModel.setTestValue(testValue);
        valueModel.setUpdateType(updateType);
        valueModel.setAppVersion(getAppVersion());
        inMemoryCache.put(requestNameKey, valueModel);
    }

    private void syncInMemoryCache(Map<String, CacheModel.ValueModel> inMemoryCache, Map<String, CacheModel.ValueModel> cacheModel) {
        for (Map.Entry<String, CacheModel.ValueModel> entry : cacheModel.entrySet()) {
            appInfraInterface.getLogging().log(LoggingInterface.LogLevel.DEBUG, AppInfraLogEventID.AI_ABTEST_CLIENT, entry.getKey() + " Count : " + entry.getValue().getTestValue());
            if (inMemoryCache.containsKey(entry.getKey())) {
                CacheModel.ValueModel valueModel = inMemoryCache.get(entry.getKey());
                if (valueModel.getUpdateType() != null && valueModel.getUpdateType().equals(ABTestClientInterface.UPDATETYPES.EVERY_APP_START.name())) {
                    valueModel.setTestValue(entry.getValue().getTestValue());
                } else if (valueModel.getUpdateType() != null && valueModel.getUpdateType().equals(ABTestClientInterface.UPDATETYPES.ONLY_AT_APP_UPDATE.name())) {
                    if (isAppUpdated(valueModel)) {
                        valueModel.setTestValue(entry.getValue().getTestValue());
                    }
                }
            } else
                inMemoryCache.put(entry.getKey(), entry.getValue());
        }
    }

    private boolean isAppUpdated(CacheModel.ValueModel valueModel) {

        return getAppVersion() > valueModel.getAppVersion();
    }

    private int getAppVersion() {
        return BuildConfig.VERSION_CODE;
    }


}
