package com.philips.cdp.di.iap.analytics;

import com.philips.cdp.tagging.Tagging;

public class IAPAnalytics {

    private static String sPreviousPage;

    public static void trackPage(String currPage) {
        if (null != sPreviousPage) {
            Tagging.trackPage(currPage, sPreviousPage);
        } else {
            Tagging.trackPage(currPage, null);
        }
        sPreviousPage = currPage;
    }

    public static void trackLaunchPage(String currPage) {
        if (null != Tagging.getLaunchingPageName()) {
            Tagging.trackPage(currPage, Tagging.getLaunchingPageName());
        } else {
            Tagging.trackPage(currPage, null);
        }
        sPreviousPage = currPage;
    }
}
