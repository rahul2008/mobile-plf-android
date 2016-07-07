
/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.apptagging;

import android.annotation.SuppressLint;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import com.adobe.mobile.Analytics;
import com.adobe.mobile.Config;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraSingleton;
import com.philips.platform.appinfra.tagging.AIAppTaggingInterface;

import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AppTagging {
    private static final String KEY_APP_NAME = "app.name";
    private static final String KEY_VERSION = "app.version";
    private static final String KEY_OS = "app.os";
    private static final String KEY_LANGUAGE = "locale.language";
    private static final String KEY_CURRENCY = "locale.currency";
    private static final String KEY_COUNTRY = "locale.country";
    private static final String KEY_TIME_STAMP = "timestamp";
    private static final String KEY_APP_ID = "appsId";
    private static final String APP_SOURCE = "appSource";
    private static final String APP_SOURCE_VALUE = "PlayStore";
    private static final String ACTION_VALUE_ANDROID = "Android ";


    public static void trackPage(String currPage) {
        AIAppTaggingInterface aiAppTaggingInterface = AppInfraSingleton.getInstance().getTagging();
        final Map<String, String> commonGoalsMap = getCommonGoalsMap();
        aiAppTaggingInterface.trackPageWithInfo(currPage, commonGoalsMap);
    }

    public static void trackFirstPage(String currPage) {
        trackPage(currPage);
    }

    public static void trackAction(String state, String key, String value) {

        //RegistrationHelper registrationHelper = RegistrationHelper.getInstance();
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
        contextData
                .put(KEY_OS,
                        ACTION_VALUE_ANDROID
                                + Build.VERSION.RELEASE);
        contextData.put(KEY_COUNTRY,
                Locale.getDefault().getCountry());
        contextData.put(KEY_APP_ID,
                Analytics.getTrackingIdentifier());
        contextData.put(APP_SOURCE,
                APP_SOURCE_VALUE);
        return contextData;
    }

    public static void pauseCollectingLifecycleData() {
        Config.pauseCollectingLifecycleData();
    }

    public static void collectLifecycleData() {
        Config.collectLifecycleData();

    }

}
