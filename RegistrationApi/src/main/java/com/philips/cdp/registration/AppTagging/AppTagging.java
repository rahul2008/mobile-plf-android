
package com.philips.cdp.registration.AppTagging;

import com.adobe.mobile.Analytics;
import com.philips.cdp.registration.settings.RegistrationHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Currency;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AppTagging {

    private static String prevPage;

    private static String trackingIdentifier;

    private static boolean isTagginEnabled;

    private static String launchingPageName;

    public static void trackPage(String currPage) {
        if (!isTagginEnabled()) {
            return;
        }
        Map<String, Object> contextData = addAnalyticsDataObject();
        if (null != prevPage) {
            contextData.put(AppTagingConstants.PREVIOUS_PAGE_NAME, prevPage);
        }
        Analytics.trackState(currPage, contextData);
        prevPage = currPage;
    }

    public static void trackFirstPage(String currPage) {
        if (!isTagginEnabled()) {
            return;
        }
        Map<String, Object> contextData = addAnalyticsDataObject();
        if (null != getLaunchingPageName()) {
            contextData.put(AppTagingConstants.PREVIOUS_PAGE_NAME, getLaunchingPageName());
        }
        Analytics.trackState(currPage, contextData);
        prevPage = currPage;
    }

    public static void trackAction(String state, String key, Object value) {
        if (!isTagginEnabled()) {
            return;
        }
        Map<String, Object> contextData = addAnalyticsDataObject();
        if (null != key) {
            contextData.put(key, value);
        }
        Analytics.trackAction(state, contextData);
    }

    public static void trackMultipleActions(String state, Map<String, Object> map) {
        if (!isTagginEnabled()) {
            return;
        }
        Map<String, Object> contextData = addAnalyticsDataObject();
        contextData.putAll(map);
        Analytics.trackAction(state, contextData);
    }

    private static Map<String, Object> addAnalyticsDataObject() {

        Map<String, Object> contextData = new HashMap<String, Object>();
        contextData.put(AppTagingConstants.CP_KEY, AppTagingConstants.CP_VALUE);
        contextData.put(AppTagingConstants.APPNAME_KEY, AppTagingConstants.APPNAME_VALUE);
        contextData.put(AppTagingConstants.VERSION_KEY, RegistrationHelper.getInstance()
                .getAppVersion());
        contextData.put(AppTagingConstants.OS_KEY, AppTagingConstants.OS_ANDROID);
        contextData.put(AppTagingConstants.COUNTRY_KEY, RegistrationHelper.getInstance()
                .getLocale().getCountry());
        contextData.put(AppTagingConstants.LANGUAGE_KEY, RegistrationHelper.getInstance()
                .getLocale().getLanguage());
        contextData.put(AppTagingConstants.CURRENCY_KEY, getCurrency());
        contextData.put(AppTagingConstants.APPSID_KEY, getTrackingIdentifer());
        contextData.put(AppTagingConstants.TIMESTAMP_KEY, getTimestamp());

        return contextData;
    }

    private static String getCurrency() {
        Currency currency = Currency.getInstance(Locale.getDefault());
        String currencyCode = currency.getCurrencyCode();
        if (currencyCode == null)
            currencyCode = AppTagingConstants.DEFAULT_CURRENCY;
        return currencyCode;
    }

    private static String getTimestamp() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

    public static String getTrackingIdentifer() {
        return trackingIdentifier;
    }

    public static void setTrackingIdentifier(String trackingIdentifier) {
        AppTagging.trackingIdentifier = trackingIdentifier;
    }

    public static boolean isTagginEnabled() {
        return isTagginEnabled;
    }

    public static void enableAppTagging(boolean isTagginEnabled) {
        AppTagging.isTagginEnabled = isTagginEnabled;
    }

    public static String getLaunchingPageName() {
        return launchingPageName;
    }

    public static void setLaunchingPageName(String launchingPageName) {
        AppTagging.launchingPageName = launchingPageName;
    }

}
