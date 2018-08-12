package com.philips.platform.aildemo.abtesting;

import com.philips.platform.appinfra.abtestclient.ABTestClientInterface;

import java.util.Map;

public interface FetchDataHandler {

    void fetchData(Map<String, CacheModel.ValueModel> data);
    void updateCacheStatus(ABTestClientInterface.CACHESTATUSVALUES cachestatusvalues);
}
