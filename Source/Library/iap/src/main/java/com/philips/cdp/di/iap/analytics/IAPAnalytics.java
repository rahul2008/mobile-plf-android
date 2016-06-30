/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.analytics;

import com.philips.cdp.tagging.Tagging;

import java.util.HashMap;
import java.util.Map;

public class IAPAnalytics {

    private static String sPreviousPage;

    public static void trackPage(String currentPage) {
        if (sPreviousPage == null) {
            sPreviousPage = Tagging.getLaunchingPageName();
        }
        Map<String, String> map = new HashMap<>();
        AnalyticsHelper.getInstance().getAIAppTaggingInterface().
                trackPageWithInfo(currentPage, map);
        sPreviousPage = currentPage;
    }

    public static void trackAction(String state, String key, Object value) {
        String valueObject = (String) value;
        AnalyticsHelper.getInstance().getAIAppTaggingInterface().
                trackActionWithInfo(state, key, valueObject);
    }

    public static void trackMultipleActions(String state, Map<String, String> map) {
        AnalyticsHelper.getInstance().getAIAppTaggingInterface().
                trackActionWithInfo(state, map);
    }

    public static void pauseCollectingLifecycleData(){
        AnalyticsHelper.getInstance().getAIAppTaggingInterface().pauseCollectingLifecycleData();
    }

    public static void collectLifecycleData(){
        AnalyticsHelper.getInstance().getAIAppTaggingInterface().collectLifecycleData();
    }
}
