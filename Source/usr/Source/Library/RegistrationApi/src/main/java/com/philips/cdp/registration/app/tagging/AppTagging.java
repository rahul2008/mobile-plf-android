/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.app.tagging;

import android.app.Activity;

import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.ui.utils.URInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;

import java.util.HashMap;
import java.util.Map;


public class AppTagging {

    private static AppTaggingInterface appTaggingInterface = RegistrationConfiguration.getInstance().getComponent().getAppTaggingInterface();

    public static void trackPage(String currPage) {
        final Map<String, String> commonGoalsMap = getCommonGoalsMap();
        try {
            appTaggingInterface.trackPageWithInfo(currPage, commonGoalsMap);
        } catch (IllegalArgumentException exception) {}
    }

    public static void trackFirstPage(String currPage) {
        trackPage(currPage);
    }

    public static void trackAction(String state, String key, String value) {
        final Map<String, String> commonGoalsMap = getCommonGoalsMap();
        commonGoalsMap.put(key, value);
        try {
            appTaggingInterface.trackActionWithInfo(state, commonGoalsMap);
        } catch (IllegalArgumentException exception) {}
    }

    public static void trackMultipleActions(String state, Map<String, String> map) {
        final Map<String, String> commonGoalsMap = getCommonGoalsMap();
        commonGoalsMap.putAll(map);
        try {
            appTaggingInterface.trackActionWithInfo(state, map);
        } catch (IllegalArgumentException exception) {}
    }

    public static Map<String, String> getCommonGoalsMap() {
        return new HashMap<>();
    }

    public static void pauseCollectingLifecycleData() {
        appTaggingInterface.pauseLifecycleInfo();
    }

    public static void collectLifecycleData(Activity activity) {
        appTaggingInterface.collectLifecycleInfo(activity);
    }

}
