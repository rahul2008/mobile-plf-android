/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.cdp.prodreg.tagging;

import android.app.Activity;

import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;

import java.util.HashMap;
import java.util.Map;

public class ProdRegTagging {

    private static AppTaggingInterface aiAppTaggingInterface;

    public static void init() {
        aiAppTaggingInterface = RegistrationConfiguration.getInstance().getComponent().getAppTaggingInterface();
        aiAppTaggingInterface = aiAppTaggingInterface.createInstanceForComponent("prg", com.philips.cdp.registration.BuildConfig.VERSION_NAME);
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

}
