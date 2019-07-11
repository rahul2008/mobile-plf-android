package com.philips.cdp.di.ecs.prx.prxclient;

import android.content.Context;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;

/**
 * PRX Dependencies Class.
 * @since 1.0.0
 */

public class PRXDependencies {

    private final AppInfraInterface mAppInfraInterface;
    private final Context mContext;
    private String mParentTLA;
    public LoggingInterface mAppInfraLogging;

    /**
     * PRXDependencies constructor.
     * @param context Context
     * @param appInfra App Infra Interface
     * @param parentTLA Parent Three Letter Acronym
     * @since 1.0.0
     */
    public PRXDependencies(Context context, AppInfraInterface appInfra , String parentTLA) {
        this.mAppInfraInterface = appInfra;
        this.mContext = context;
        this.mParentTLA = parentTLA;
    }

    /**
     * Getter for AppInfra Interface.
     * @return Returns the App Infra interface
     * @since 1.0.0
     */
    public AppInfraInterface getAppInfra() {
        return this.mAppInfraInterface;
    }

    /**
     * Get the context.
     * @return Returns the context
     * @since 1.0.0
     */
    public Context getContext() {
        return this.mContext;
    }

    /**
     * Getter for parent TLA.
     * @return Returns the parent TLA
     * @since  2.2.0
     */
    public String getParentTLA() {
        return mParentTLA;
    }
}
