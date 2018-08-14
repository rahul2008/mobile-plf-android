package com.philips.platform.appframework.abtesting;

import android.util.Log;

import com.philips.platform.appinfra.AppInfraLogEventID;
import com.philips.platform.appinfra.abtestclient.ABTestClientInterface;
import com.philips.platform.appinfra.demo.BuildConfig;

import java.util.Map;

public class AbTestingUtil {

    static void syncInMemoryCache(Map<String, CacheModel.ValueModel> inMemoryCache, Map<String, CacheModel.ValueModel> cacheModel) {
        for (Map.Entry<String, CacheModel.ValueModel> entry : cacheModel.entrySet()) {
            Log.d(AppInfraLogEventID.AI_ABTEST_CLIENT, entry.getKey() + " Count : " + entry.getValue().getTestValue());
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
                inMemoryCache.put(entry.getKey(), entry.getValue());
        }
    }

    //TODO (Deepthi) - need to check can we take from AppUpdate
    static int getAppVersion() {
        return BuildConfig.VERSION_CODE;
    }

}
