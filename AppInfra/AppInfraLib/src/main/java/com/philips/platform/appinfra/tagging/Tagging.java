
package com.philips.platform.appinfra.tagging;

import android.content.Context;

import com.adobe.mobile.Analytics;
import com.adobe.mobile.Config;
import com.adobe.mobile.MobilePrivacyStatus;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
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

    private static String newFieldKey;

    private static String newFieldValue;

    private static String componentVersionVersionValue ;
    private static String mLanguage;
//    private static String mCountry;
    private static String mAppsIdkey;
    private static String mLocalTimestamp;
    private static String mUTCTimestamp;


    private static String[] defaultValues = {
            TaggingConstants.LANGUAGE_KEY,
            TaggingConstants.APPSID_KEY,
            TaggingConstants.COMPONENT_ID,
            TaggingConstants.COMPONENT_VERSION,
            TaggingConstants.LOCAL_TIMESTAMP_KEY,


    };


//    private static String mCurruncy;

    private static Locale mlocale;

    private static Context mcontext;

    private static String mAppName;
    private static Map<String, Object> contextData;

    public static void init(Locale locale, Context context,String appName){
        mlocale = locale;
        mcontext = context;
        mAppName = appName;
        Config.setContext(context);
        contextData = addAnalyticsDataObject();

        if(appName == null){
            throw new RuntimeException("Please set app name for tagging library");
        }
    }

    public static void trackPage(String currPage,String prevPage, String key, String value) {
        validateAppTaggingInitialization();
        if (!isTaggingEnabled()) {
            return;
        }

        if(Arrays.asList(defaultValues).contains(key)){

            switch (key){
                case TaggingConstants.LANGUAGE_KEY:
                    contextData = addAnalyticsDataObject();
                    contextData.put(TaggingConstants.LANGUAGE_KEY, value);
                    setLanguageOverridden(value);
                    break;
                case TaggingConstants.APPSID_KEY:
                    contextData = addAnalyticsDataObject();
                    contextData.put(TaggingConstants.APPSID_KEY, value);
                    setAppsIdkeyOverridden(value);
                    break;
                case TaggingConstants.LOCAL_TIMESTAMP_KEY:
                    contextData = addAnalyticsDataObject();
                    contextData.put(TaggingConstants.LOCAL_TIMESTAMP_KEY, value);
                    setLocalTimeStampOverridden(value);
                    break;
                case TaggingConstants.UTC_TIMESTAMP_KEY:
                    contextData = addAnalyticsDataObject();
                    contextData.put(TaggingConstants.UTC_TIMESTAMP_KEY, value);
                    setUTCTimeStampOverridden(value);
                    break;
                case TaggingConstants.COMPONENT_ID:
                    contextData = addAnalyticsDataObject();
                    contextData.put(TaggingConstants.COMPONENT_ID, value);
                    setComponentID(value);
                    break;
                case TaggingConstants.COMPONENT_VERSION:
                    contextData = addAnalyticsDataObject();
                    contextData.put(TaggingConstants.COMPONENT_VERSION, value);
                    setComponentVersionVersionValue(value);
                    break;

            }


        }else{
            setNewKey(key);
            setNewValue(value);
            contextData = addAnalyticsDataObject();
        }
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
        if(mlocale == null && mcontext == null && isTaggingEnabled()){
            throw new RuntimeException("Please initialize Tagging library by calling its init method");
        }
    }




    public static void trackAction(String state, String key, Object value) {
        validateAppTaggingInitialization();
        if (!isTaggingEnabled()) {
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
        if (!isTaggingEnabled()) {
            return;
        }
        Map<String, Object> contextData = addAnalyticsDataObject();

        Map tmp = new HashMap(map);
        tmp.keySet().removeAll(contextData.keySet());
//        target.putAll(tmp);
        contextData.putAll(map);
        Analytics.trackAction(state, contextData);
    }

    public static void trackMultipleState(String state, Map<String, Object> map) {
        validateAppTaggingInitialization();
        if (!isTaggingEnabled()) {
            return;
        }
        Map<String, Object> contextData = addAnalyticsDataObject();
        contextData.putAll(map);
        Analytics.trackState(state, contextData);
    }

    private static Map<String, Object> addAnalyticsDataObject() {
        Map<String, Object> contextData = new HashMap<String, Object>();

//        contextData.put(TaggingConstants.CP_KEY, TaggingConstants.CP_VALUE);
//        contextData.put(TaggingConstants.APPNAME_KEY, mAppName);
//        contextData.put(TaggingConstants.VERSION_KEY, getAppVersion());
//        contextData.put(TaggingConstants.OS_KEY, TaggingConstants.OS_ANDROID);
//        contextData.put(TaggingConstants.COUNTRY_KEY, getCountry());
        contextData.put(TaggingConstants.LANGUAGE_KEY, getLanguage());
//        contextData.put(TaggingConstants.CURRENCY_KEY, getCurrency());

        contextData.put(TaggingConstants.APPSID_KEY, getAppsId());
        contextData.put(TaggingConstants.COMPONENT_ID, getComponentId());
        contextData.put(TaggingConstants.COMPONENT_VERSION, getComponentVersionVersionValue());
        contextData.put(TaggingConstants.LOCAL_TIMESTAMP_KEY, getLocalTimestamp());
        contextData.put(TaggingConstants.UTC_TIMESTAMP_KEY, getUTCTimestamp());
        if (null != getNewKey() && null != getNewValue()) {
//            contextData.put(getComponentVersionKey(), getComponentVersionVersionValue());

            if(!getNewKey().contains(",") && getNewValue().contains(",") )
            contextData.put(getNewKey(), getNewValue());
        }

//        if(overriden){
////            contextData.put(TaggingConstants.COUNTRY_KEY, getCountryOverriden());
//            contextData.put(TaggingConstants.LANGUAGE_KEY, getLanguageyOverridden());
////            contextData.put(TaggingConstants.CURRENCY_KEY, getCurrencyOverriden());
//
//            contextData.put(TaggingConstants.COMPONENT_VERSION, getComponentVersionVersionValue());
//            contextData.put(TaggingConstants.COMPONENT_ID, getComponentId());
//            contextData.put(TaggingConstants.TIMESTAMP_KEY, getTimeStampOverridden());
//            contextData.put(TaggingConstants.APPSID_KEY, getAppsIDkeyOverridden());
//        }

        return contextData;
    }
    private static String getAppsId(){
        if(mAppsIdkey == null){
            mAppsIdkey= Analytics.getTrackingIdentifier();
        }

        return mAppsIdkey;
    }

//    private static String getCountry(){
//        return mlocale.getCountry();
//    }

    private static void setNewKey(String newFieldkey) {
        Tagging.newFieldKey = newFieldkey;

    }
    private static void setNewValue(String newFieldvalue) {
        Tagging.newFieldValue = newFieldvalue;
    }
    private static String getNewKey(){
        return newFieldKey;
    }
    private static String getNewValue(){
        return newFieldValue;
    }
//    private static String getAppVersion() {
//        String appVersion = null;
//        try {
//            PackageInfo packageInfo = mcontext.getPackageManager().getPackageInfo(
//                    mcontext.getPackageName(), 0);
//
//            appVersion = packageInfo.versionName;
//        } catch (PackageManager.NameNotFoundException e) {
//
//        }
//        return appVersion;
//    }

    private static String getLanguage(){
        if(mLanguage == null){
            mLanguage = mlocale.getLanguage();
        }
        return mLanguage;

    }

//    private static String getCurrency() {
//        Currency currency = Currency.getInstance(mlocale);
//        String currencyCode = currency.getCurrencyCode();
//        if (currencyCode == null)
//            currencyCode = TaggingConstants.DEFAULT_CURRENCY;
//        return currencyCode;
//    }
//    private static String getCurrencyOverridden() {
//        return mCurruncy;
//    }
//    private static String getLanguageyOverridden() {
//        return mLanguage;
//
//    }
//    private static String getCountryOverridden() {
//        return mCountry;
//    }
//    private static void setCountryOverridden(String country) {
//        Tagging.mCountry = country;
//    }

    private static void setAppsIdkeyOverridden(String appsIdkey) {
        Tagging.mAppsIdkey = appsIdkey;
    }
    private static void setLocalTimeStampOverridden(String localTimestamp) {
        Tagging.mLocalTimestamp = localTimestamp;
    }
    private static void setUTCTimeStampOverridden(String UTCTimestamp) {
        Tagging.mUTCTimestamp = UTCTimestamp;
    }
    private static void setLanguageOverridden(String langue) {
        Tagging.mLanguage=langue;
    }
//    private static void setCurrencyOverridden(String currency) {
//        Tagging.mCurruncy=currency;
//    }
    private static String getUTCTimestamp() {

        if(mLocalTimestamp == null){
            DateFormat df = DateFormat.getTimeInstance();
            df.setTimeZone(TimeZone.getTimeZone("gmt"));
            String utcTime = df.format(new Date());
            mLocalTimestamp = utcTime;
        }


        return mLocalTimestamp;
    }

    private static String getLocalTimestamp() {


        if(mUTCTimestamp == null){
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formattedDate = df.format(c.getTime());
            mUTCTimestamp = formattedDate;
        }


        return mUTCTimestamp;
    }

//    public static String getTrackingIdentifier() {
//        return trackingIdentifier;
//    }
//
//    public static String getAppsIDkeyOverridden() {
//        return mAppsIdkey;
//    }

    public static void setTrackingIdentifier(String trackingIdentifier) {
        Tagging.trackingIdentifier = trackingIdentifier;
    }

    public static boolean isTaggingEnabled() {
        return isTagginEnabled;
    }

    public static void enableAppTagging(boolean isTagginEnabled) {
        Tagging.isTagginEnabled = isTagginEnabled;
    }

    public static String getComponentId() {
        if(componentVersionKey == null){
            componentVersionKey = "DefaultText";
        }
        return componentVersionKey;
    }

    public static void setComponentID(String componentID) {
        Tagging.componentVersionKey = componentID;
    }

    public static String getComponentVersionVersionValue() {
        if(componentVersionVersionValue == null){
            componentVersionVersionValue = "DefalutValue";
        }
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
//    public static void collectLifecycleData(Activity activity){
//        contextData = addAnalyticsDataObject(false);
//        Config.collectLifecycleData(activity,contextData);
//    }
}
