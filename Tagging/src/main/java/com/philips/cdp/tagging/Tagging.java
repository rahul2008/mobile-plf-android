
package com.philips.cdp.tagging;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.adobe.mobile.Analytics;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Currency;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Tagging {

    private static String trackingIdentifier;

    private static boolean isTagginEnabled;

    private static String launchingPageName;

    private static String componentVersionKey;

    private static String componentVersionVersionValue;

    private static Locale locale;

    private static Context context;

    public static void init(Locale pLocale, Context pContext){
        locale = pLocale;
        context = pContext;
    }

    public static void trackPage(String currPage,String prevPage) {
        validateAppTaggingInitialization();
        if (!isTagginEnabled()) {
            return;
        }
        Map<String, Object> contextData = addAnalyticsDataObject();
        if (null != prevPage) {
            contextData.put(com.philips.cdp.tagging.TaggingConstants.PREVIOUS_PAGE_NAME, prevPage);
        }
        Analytics.trackState(currPage, contextData);

    }



    private static void validateAppTaggingInitialization(){
        if(locale == null && context == null ){
            throw new RuntimeException("Please initialize Tagging library by calling its init method");
        }
    }




    public static void trackAction(String state, String key, Object value) {
        validateAppTaggingInitialization();
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
        validateAppTaggingInitialization();
        if (!isTagginEnabled()) {
            return;
        }
        Map<String, Object> contextData = addAnalyticsDataObject();
        contextData.putAll(map);
        Analytics.trackAction(state, contextData);
    }

    private static Map<String, Object> addAnalyticsDataObject() {
        Map<String, Object> contextData = new HashMap<String, Object>();
        contextData.put(com.philips.cdp.tagging.TaggingConstants.CP_KEY, com.philips.cdp.tagging.TaggingConstants.CP_VALUE);
        contextData.put(com.philips.cdp.tagging.TaggingConstants.APPNAME_KEY, com.philips.cdp.tagging.TaggingConstants.APPNAME_VALUE);
        contextData.put(com.philips.cdp.tagging.TaggingConstants.VERSION_KEY, getAppVersion());
        contextData.put(com.philips.cdp.tagging.TaggingConstants.OS_KEY, com.philips.cdp.tagging.TaggingConstants.OS_ANDROID);
        contextData.put(com.philips.cdp.tagging.TaggingConstants.COUNTRY_KEY, getCountry());
        contextData.put(com.philips.cdp.tagging.TaggingConstants.LANGUAGE_KEY, getLanguage());
        contextData.put(com.philips.cdp.tagging.TaggingConstants.CURRENCY_KEY, getCurrency());
        contextData.put(com.philips.cdp.tagging.TaggingConstants.APPSID_KEY, getTrackingIdentifer());
        contextData.put(com.philips.cdp.tagging.TaggingConstants.TIMESTAMP_KEY, getTimestamp());
        if (null != getComponentVersionKey() && null != getComponentVersionVersionValue()) {
            contextData.put(getComponentVersionKey(), getComponentVersionVersionValue());
        }
        return contextData;
    }

    private static String getCountry(){
        //Locale locale = Locale.getDefault();
        return locale.getCountry();
    }

    private static String getAppVersion() {
        String appVersion = null;
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);

            appVersion = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {

        }
        return appVersion;
    }

    private static String getLanguage(){
        //Locale locale = Locale.getDefault();
        return locale.getLanguage();

    }


    private static String getCurrency() {
        Currency currency = Currency.getInstance(Locale.getDefault());
        String currencyCode = currency.getCurrencyCode();
        if (currencyCode == null)
            currencyCode = com.philips.cdp.tagging.TaggingConstants.DEFAULT_CURRENCY;
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
        Tagging.trackingIdentifier = trackingIdentifier;
    }

    public static boolean isTagginEnabled() {
        return isTagginEnabled;
    }

    public static void enableAppTagging(boolean isTagginEnabled) {
        Tagging.isTagginEnabled = isTagginEnabled;
    }



    public static String getComponentVersionKey() {
        return componentVersionKey;
    }

    public static void setComponentVersionKey(String componentVersionKey) {
        Tagging.componentVersionKey = componentVersionKey;
    }

    public static String getComponentVersionVersionValue() {
        return componentVersionVersionValue;
    }

    public static void setComponentVersionVersionValue(String componentVersionVersionValue) {
        Tagging.componentVersionVersionValue = componentVersionVersionValue;
    }

    public static String getLaunchingPageName() {
        return launchingPageName;
    }

    public  static void setLaunchingPageName(String launchingPageName) {
        Tagging.launchingPageName = launchingPageName;
    }




}
