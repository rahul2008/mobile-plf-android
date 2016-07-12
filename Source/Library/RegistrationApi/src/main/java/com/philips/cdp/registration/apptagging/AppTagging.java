
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
import com.philips.platform.appinfra.AppInfraSingleton;
import com.philips.platform.appinfra.tagging.AIAppTaggingInterface;

import java.util.HashMap;
import java.util.Map;

public class AppTagging {



    public static void trackPage(String currPage) {
        AIAppTaggingInterface aiAppTaggingInterface = AppInfraSingleton.getInstance().getTagging();
        final Map<String, String> commonGoalsMap = getCommonGoalsMap();
        aiAppTaggingInterface.trackPageWithInfo(currPage, commonGoalsMap);
    }

    public static void trackFirstPage(String currPage) {
        trackPage(currPage);
    }

    public static void trackAction(String state, String key, String value) {
        AIAppTaggingInterface aiAppTaggingInterface = RegistrationHelper.getInstance().getAppInfraInstance().getTagging();
        final Map<String, String> commonGoalsMap = getCommonGoalsMap();
        commonGoalsMap.put(key, value);
        aiAppTaggingInterface.trackActionWithInfo(state, commonGoalsMap);
    }

    public static void trackMultipleActions(String state, Map<String, String> map) {
        AIAppTaggingInterface aiAppTaggingInterface = RegistrationHelper.getInstance().getAppInfraInstance().getTagging();
        final Map<String, String> commonGoalsMap = getCommonGoalsMap();
        commonGoalsMap.putAll(map);
        aiAppTaggingInterface.trackActionWithInfo(state, map);
    }

    public static Map<String, String> getCommonGoalsMap() {
        Map<String, String> contextData = new HashMap<>();
        return contextData;
    }

    public static void pauseCollectingLifecycleData() {
        AIAppTaggingInterface aiAppTaggingInterface = RegistrationHelper.getInstance().getAppInfraInstance().getTagging();
        aiAppTaggingInterface.pauseLifecycleInfo();
    }

    public static void collectLifecycleData(Activity activity) {
        AIAppTaggingInterface aiAppTaggingInterface = RegistrationHelper.getInstance().getAppInfraInstance().getTagging();
        aiAppTaggingInterface.collectLifecycleInfo(activity);
    }

}
