/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.mec.analytics;

import android.app.Activity;

import com.philips.cdp.di.mec.integration.MECDependencies;
import com.philips.cdp.di.mec.utils.MECLog;
import com.philips.platform.appinfra.BuildConfig;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;

import java.util.HashMap;
import java.util.Map;

public class MECAnalytics {
    public static AppTaggingInterface sAppTaggingInterface;

    private MECAnalytics(){

    }

    public static void initIAPAnalytics(MECDependencies dependencies) {
        sAppTaggingInterface =
                dependencies.getAppInfra().getTagging().
                        createInstanceForComponent(MECAnalyticsConstant.COMPONENT_NAME, BuildConfig.VERSION_NAME);
    }

    public static void trackPage(String currentPage) {
        if (sAppTaggingInterface != null) {
            Map<String, String> map = new HashMap<>();
            sAppTaggingInterface.trackPageWithInfo(currentPage, map);
        }
    }

    public static void trackAction(String state, String key, Object value) {
        String valueObject = (String) value;
        MECLog.i(MECLog.LOG, "trackAction" + valueObject);
        if (sAppTaggingInterface != null)
            sAppTaggingInterface.
                    trackActionWithInfo(state, key, valueObject);
    }

    public static void trackMultipleActions(String state, Map<String, String> map) {
        if (sAppTaggingInterface != null)
            sAppTaggingInterface.
                    trackActionWithInfo(state, map);
    }

    public static void pauseCollectingLifecycleData() {
        if (sAppTaggingInterface != null)
            sAppTaggingInterface.pauseLifecycleInfo();
    }

    public static void collectLifecycleData(Activity activity) {
        if (sAppTaggingInterface != null)
            sAppTaggingInterface.collectLifecycleInfo(activity);
    }

    public static void clearAppTaggingInterface() {
        sAppTaggingInterface = null;
    }
}
