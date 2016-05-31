package com.philips.cdp.di.iap.analytics;

import com.philips.cdp.tagging.Tagging;

public class IAPAnalytics {

    private static String sPreviousPage;

    public static void trackPage(String currPage) {
        if (sPreviousPage == null) {
            sPreviousPage = Tagging.getLaunchingPageName();
        }
        Tagging.trackPage(currPage, sPreviousPage);
        sPreviousPage = currPage;
    }
}
