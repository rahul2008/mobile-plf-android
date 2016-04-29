
package com.philips.appinfra.tagging;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.adobe.mobile.Analytics;
import com.adobe.mobile.Config;
import com.adobe.mobile.MobilePrivacyStatus;

import java.text.DateFormat;
import java.util.Currency;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class Tagging {
    private static String trackingIdentifier;

    private static boolean isTagginEnabled;

    private static String launchingPageName;

    private static String componentVersionKey;

    private static String componentVersionVersionValue;

    private static Locale mlocale;

    private static Context mcontext;

    private static String mAppName;

    public static void init(Locale locale, Context context,String appName){
        mlocale = locale;
        mcontext = context;
        mAppName = appName;
        Config.setContext(context);
        if(appName == null){
            throw new RuntimeException("Please set app name for tagging library");
        }
    }

    public static void trackPage(String currPage,String prevPage) {
        validateAppTaggingInitialization();
        if (!isTagginEnabled()) {
            return;
        }
        Map<String, Object> contextData = addAnalyticsDataObject();
        if (null != prevPage) {
            contextData.put(TaggingConstants.PREVIOUS_PAGE_NAME, prevPage);
        }
        Analytics.trackState(currPage, contextData);

    }

    public static void setDebuggable(final boolean enable){
        Config.setDebugLogging(enable);
    }

    public static void setPrivacyStatus(TaggingWrapper.AIATMobilePrivacyStatus privacyStatus){
        switch (privacyStatus) {
            case MOBILE_PRIVACY_STATUS_OPT_IN:
                Config.setPrivacyStatus(MobilePrivacyStatus.MOBILE_PRIVACY_STATUS_OPT_IN);
                break;
            case MOBILE_PRIVACY_STATUS_OPT_OUT:
                Config.setPrivacyStatus(MobilePrivacyStatus.MOBILE_PRIVACY_STATUS_OPT_OUT);

                break;
            case MOBILE_PRIVACY_STATUS_UNKNOWN:
                Config.setPrivacyStatus(MobilePrivacyStatus.MOBILE_PRIVACY_STATUS_UNKNOWN);
                break;

        }


    }
    public static void getPrivacyStatus(){
        Config.getPrivacyStatus();
    }


    private static void validateAppTaggingInitialization(){
        if(mlocale == null && mcontext == null && isTagginEnabled()){
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
        contextData.put(TaggingConstants.CP_KEY, TaggingConstants.CP_VALUE);
        contextData.put(TaggingConstants.APPNAME_KEY, mAppName);
        contextData.put(TaggingConstants.VERSION_KEY, getAppVersion());
        contextData.put(TaggingConstants.OS_KEY, TaggingConstants.OS_ANDROID);
        contextData.put(TaggingConstants.COUNTRY_KEY, getCountry());
        contextData.put(TaggingConstants.LANGUAGE_KEY, getLanguage());
        contextData.put(TaggingConstants.CURRENCY_KEY, getCurrency());
        contextData.put(TaggingConstants.APPSID_KEY, getTrackingIdentifer());
        contextData.put(TaggingConstants.TIMESTAMP_KEY, getTimestamp());
        if (null != getComponentVersionKey() && null != getComponentVersionVersionValue()) {
            contextData.put(getComponentVersionKey(), getComponentVersionVersionValue());
        }
        return contextData;
    }

    private static String getCountry(){
        return mlocale.getCountry();
    }

    private static String getAppVersion() {
        String appVersion = null;
        try {
            PackageInfo packageInfo = mcontext.getPackageManager().getPackageInfo(
                    mcontext.getPackageName(), 0);

            appVersion = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {

        }
        return appVersion;
    }

    private static String getLanguage(){
        return mlocale.getLanguage();

    }

    private static String getCurrency() {
        Currency currency = Currency.getInstance(mlocale);
        String currencyCode = currency.getCurrencyCode();
        if (currencyCode == null)
            currencyCode = TaggingConstants.DEFAULT_CURRENCY;
        return currencyCode;
    }

    private static String getTimestamp() {
//        Calendar c = Calendar.getInstance();
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String formattedDate = df.format(c.getTime());

        DateFormat df = DateFormat.getTimeInstance();
        df.setTimeZone(TimeZone.getTimeZone("gmt"));
        String gmtTime = df.format(new Date());
        return gmtTime;
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

    public static void setLaunchingPageName(String launchingPageName) {
        Tagging.launchingPageName = launchingPageName;
    }

    public static void pauseCollectingLifecycleData(){
        Config.pauseCollectingLifecycleData();
    }

    public static void collectLifecycleData(){
        Config.collectLifecycleData();
    }
}
