/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.utils;

import android.content.Context;

import com.philips.cdp.di.iap.analytics.IAPAnalyticsConstant;
import com.philips.cl.di.apptagging.BuildConfig;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.AppInfraSingleton;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;

public class AppInfraHelper {

    private static volatile AppInfraHelper mAnalyticsHelper = null;
    private AppTaggingInterface mAppTaggingInterface;
    private LoggingInterface mIAPLoggingInterface;

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

    public AppTaggingInterface getAIAppTaggingInterface() {
        return mAppTaggingInterface;
    }

    public LoggingInterface getIAPLoggingInterfaceInterface() {
        return mIAPLoggingInterface;
    }

    public synchronized void initializeAppInfra(final Context context) {
        AppInfraInterface appInfra;

        AppInfraSingleton.setInstance(appInfra = new AppInfra.Builder().build(context));

        appInfra=AppInfraSingleton.getInstance();

        mAppTaggingInterface = appInfra.getTagging().
                createInstanceForComponent(IAPAnalyticsConstant.COMPONENT_NAME, BuildConfig.VERSION_NAME);
        mAppTaggingInterface.setPreviousPage("IAPDemo");
        mIAPLoggingInterface=appInfra.getLogging().createInstanceForComponent(IAPAnalyticsConstant.COMPONENT_NAME, BuildConfig.VERSION_NAME);
        IAPLog.enableLogging(true);
        mAppTaggingInterface.setPrivacyConsent(AppTaggingInterface.PrivacyStatus.OPTIN);
    }
}
