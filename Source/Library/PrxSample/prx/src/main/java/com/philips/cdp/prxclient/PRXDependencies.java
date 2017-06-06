package com.philips.cdp.prxclient;

import android.content.Context;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;

/**
 * Created by 310243577 on 12/16/2016.
 */

public class PRXDependencies {

    private AppInfraInterface mAppInfraInterface;
    private Context mContext;
    private String mParentTLA;
    public LoggingInterface mAppInfraLogging;

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
