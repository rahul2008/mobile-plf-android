package com.philips.platform.aildemo.abtesting;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.philips.platform.appinfra.abtestclient.ABTestClientInterface;
import com.philips.platform.appinfra.demo.R;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.philips.platform.aildemo.abtesting.AbTestingUtil.getAppVersion;


public class FireBaseWrapper implements OnCompleteListener<Void>, OnFailureListener {

    private static final String TAG = FireBaseWrapper.class.getSimpleName();
    private FirebaseRemoteConfig remoteConfig;
    private FetchDataHandler fetchDataHandler;
    private ABTestClientInterface.OnRefreshListener onRefreshListener;

    public void initFireBase() {
        remoteConfig = FirebaseRemoteConfig.getInstance();
        // TODO (Deepthi) - need to discuss on firebase defaults
//        remoteConfig.setDefaults(R.xml.firebase_defaults);
    }

    public void fetchDataFromFireBase(final FetchDataHandler fetchDataHandler, ABTestClientInterface.OnRefreshListener onRefreshListener) {
        this.onRefreshListener = onRefreshListener;
        this.fetchDataHandler = fetchDataHandler;
        remoteConfig.fetch().addOnCompleteListener(this).addOnFailureListener(this);
    }

    void enableDeveloperMode(boolean state) {
        FirebaseRemoteConfigSettings config = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(state)
                .build();
        remoteConfig.setConfigSettings(config);
    }

    @Override
    public void onComplete(@NonNull Task<Void> task) {
        if (task.isSuccessful()) {
            Log.d(TAG, "Fetch Succeeded");
            final FirebaseRemoteConfig remoteConfig = FirebaseRemoteConfig.getInstance();
            remoteConfig.activateFetched();
            fetchDataHandler.fetchData(fetchExperiences());
            fetchDataHandler.updateCacheStatus(ABTestClientInterface.CACHESTATUSVALUES.EXPERIENCE_UPDATED);
            onRefreshListener.onSuccess();
        }
    }

    @Override
    public void onFailure(@NonNull Exception e) {
        Log.d(TAG, "Fetch Failed");
        onRefreshListener.onError(ABTestClientInterface.OnRefreshListener.ERRORVALUES.NO_NETWORK, e.getMessage());
        fetchDataHandler.updateCacheStatus(ABTestClientInterface.CACHESTATUSVALUES.EXPERIENCE_NOT_UPDATED);
    }

    private Map<String, CacheModel.ValueModel> fetchExperiences() {
        final FirebaseRemoteConfig remoteConfig = FirebaseRemoteConfig.getInstance();
        Set<String> experiments = remoteConfig.getKeysByPrefix("");
        Map<String, CacheModel.ValueModel> map = new HashMap<>();
        Log.d("experiment size ", experiments.size() + "");
        for (String key : experiments) {
            CacheModel.ValueModel valueModel = new CacheModel.ValueModel();
            valueModel.setTestValue(remoteConfig.getString(key));
            valueModel.setAppVersion(getAppVersion());
            valueModel.setUpdateType(ABTestClientInterface.UPDATETYPES.EVERY_APP_START.name());
            map.put(key, valueModel);
            Log.d("experiments value", valueModel.getTestValue());
        }
        return map;
    }
}
