package com.philips.platform.appframework.abtesting;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.AppInfraLogEventID;
import com.philips.platform.appinfra.abtestclient.ABTestClientInterface;
import com.philips.platform.appinfra.abtestclient.CacheModel;
import com.philips.platform.appinfra.logging.LoggingInterface;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

class FireBaseWrapper implements OnCompleteListener<Void>, OnFailureListener {

    private FirebaseRemoteConfig remoteConfig;
    private AbTestingImpl.FetchDataHandler fetchDataHandler;
    private ABTestClientInterface.OnRefreshListener onRefreshListener;
    private final int defaultCacheExpirationTime = 43200;
    private int cacheExpirationTime = 43200; // 12hours by default
    private AppInfraInterface appInfraInterface;

    FireBaseWrapper(FirebaseRemoteConfig remoteConfig) {
        this.remoteConfig = remoteConfig;
    }

    void fetchDataFromFireBase(final AbTestingImpl.FetchDataHandler fetchDataHandler, ABTestClientInterface.OnRefreshListener onRefreshListener) {
        this.onRefreshListener = onRefreshListener;
        this.fetchDataHandler = fetchDataHandler;
        remoteConfig.fetch(cacheExpirationTime).addOnCompleteListener(this).addOnFailureListener(this);
    }

    void enableDeveloperMode(boolean state) {
        FirebaseRemoteConfigSettings config = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(state)
                .build();
        remoteConfig.setConfigSettings(config);
        if (state) {
            cacheExpirationTime = 0; // have made cache expiration time to zero seconds if enabled developer mode
        } else {
            cacheExpirationTime = defaultCacheExpirationTime;
        }
        
        appInfraInterface.getLogging().log(LoggingInterface.LogLevel.DEBUG, AppInfraLogEventID.AI_ABTEST_CLIENT,"firebase developer mode set to "+state);
    }

    @Override
    public void onComplete(@NonNull Task<Void> task) {
        if (task.isSuccessful()) {
            appInfraInterface.getLogging().log(LoggingInterface.LogLevel.DEBUG, AppInfraLogEventID.AI_ABTEST_CLIENT, "Fetch Succeeded");
            remoteConfig.activateFetched();
            fetchDataHandler.fetchData(fetchExperiences());
            fetchDataHandler.updateCacheStatus(ABTestClientInterface.CACHESTATUS.EXPERIENCE_UPDATED);
            onRefreshListener.onSuccess();
        }
    }

    @Override
    public void onFailure(@NonNull Exception e) {
        appInfraInterface.getLogging().log(LoggingInterface.LogLevel.DEBUG, AppInfraLogEventID.AI_ABTEST_CLIENT, "Fetch Failed");
        onRefreshListener.onError(ABTestClientInterface.OnRefreshListener.ERRORVALUE.SERVER_ERROR);
        fetchDataHandler.updateCacheStatus(ABTestClientInterface.CACHESTATUS.EXPERIENCE_NOT_UPDATED);
    }

    private Map<String, CacheModel.ValueModel> fetchExperiences() {
        Set<String> experiments = remoteConfig.getKeysByPrefix("");
        Map<String, CacheModel.ValueModel> map = new HashMap<>();
        appInfraInterface.getLogging().log(LoggingInterface.LogLevel.DEBUG, "experiment size ", experiments.size() + "");
        for (String key : experiments) {
            CacheModel.ValueModel valueModel = new CacheModel.ValueModel();
            valueModel.setTestValue(remoteConfig.getString(key));
            valueModel.setAppVersion(appInfraInterface.getAppIdentity().getAppVersion());
            valueModel.setUpdateType(ABTestClientInterface.UPDATETYPE.APP_RESTART.name());
            map.put(key, valueModel);
            appInfraInterface.getLogging().log(LoggingInterface.LogLevel.DEBUG, "experiments value", valueModel.getTestValue());
        }
        return map;
    }

    void initAppInfra(AppInfraInterface appInfraInterface) {
        this.appInfraInterface = appInfraInterface;
    }

    int getCacheExpirationTime() {
        return cacheExpirationTime;
    }
}
