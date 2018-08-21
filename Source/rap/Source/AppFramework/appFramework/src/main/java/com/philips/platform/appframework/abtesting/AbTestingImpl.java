package com.philips.platform.appframework.abtesting;

import android.support.annotation.NonNull;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.gson.Gson;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.AppInfraLogEventID;
import com.philips.platform.appinfra.abtestclient.ABTestClientInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;

import java.util.HashMap;
import java.util.Map;

public class AbTestingImpl implements ABTestClientInterface {

    private FireBaseWrapper fireBaseWrapper;
    private AbTestingLocalCache abTestingLocalCache;
    private Map<String, CacheModel.ValueModel> inMemoryCache;
    private AppInfraInterface appInfraInterface;
    private CACHESTATUSVALUES cachestatusvalues = CACHESTATUSVALUES.EXPERIENCE_NOT_UPDATED;


    public void initFireBase() {
        fireBaseWrapper = getFireBaseWrapper();
    }

    @NonNull
    FireBaseWrapper getFireBaseWrapper() {
        return new FireBaseWrapper(FirebaseRemoteConfig.getInstance());
    }

    /**
     * To be invoked first before calling any api of this class
     * @param appInfraInterface - pass appInfra reference
     */
    public void initAbTesting(AppInfraInterface appInfraInterface) {
        this.appInfraInterface = appInfraInterface;
        abTestingLocalCache = getAbTestingLocalCache(appInfraInterface);
        abTestingLocalCache.initAppInfra(appInfraInterface);
        fireBaseWrapper.initAppInfra(appInfraInterface);
        inMemoryCache = new HashMap<>();
        if (abTestingLocalCache.getCacheFromPreference().getTestValues() != null)
            syncInMemoryCache(inMemoryCache, abTestingLocalCache.getCacheFromPreference().getTestValues());
        appInfraInterface.getLogging().log(LoggingInterface.LogLevel.DEBUG, AppInfraLogEventID.AI_ABTEST_CLIENT, " in-memory cache size " + inMemoryCache.size() + "");
    }

    @NonNull
    AbTestingLocalCache getAbTestingLocalCache(AppInfraInterface appInfraInterface) {
        return new AbTestingLocalCache(appInfraInterface.getAppInfraContext(), new Gson());
    }

    @Override
    public CACHESTATUSVALUES getCacheStatus() {
        return cachestatusvalues;
    }

    @Override
    public String getTestValue(@NonNull String requestNameKey, @NonNull String defaultValue, UPDATETYPES updateType) {
        if (requestNameKey.isEmpty())
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
            onRefreshListener.onError(OnRefreshListener.ERRORVALUES.NO_NETWORK);
            return;
        }
        fireBaseWrapper.fetchDataFromFireBase(getFetchDataHandler(), onRefreshListener);
    }

    @NonNull
    FetchDataHandler getFetchDataHandler() {
        return new FetchDataHandler() {
            @Override
            public void fetchData(Map<String, CacheModel.ValueModel> data) {
                syncInMemoryCache(inMemoryCache, data);
            }

            @Override
            public void updateCacheStatus(CACHESTATUSVALUES cachestatusvalues) {
                AbTestingImpl.this.cachestatusvalues = cachestatusvalues;
            }
        };
    }

    @Override
    public void enableDeveloperMode(boolean state) {
        fireBaseWrapper.enableDeveloperMode(state);
    }

    private void updateCachesForTestName(String requestNameKey, String testValue, UPDATETYPES updateType) {
        if (inMemoryCache.containsKey(requestNameKey)) {
            final CacheModel.ValueModel val = inMemoryCache.get(requestNameKey);
            if (val.getTestValue() == null || !updateType.name().equalsIgnoreCase(UPDATETYPES.EVERY_APP_START.name())) {
                updateInMemoryCache(requestNameKey, testValue, updateType.name());
                abTestingLocalCache.updatePreferenceCacheModel(inMemoryCache);
            }
            //else value is already there in cache ignoring the new value
        }
        if (updateType.equals(UPDATETYPES.EVERY_APP_START)) {
            abTestingLocalCache.removeFromDisk(requestNameKey);
        } else if (updateType.name().equals
                (UPDATETYPES.ONLY_AT_APP_UPDATE.name())) {
            abTestingLocalCache.saveCacheToDisk();
        }
        appInfraInterface.getLogging().log(LoggingInterface.LogLevel.INFO, AppInfraLogEventID.AI_ABTEST_CLIENT,
                "testValue " + testValue);
    }

    private void updateInMemoryCache(String requestNameKey, String testValue, String updateType) {
        final CacheModel.ValueModel valueModel = new CacheModel.ValueModel();
        valueModel.setTestValue(testValue);
        valueModel.setUpdateType(updateType);
        valueModel.setAppVersion(appInfraInterface.getAppIdentity().getAppVersion());
        inMemoryCache.put(requestNameKey, valueModel);
    }

    private void syncInMemoryCache(Map<String, CacheModel.ValueModel> inMemoryCache, Map<String, CacheModel.ValueModel> cacheModel) {
        for (Map.Entry<String, CacheModel.ValueModel> entry : cacheModel.entrySet()) {
            appInfraInterface.getLogging().log(LoggingInterface.LogLevel.DEBUG, AppInfraLogEventID.AI_ABTEST_CLIENT, " abtest data "+entry.getKey() +" --- "+ entry.getValue().getTestValue());
            if (inMemoryCache.containsKey(entry.getKey())) {
                CacheModel.ValueModel valueModel = inMemoryCache.get(entry.getKey());
                if (valueModel.getUpdateType() != null && valueModel.getUpdateType().equals(UPDATETYPES.EVERY_APP_START.name())) {
                    valueModel.setTestValue(entry.getValue().getTestValue());
                } else if (valueModel.getUpdateType() != null && valueModel.getUpdateType().equals(UPDATETYPES.ONLY_AT_APP_UPDATE.name())) {
                    if (isAppUpdated(valueModel)) {
                        valueModel.setTestValue(entry.getValue().getTestValue());
                        valueModel.setAppVersion(entry.getValue().getAppVersion());
                    }
                }
            } else
                inMemoryCache.put(entry.getKey(), entry.getValue());
        }
    }

    private boolean isAppUpdated(CacheModel.ValueModel valueModel) {
        return valueModel.getAppVersion() == null || valueModel.getAppVersion().isEmpty() || !valueModel.getAppVersion().equalsIgnoreCase(appInfraInterface.getAppIdentity().getAppVersion());
    }

    Map<String, CacheModel.ValueModel> getInMemoryCache() {
        return inMemoryCache;
    }
}
