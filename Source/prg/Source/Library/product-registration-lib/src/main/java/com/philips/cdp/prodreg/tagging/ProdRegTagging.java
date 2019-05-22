/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.cdp.prodreg.tagging;

import android.app.Activity;

import com.philips.cdp.prodreg.constants.AnalyticsConstants;
import com.philips.cdp.product_registration_lib.BuildConfig;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;

import java.util.HashMap;
import java.util.Map;

public class ProdRegTagging {

    private static AppTaggingInterface aiAppTaggingInterface;

    public static void init(AppTaggingInterface appTaggingInterface) {
        aiAppTaggingInterface = appTaggingInterface.createInstanceForComponent("prg", BuildConfig.VERSION_NAME);
    }

    public static void trackPage(String pageName) {
        aiAppTaggingInterface.trackPageWithInfo(pageName, null);
    }

    public static void trackAction(String event, String key, String value) {
        final Map<String, String> commonGoalsMap = new HashMap<>();
        commonGoalsMap.put(key, value);
        aiAppTaggingInterface.trackActionWithInfo(event, commonGoalsMap);
    }

    public static void pauseCollectingLifecycleData() {
        aiAppTaggingInterface.pauseLifecycleInfo();
    }

    public static void collectLifecycleData(Activity activity) {
        aiAppTaggingInterface.collectLifecycleInfo(activity);
    }

    public static void trackAppNotification(String title, String selected) {
        final Map<String, String> commonGoalsMap = new HashMap<>();
        commonGoalsMap.put(AnalyticsConstants.PRG_IN_APP_NOTIFICATION, title);
        commonGoalsMap.put(AnalyticsConstants.PRG_IN_APP_NOTIFICATION_RESPONSE, selected);
        aiAppTaggingInterface.trackActionWithInfo(AnalyticsConstants.SEND_DATA, commonGoalsMap);
    }
}
