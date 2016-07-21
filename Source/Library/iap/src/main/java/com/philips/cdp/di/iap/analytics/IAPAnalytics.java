/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.analytics;

import com.adobe.mobile.Config;
import com.philips.cdp.di.iap.utils.AppInfraHelper;

import java.util.HashMap;
import java.util.Map;

public class IAPAnalytics {

    public static void trackPage(String currentPage) {
        if (AppInfraHelper.getInstance().getAIAppTaggingInterface() != null) {
            Map<String, String> map = new HashMap<>();
            AppInfraHelper.getInstance().getAIAppTaggingInterface().
                    trackPageWithInfo(currentPage, map);
//            AppInfraHelper.getInstance().getAIAppTaggingInterface().setPreviousPage(currentPage);
        }
    }

    public static void trackAction(String state, String key, Object value) {
        String valueObject = (String) value;
        if (AppInfraHelper.getInstance().getAIAppTaggingInterface() != null)
            AppInfraHelper.getInstance().getAIAppTaggingInterface().
                    trackActionWithInfo(state, key, valueObject);
    }

    public static void trackMultipleActions(String state, Map<String, String> map) {
        if (AppInfraHelper.getInstance().getAIAppTaggingInterface() != null)
            AppInfraHelper.getInstance().getAIAppTaggingInterface().
                    trackActionWithInfo(state, map);
    }

    public static void pauseCollectingLifecycleData() {
//        if (AppInfraHelper.getInstance().getAIAppTaggingInterface() != null)
//            AppInfraHelper.getInstance().getAIAppTaggingInterface().;
        Config.pauseCollectingLifecycleData();
    }

    public static void collectLifecycleData() {
//        if (AppInfraHelper.getInstance().getAIAppTaggingInterface() != null)
//            AppInfraHelper.getInstance().getAIAppTaggingInterface().collectLifecycleInfo(activity);
        Config.collectLifecycleData();
    }
}
