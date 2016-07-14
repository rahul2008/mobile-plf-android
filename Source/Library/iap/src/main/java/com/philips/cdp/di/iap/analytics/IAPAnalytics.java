/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.analytics;

import com.adobe.mobile.Config;

import java.util.HashMap;
import java.util.Map;

public class IAPAnalytics {

    public static void trackPage(String currentPage) {
        if (AnalyticsHelper.getInstance().getAIAppTaggingInterface() != null) {
            Map<String, String> map = new HashMap<>();
            AnalyticsHelper.getInstance().getAIAppTaggingInterface().
                    trackPageWithInfo(currentPage, map);
            AnalyticsHelper.getInstance().getAIAppTaggingInterface().setPreviousPage(currentPage);
        }
    }

    public static void trackAction(String state, String key, Object value) {
        String valueObject = (String) value;
        if (AnalyticsHelper.getInstance().getAIAppTaggingInterface() != null)
            AnalyticsHelper.getInstance().getAIAppTaggingInterface().
                    trackActionWithInfo(state, key, valueObject);
    }

    public static void trackMultipleActions(String state, Map<String, String> map) {
        if (AnalyticsHelper.getInstance().getAIAppTaggingInterface() != null)
            AnalyticsHelper.getInstance().getAIAppTaggingInterface().
                    trackActionWithInfo(state, map);
    }

    public static void pauseCollectingLifecycleData() {
//        if (AnalyticsHelper.getInstance().getAIAppTaggingInterface() != null)
//            AnalyticsHelper.getInstance().getAIAppTaggingInterface().;
        Config.pauseCollectingLifecycleData();
    }

    public static void collectLifecycleData() {
//        if (AnalyticsHelper.getInstance().getAIAppTaggingInterface() != null)
//            AnalyticsHelper.getInstance().getAIAppTaggingInterface().collectLifecycleInfo(activity);
        Config.collectLifecycleData();
    }
}
