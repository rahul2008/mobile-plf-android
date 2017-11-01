/**
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.cdp2.ews.tagging;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.adobe.mobile.Analytics;
import com.philips.cdp2.ews.microapp.EWSDependencyProvider;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("WeakerAccess")
public class EWSTagger {

    private EWSTagger() {
    }

    public static void collectLifecycleInfo(Activity activity) {
        getAppTaggingInterface().collectLifecycleInfo(activity);
    }

    public static void pauseLifecycleInfo() {
        getAppTaggingInterface().pauseLifecycleInfo();
    }

    public static void trackAction(@NonNull final String action, @NonNull final Map<String, String> map) {
        getAppTaggingInterface().trackActionWithInfo(action, map);
    }

    public static void trackPage(String pageName) {

        getAppTaggingInterface().trackPageWithInfo(pageName, null);
    }

    public static void trackActionSendData(@NonNull final String key, @NonNull final String value) {
        Map<String, String> map = new HashMap<>();
        map.put(key, value);
        trackAction(Tag.KEY.SEND_DATA, map);
    }

    @SuppressWarnings("unchecked")
    public static void trackAction(String action, String key, String value) {
        Map<String, String> commonGoalsMap = new HashMap<>();
        commonGoalsMap.put(key, value);
        getAppTaggingInterface().trackActionWithInfo(action, commonGoalsMap);
    }

    @VisibleForTesting()
    static AppTaggingInterface getAppTaggingInterface() {
        return EWSDependencyProvider.getInstance().getTaggingInterface();
    }

    public static void startTimedAction(String action) {
        getAppTaggingInterface().trackTimedActionStart(action);
    }

    public static void stopTimedAction(String action) {
        getAppTaggingInterface().trackTimedActionEnd(action);
    }
}