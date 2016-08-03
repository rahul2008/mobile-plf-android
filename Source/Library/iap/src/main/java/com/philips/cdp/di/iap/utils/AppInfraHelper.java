/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.utils;

import android.content.Context;

import com.philips.cdp.di.iap.analytics.IAPAnalyticsConstant;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.BuildConfig;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;

public class AppInfraHelper {

    private static volatile AppInfraHelper mAnalyticsHelper = null;
    private AppTaggingInterface mAppTaggingInterface;
    private LoggingInterface mIapLoggingInterface;
    public AppInfraInterface mAppInfraInterface;

    private AppInfraHelper() {
    }

    public static AppInfraHelper getInstance() {
        if (mAnalyticsHelper == null) {
            synchronized (AppInfraHelper.class) {
                if (mAnalyticsHelper == null) {
                    mAnalyticsHelper = new AppInfraHelper();
                }
            }
        }
        return mAnalyticsHelper;
    }

    public AppTaggingInterface getIapTaggingInterface() {
        return mAppTaggingInterface;
    }

    public LoggingInterface getIapLoggingInterfaceInterface() {
        return mIapLoggingInterface;
    }

    public synchronized void initializeAppInfra(final Context context) {
        mAppInfraInterface = new AppInfra.Builder().build(context);

        //Tagging
        mAppTaggingInterface = mAppInfraInterface.getTagging().
                createInstanceForComponent(IAPAnalyticsConstant.COMPONENT_NAME, BuildConfig.VERSION_NAME);
        // Moved to vertical demo app to set previous page
        //mAppTaggingInterface.setPreviousPage("IAPDemo");

        //Logging
        mIapLoggingInterface = mAppInfraInterface.getLogging().createInstanceForComponent(IAPAnalyticsConstant.COMPONENT_NAME, BuildConfig.VERSION_NAME);
        IAPLog.enableLogging(true);
    }
}
