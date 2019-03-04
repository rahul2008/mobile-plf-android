/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.platform.pim.utilities;

import android.app.Activity;
import android.support.annotation.VisibleForTesting;

import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.pim.configration.PimConfiguration;

import java.util.HashMap;
import java.util.Map;


public class PimTagging {

    private static AppTaggingInterface appTaggingInterface;

    public static void init(){
        appTaggingInterface = PimConfiguration.getInstance().getComponent().getAppTaggingInterface();
        appTaggingInterface = appTaggingInterface.createInstanceForComponent(PimConstants.COMPONENT_TAGS_ID, PimConstants.getAppAuthApiVersion());
    }

    public static void trackPage(String currPage) {
        final Map<String, String> commonGoalsMap = getCommonGoalsMap();
        appTaggingInterface.trackPageWithInfo(currPage, commonGoalsMap);
    }

    public static void trackFirstPage(String currPage) {
        trackPage(currPage);
    }

    public static void trackAction(String state, String key, String value) {
        final Map<String, String> commonGoalsMap = getCommonGoalsMap();
        commonGoalsMap.put(key, value);
        appTaggingInterface.trackActionWithInfo(state, commonGoalsMap);
    }

    public static void trackMultipleActions(String state, Map<String, String> map) {
        final Map<String, String> commonGoalsMap = getCommonGoalsMap();
        commonGoalsMap.putAll(map);
        appTaggingInterface.trackActionWithInfo(state, map);
    }

    private static Map<String, String> getCommonGoalsMap() {
        return new HashMap<>();
    }

    public static void pauseCollectingLifecycleData() {
        appTaggingInterface.pauseLifecycleInfo();
    }

    public static void collectLifecycleData(Activity activity) {
        appTaggingInterface.collectLifecycleInfo(activity);
    }


    @VisibleForTesting
    public static void setMockAppTaggingInterface(AppTaggingInterface mockAppTaggingInterface) {
        appTaggingInterface = mockAppTaggingInterface;
    }
}
