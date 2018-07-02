package com.philips.platform.appinfra.abtestclient;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import java.util.Map;

public class ABTestFireBaseImpl implements ABTestClientInterface {


    public ABTestFireBaseImpl() {
        FirebaseRemoteConfig remoteConfig = FirebaseRemoteConfig.getInstance();

        FirebaseRemoteConfigSettings config = new FirebaseRemoteConfigSettings.Builder()
                .build();
        remoteConfig.setConfigSettings(config);
        //TODO - need to discuss about setting default xml with values
    }

    @Override
    public CACHESTATUSVALUES getCacheStatus() {
        return null;
    }

    @Override
    public String getTestValue(String requestNameKey, String defaultValue, UPDATETYPES updateType, Map<String, Object> parameters) {
        return null;
    }

    @Override
    public void updateCache(OnRefreshListener listener) {

    }
}
