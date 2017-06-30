package com.philips.cdp.prxclient;

import android.content.Context;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;

/**
 * PRX Dependencies Class.
 */

public class PRXDependencies {

    private final AppInfraInterface mAppInfraInterface;
    private final Context mContext;
    private String mParentTLA;
    public LoggingInterface mAppInfraLogging;

    @Deprecated
    public PRXDependencies(Context context, AppInfraInterface appInfra) {
        this.mAppInfraInterface = appInfra;
        this.mContext = context;
    }

    public PRXDependencies(Context context, AppInfraInterface appInfra , String parentTLA) {
        this.mAppInfraInterface = appInfra;
        this.mContext = context;
        this.mParentTLA = parentTLA;
    }

    public AppInfraInterface getAppInfra() {
        return this.mAppInfraInterface;
    }

    public Context getContext() {
        return this.mContext;
    }

    public String getParentTLA() {
        return mParentTLA;
    }
}
