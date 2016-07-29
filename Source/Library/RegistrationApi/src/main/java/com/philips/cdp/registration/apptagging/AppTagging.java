/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.apptagging;

import android.app.Activity;

import com.philips.cdp.registration.settings.RegistrationHelper;

import java.util.HashMap;
import java.util.Map;

public class AppTagging {


    public static void trackPage(String currPage) {
        final Map<String, String> commonGoalsMap = getCommonGoalsMap();
        RegistrationHelper.getInstance().getAppTaggingInterface().
                trackPageWithInfo(currPage, commonGoalsMap);
    }

    public static void trackFirstPage(String currPage) {
        trackPage(currPage);
    }

    public static void trackAction(String state, String key, String value) {
        final Map<String, String> commonGoalsMap = getCommonGoalsMap();
        commonGoalsMap.put(key, value);
        RegistrationHelper.getInstance().getAppTaggingInterface().
                trackActionWithInfo(state, commonGoalsMap);
    }

    public static void trackMultipleActions(String state, Map<String, String> map) {
        final Map<String, String> commonGoalsMap = getCommonGoalsMap();
        commonGoalsMap.putAll(map);
        RegistrationHelper.getInstance().getAppTaggingInterface().trackActionWithInfo(state, map);
    }

    public static Map<String, String> getCommonGoalsMap() {
        return new HashMap<>();
    }

    public static void pauseCollectingLifecycleData() {
        RegistrationHelper.getInstance().getAppTaggingInterface().pauseLifecycleInfo();
    }

    public static void collectLifecycleData(Activity activity) {
        RegistrationHelper.getInstance().getAppTaggingInterface().collectLifecycleInfo(activity);
    }

}
