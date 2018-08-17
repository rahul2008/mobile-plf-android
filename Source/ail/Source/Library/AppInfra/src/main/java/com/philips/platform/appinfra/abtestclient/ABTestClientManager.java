/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.abtestclient;

import android.support.annotation.NonNull;

/**
 * The ABTest Client Manager
 */

public class ABTestClientManager implements ABTestClientInterface {


    @Override
    public CACHESTATUSVALUES getCacheStatus() {
        return null;
    }

    @Override
    public String getTestValue(@NonNull String requestNameKey, @NonNull String defaultValue, UPDATETYPES updateType) {
        return defaultValue;
    }

    @Override
    public void updateCache(OnRefreshListener listener) {

    }

    @Override
    public void enableDeveloperMode(boolean state) {

    }
}
