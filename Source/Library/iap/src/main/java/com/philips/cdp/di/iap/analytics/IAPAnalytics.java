package com.philips.cdp.di.iap.analytics;

import com.philips.cdp.tagging.Tagging;

import java.util.Map;

public class IAPAnalytics {

    private static String sPreviousPage;

    public static void trackPage(String currPage) {
        if (sPreviousPage == null) {
            sPreviousPage = Tagging.getLaunchingPageName();
        }
        Tagging.trackPage(currPage, sPreviousPage);
        sPreviousPage = currPage;
    }

    public static void trackAction(String state, String key, Object value) {
        Tagging.trackAction(state, key, value);
    }

    public static void trackMultipleActions(String state, Map<String, Object> map) {
        Tagging.trackMultipleActions(state, map);
    }
}
