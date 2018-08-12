package com.philips.platform.aildemo.abtesting;

import android.util.Log;

import com.philips.platform.appinfra.abtestclient.ABTestClientInterface;
import com.philips.platform.appinfra.demo.BuildConfig;

import java.util.Map;

public class AbTestingUtil {

    static void syncInMemoryCache(Map<String, CacheModel.ValueModel> inMemoryCache, Map<String, CacheModel.ValueModel> cacheModel) {
        for (Map.Entry<String, CacheModel.ValueModel> entry : cacheModel.entrySet()) {
            Log.d("Item : ", entry.getKey() + " Count : " + entry.getValue().getTestValue());
            if (inMemoryCache.containsKey(entry.getKey())) {
                //TODO - have to remove below comments after aligning with sahu
                // variable is restart type or app-update
                //if restart type , update that to firebase value
                // if app-update type, 2 conditions
                // is app is updating - we need to update the inmemory cache
                // if app is restart - we don't need to update
                CacheModel.ValueModel valueModel = inMemoryCache.get(entry.getKey());
                if (valueModel.getUpdateType() != null && valueModel.getUpdateType().equals(ABTestClientInterface.UPDATETYPES.EVERY_APP_START.name())) {
                    valueModel.setTestValue(entry.getValue().getTestValue());
                } else if (valueModel.getUpdateType() != null && valueModel.getUpdateType().equals(ABTestClientInterface.UPDATETYPES.ONLY_AT_APP_UPDATE.name())) {
                    if (Double.parseDouble(getAppVersion()) > Double.parseDouble(valueModel.getAppVersion())) {
                        valueModel.setTestValue(entry.getValue().getTestValue());
                    }
                }
            } else
                inMemoryCache.put(entry.getKey(), entry.getValue());
        }
    }

    static String getAppVersion() {
        return BuildConfig.VERSION_NAME;
    }

}
