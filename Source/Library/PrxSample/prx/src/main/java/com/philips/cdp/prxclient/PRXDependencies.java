package com.philips.cdp.prxclient;

import android.content.Context;

import com.philips.platform.appinfra.AppInfraInterface;

/**
 * Created by 310243577 on 12/16/2016.
 */

public class PRXDependencies {

    private AppInfraInterface mAppInfraInterface;
    private Context mContext;

    public PRXDependencies(Context context, AppInfraInterface appInfra) {
        this.mAppInfraInterface = appInfra;
        this.mContext = context;
    }

    public AppInfraInterface getAppInfra() {
        return this.mAppInfraInterface;
    }

    public Context getContext() {
        return this.mContext;
    }
}
