package com.philips.platform.appframework.abtesting;

import android.support.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.AppInfraLogEventID;
import com.philips.platform.appinfra.abtestclient.ABTestClientInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class FireBaseWrapper implements OnCompleteListener<Void>, OnFailureListener {

    private FirebaseRemoteConfig remoteConfig;
    private FetchDataHandler fetchDataHandler;
    private ABTestClientInterface.OnRefreshListener onRefreshListener;
    private int cacheExpirationTime = 43200; // 12hours by default
    private AppInfraInterface appInfraInterface;

    public void initFireBase() {
        remoteConfig = FirebaseRemoteConfig.getInstance();
    }

    public void fetchDataFromFireBase(final FetchDataHandler fetchDataHandler, ABTestClientInterface.OnRefreshListener onRefreshListener) {
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
            cacheExpirationTime = 0;
        } else
            cacheExpirationTime = 43200;
        
        appInfraInterface.getLogging().log(LoggingInterface.LogLevel.DEBUG, AppInfraLogEventID.AI_ABTEST_CLIENT,"firebase developer mode set to "+state);
    }

    @Override
    public void onComplete(@NonNull Task<Void> task) {
        if (task.isSuccessful()) {
            appInfraInterface.getLogging().log(LoggingInterface.LogLevel.DEBUG, AppInfraLogEventID.AI_ABTEST_CLIENT, "Fetch Succeeded");
            // TODO: Deepthi, why do we getinstance again and reassign to remoteconfig since its already done during initialization
            final FirebaseRemoteConfig remoteConfig = FirebaseRemoteConfig.getInstance();
            remoteConfig.activateFetched();
            fetchDataHandler.fetchData(fetchExperiences());
            fetchDataHandler.updateCacheStatus(ABTestClientInterface.CACHESTATUSVALUES.EXPERIENCE_UPDATED);
            onRefreshListener.onSuccess();
        }
    }

    @Override
    public void onFailure(@NonNull Exception e) {
        appInfraInterface.getLogging().log(LoggingInterface.LogLevel.DEBUG, AppInfraLogEventID.AI_ABTEST_CLIENT, "Fetch Failed");
        onRefreshListener.onError(ABTestClientInterface.OnRefreshListener.ERRORVALUES.SERVER_ERROR);
        fetchDataHandler.updateCacheStatus(ABTestClientInterface.CACHESTATUSVALUES.EXPERIENCE_NOT_UPDATED);
    }

    private Map<String, CacheModel.ValueModel> fetchExperiences() {
        final FirebaseRemoteConfig remoteConfig = FirebaseRemoteConfig.getInstance();
        Set<String> experiments = remoteConfig.getKeysByPrefix("");
        Map<String, CacheModel.ValueModel> map = new HashMap<>();
        appInfraInterface.getLogging().log(LoggingInterface.LogLevel.DEBUG, "experiment size ", experiments.size() + "");
        for (String key : experiments) {
            CacheModel.ValueModel valueModel = new CacheModel.ValueModel();
            valueModel.setTestValue(remoteConfig.getString(key));
            valueModel.setAppVersion(Integer.parseInt(appInfraInterface.getAppIdentity().getAppVersion()));
            valueModel.setUpdateType(ABTestClientInterface.UPDATETYPES.EVERY_APP_START.name());
            map.put(key, valueModel);
            appInfraInterface.getLogging().log(LoggingInterface.LogLevel.DEBUG, "experiments value", valueModel.getTestValue());
        }
        return map;
    }

    void initAppInfra(AppInfraInterface appInfraInterface) {
        this.appInfraInterface = appInfraInterface;
    }
}
