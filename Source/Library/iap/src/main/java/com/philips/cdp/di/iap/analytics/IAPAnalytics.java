/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.analytics;

import android.app.Activity;

import com.philips.cdp.di.iap.utils.AppInfraHelper;

import java.util.HashMap;
import java.util.Map;

public class IAPAnalytics {

    public static void trackPage(String currentPage) {
        if (AppInfraHelper.getInstance().getIapTaggingInterface() != null) {
            Map<String, String> map = new HashMap<>();
            AppInfraHelper.getInstance().getIapTaggingInterface().
                    trackPageWithInfo(currentPage, map);
        }
    }

    public static void trackAction(String state, String key, Object value) {
        String valueObject = (String) value;
        if (AppInfraHelper.getInstance().getIapTaggingInterface() != null)
            AppInfraHelper.getInstance().getIapTaggingInterface().
                    trackActionWithInfo(state, key, valueObject);
    }

    public static void trackMultipleActions(String state, Map<String, String> map) {
        if (AppInfraHelper.getInstance().getIapTaggingInterface() != null)
            AppInfraHelper.getInstance().getIapTaggingInterface().
                    trackActionWithInfo(state, map);
    }

    public static void pauseCollectingLifecycleData() {
        if (AppInfraHelper.getInstance().getIapTaggingInterface() != null)
            AppInfraHelper.getInstance().getIapTaggingInterface().pauseLifecycleInfo();
    }

    public static void collectLifecycleData(Activity activity) {
        if (AppInfraHelper.getInstance().getIapTaggingInterface() != null)
            AppInfraHelper.getInstance().getIapTaggingInterface().collectLifecycleInfo(activity);
    }
}
