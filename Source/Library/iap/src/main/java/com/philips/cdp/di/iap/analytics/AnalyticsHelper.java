/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.analytics;

import android.content.Context;

import com.philips.cdp.di.iap.BuildConfig;
import com.philips.cdp.di.iap.utils.IAPLog;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.tagging.AIAppTaggingInterface;

public class AnalyticsHelper {

    private static volatile AnalyticsHelper mAnalyticsHelper = null;
    private AIAppTaggingInterface mAIAppTaggingInterface;
    private LoggingInterface mIAPLoggingInterface;

    private AnalyticsHelper() {
    }

    public static AnalyticsHelper getInstance() {
        if (mAnalyticsHelper == null) {
            synchronized (AnalyticsHelper.class) {
                if (mAnalyticsHelper == null) {
                    mAnalyticsHelper = new AnalyticsHelper();
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

    public synchronized void initializeIAPTagging(final Context context) {
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
