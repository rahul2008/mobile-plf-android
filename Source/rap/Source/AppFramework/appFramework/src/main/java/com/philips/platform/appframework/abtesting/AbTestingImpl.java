package com.philips.platform.appframework.abtesting;

import android.support.annotation.NonNull;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.abtestclient.ABTestClientInterface;

public class AbTestingImpl implements ABTestClientInterface {

    private AbTestingHelper abTestingHelper;

    public AbTestingImpl() {
        abTestingHelper = new AbTestingHelper();
        abTestingHelper.initFireBase();
    }

    public void initAbTesting(AppInfraInterface appInfraInterface) {
        abTestingHelper.initAbTesting(appInfraInterface);
    }

    @Override
    public CACHESTATUSVALUES getCacheStatus() {
        return abTestingHelper.getCacheStatus();
    }

    @Override
    public String getTestValue(@NonNull String requestNameKey, String defaultValue, UPDATETYPES updateType) {
        return abTestingHelper.getTestValue(requestNameKey, defaultValue, updateType);
    }

    @Override
    public void updateCache(OnRefreshListener onRefreshListener) {
        abTestingHelper.updateCache(onRefreshListener);
    }

    @Override
    public void enableDeveloperMode(boolean state) {
        abTestingHelper.enableDeveloperMode(state);
    }


}
