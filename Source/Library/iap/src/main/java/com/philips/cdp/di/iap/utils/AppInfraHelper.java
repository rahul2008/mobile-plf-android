/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.utils;

import android.content.Context;

import com.philips.cdp.di.iap.BuildConfig;
import com.philips.cdp.di.iap.analytics.IAPAnalyticsConstant;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.tagging.AIAppTaggingInterface;

public class AppInfraHelper {

    private static volatile AppInfraHelper mAnalyticsHelper = null;
    private AIAppTaggingInterface mAIAppTaggingInterface;
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

    public AIAppTaggingInterface getAIAppTaggingInterface() {
        return mAIAppTaggingInterface;
    }

    public LoggingInterface getIAPLoggingInterfaceInterface() {
        return mIAPLoggingInterface;
    }

    public synchronized void initializeAppInfra(final Context context) {
//        Tagging.setDebuggable(true);
//        Tagging.enableAppTagging(true);
        AppInfra appInfra = new AppInfra.Builder().build(context);
        mAIAppTaggingInterface = appInfra.getTagging().
                createInstanceForComponent(IAPAnalyticsConstant.COMPONENT_NAME, BuildConfig.VERSION_NAME);
        mAIAppTaggingInterface.setPreviousPage("IAPDemo");
        mIAPLoggingInterface=appInfra.getLogging().createInstanceForComponent(IAPAnalyticsConstant.COMPONENT_NAME, BuildConfig.VERSION_NAME);
        IAPLog.enableLogging(true);
//        mAIAppTaggingInterface.setPrivacyConsent(AIAppTaggingInterface.PrivacyStatus.OPTIN);
    }
}
