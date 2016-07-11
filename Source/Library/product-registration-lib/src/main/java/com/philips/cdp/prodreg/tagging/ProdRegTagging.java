package com.philips.cdp.prodreg.tagging;

import android.annotation.SuppressLint;
import android.os.Build;

import com.adobe.mobile.Analytics;
import com.philips.cdp.product_registration_lib.BuildConfig;
import com.philips.platform.appinfra.AppInfraSingleton;
import com.philips.platform.appinfra.tagging.AIAppTaggingInterface;

import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ProdRegTagging {

    private static ProdRegTagging prodRegTagging;
    private static AIAppTaggingInterface aiAppTaggingInterface;

    private ProdRegTagging() {
    }

    public static ProdRegTagging getInstance() {
        if (prodRegTagging == null) {
            prodRegTagging = new ProdRegTagging();
            aiAppTaggingInterface = AppInfraSingleton.getInstance().getTagging();
            aiAppTaggingInterface.setPrivacyConsent(AIAppTaggingInterface.PrivacyStatus.OPTIN);
        }
        return prodRegTagging;
    }

    private static String getCurrency() {
        Currency currency = Currency.getInstance(Locale.getDefault());
        String currencyCode = currency.getCurrencyCode();
        if (currencyCode == null)
            currencyCode = AnalyticsConstants.KEY_CURRENCY;
        return currencyCode;
    }

    public static String getAppVersion() {
        return BuildConfig.VERSION_NAME;
    }

    @SuppressLint("SimpleDateFormat")
    private static String getTimestamp() {
        long timeMillis = System.currentTimeMillis();
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        String date = dateFormat.format(new Date(timeMillis));
        return date;
    }

    public AIAppTaggingInterface getAiAppTaggingInterface() {
        return aiAppTaggingInterface;
    }

    /*
     * This context data will be called for every page track.
     */
    public Map<String, String> getCommonGoalsMap() {
        Map<String, String> contextData = new HashMap<>();
        contextData.put(AnalyticsConstants.KEY_APP_NAME,
                AnalyticsConstants.ACTION_VALUE_APP_NAME);
        contextData.put(AnalyticsConstants.KEY_VERSION, String.valueOf(getAppVersion()));
        contextData
                .put(AnalyticsConstants.KEY_OS,
                        AnalyticsConstants.ACTION_VALUE_ANDROID
                                + Build.VERSION.RELEASE);
        contextData.put(AnalyticsConstants.KEY_COUNTRY,
                Locale.getDefault().getCountry());
        contextData.put(AnalyticsConstants.KEY_LANGUAGE,
                Locale.getDefault().getLanguage());
        contextData.put(AnalyticsConstants.KEY_CURRENCY, getCurrency());
        contextData.put(AnalyticsConstants.KEY_TIME_STAMP, getTimestamp());
        contextData.put(AnalyticsConstants.KEY_APP_ID,
                Analytics.getTrackingIdentifier());
        contextData.put(AnalyticsConstants.APP_SOURCE,
                AnalyticsConstants.APP_SOURCE_VALUE);
        return contextData;
    }

    public void trackPageWithCommonGoals(String pageName, Map<String, String> trackInfo) {
        final Map<String, String> commonGoalsMap = getCommonGoalsMap();
        commonGoalsMap.putAll(trackInfo);
        getAiAppTaggingInterface().trackPageWithInfo(pageName, commonGoalsMap);
    }

    public void trackPageWithCommonGoals(String pageName, String key, String value) {
        final Map<String, String> commonGoalsMap = getCommonGoalsMap();
        commonGoalsMap.put(key, value);
        getAiAppTaggingInterface().trackPageWithInfo(pageName, commonGoalsMap);
    }

    public void trackOnlyPage(String pageName, Map<String, String> trackInfo) {
        getAiAppTaggingInterface().trackPageWithInfo(pageName, trackInfo);
    }

    public void trackOnlyPage(String pageName, String key, String value) {
        final Map<String, String> hashMap = new HashMap<>();
        hashMap.put(key, value);
        getAiAppTaggingInterface().trackPageWithInfo(pageName, hashMap);
    }

    public void trackActionWithCommonGoals(String event, Map<String, String> trackInfo) {
        final Map<String, String> commonGoalsMap = getCommonGoalsMap();
        commonGoalsMap.putAll(trackInfo);
        getAiAppTaggingInterface().trackActionWithInfo(event, commonGoalsMap);
    }

    public void trackActionWithCommonGoals(String event, String key, String value) {
        final Map<String, String> commonGoalsMap = getCommonGoalsMap();
        commonGoalsMap.put(key, value);
        getAiAppTaggingInterface().trackActionWithInfo(event, commonGoalsMap);
    }

    public void trackOnlyAction(String event, Map<String, String> trackInfo) {
        getAiAppTaggingInterface().trackActionWithInfo(event, trackInfo);
    }

    public void trackOnlyAction(String event, String key, String value) {
        final Map<String, String> hashMap = new HashMap<>();
        hashMap.put(key, value);
        getAiAppTaggingInterface().trackActionWithInfo(event, hashMap);
    }
}
