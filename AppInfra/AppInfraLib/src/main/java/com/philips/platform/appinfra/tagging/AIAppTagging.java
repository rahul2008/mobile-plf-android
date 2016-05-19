
package com.philips.platform.appinfra.tagging;

import android.content.Context;

import com.adobe.mobile.Analytics;
import com.adobe.mobile.Config;
import com.adobe.mobile.MobilePrivacyStatus;
import com.philips.platform.appinfra.AppInfra;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class AIAppTagging implements AIAppTaggingInterface {
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


    private AppInfra mAppInfra;
    protected String mComponentID;
    protected String mComponentVersion;

    private static String[] defaultValues = {
            AIAppTaggingConstants.LANGUAGE_KEY,
            AIAppTaggingConstants.APPSID_KEY,
            AIAppTaggingConstants.COMPONENT_ID,
            AIAppTaggingConstants.COMPONENT_VERSION,
            AIAppTaggingConstants.LOCAL_TIMESTAMP_KEY,


    };


//    private static String mCurruncy;

    private static Locale mlocale;

    private static Context mcontext;

    private static String mAppName;
    private static Map<String, Object> contextData;

    public AIAppTagging(AppInfra aAppInfra) {
        mAppInfra = aAppInfra;
        // Class shall not presume appInfra to be completely initialized at this point.
        // At any call after the constructor, appInfra can be presumed to be complete.

    }

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

//    public static void trackPage(String currPage,String prevPage, String key, String value) {
//        validateAppTaggingInitialization();
//        if (!isTaggingEnabled()) {
//            return;
//        }
//
//        if(Arrays.asList(defaultValues).contains(key)){
//
//            switch (key){
//                case AIAppTaggingConstants.LANGUAGE_KEY:
//                    contextData = addAnalyticsDataObject();
//                    contextData.put(AIAppTaggingConstants.LANGUAGE_KEY, value);
//                    setLanguageOverridden(value);
//                    break;
//                case AIAppTaggingConstants.APPSID_KEY:
//                    contextData = addAnalyticsDataObject();
//                    contextData.put(AIAppTaggingConstants.APPSID_KEY, value);
//                    setAppsIdkeyOverridden(value);
//                    break;
//                case AIAppTaggingConstants.LOCAL_TIMESTAMP_KEY:
//                    contextData = addAnalyticsDataObject();
//                    contextData.put(AIAppTaggingConstants.LOCAL_TIMESTAMP_KEY, value);
//                    setLocalTimeStampOverridden(value);
//                    break;
//                case AIAppTaggingConstants.UTC_TIMESTAMP_KEY:
//                    contextData = addAnalyticsDataObject();
//                    contextData.put(AIAppTaggingConstants.UTC_TIMESTAMP_KEY, value);
//                    setUTCTimeStampOverridden(value);
//                    break;
//                case AIAppTaggingConstants.COMPONENT_ID:
//                    contextData = addAnalyticsDataObject();
//                    contextData.put(AIAppTaggingConstants.COMPONENT_ID, value);
//                    setComponentID(value);
//                    break;
//                case AIAppTaggingConstants.COMPONENT_VERSION:
//                    contextData = addAnalyticsDataObject();
//                    contextData.put(AIAppTaggingConstants.COMPONENT_VERSION, value);
//                    setComponentVersionVersionValue(value);
//                    break;
//
//            }
//
//
//        }else{
//            setNewKey(key);
//            setNewValue(value);
//            contextData = addAnalyticsDataObject();
//        }
//        if (null != prevPage) {
//            contextData.put(AIAppTaggingConstants.PREVIOUS_PAGE_NAME, prevPage);
//        }
//        Analytics.trackState(currPage, contextData);
//
//    }

    public static void setDebuggable(final boolean enable){
        Config.setDebugLogging(enable);
    }

//    public static void setPrivacyStatus(AIAppTaggingWrapper.AIATMobilePrivacyStatus privacyStatus){
//        switch (privacyStatus) {
//            case MOBILE_PRIVACY_STATUS_OPT_IN:
//                Config.setPrivacyStatus(MobilePrivacyStatus.MOBILE_PRIVACY_STATUS_OPT_IN);
//                break;
//            case MOBILE_PRIVACY_STATUS_OPT_OUT:
//                Config.setPrivacyStatus(MobilePrivacyStatus.MOBILE_PRIVACY_STATUS_OPT_OUT);
//
//                break;
//            case MOBILE_PRIVACY_STATUS_UNKNOWN:
//                Config.setPrivacyStatus(MobilePrivacyStatus.MOBILE_PRIVACY_STATUS_UNKNOWN);
//                break;
//
//        }
//
//
//    }
    public static void getPrivacyStatus(){
        Config.getPrivacyStatus();
    }


    private static void validateAppTaggingInitialization(){
        if(mlocale == null && mcontext == null && isTaggingEnabled()){
            throw new RuntimeException("Please initialize AIAppTagging library by calling its init method");
        }
    }




//    public static void trackAction(String state, String key, Object value) {
//        validateAppTaggingInitialization();
//        if (!isTaggingEnabled()) {
//            return;
//        }
//        Map<String, Object> contextData = addAnalyticsDataObject();
//        if (null != key) {
//
//            contextData.put(key, value);
//        }
//        Analytics.trackAction(state, contextData);
//    }

//    public static void trackMultipleActions(String state, Map<String, Object> map) {
//        validateAppTaggingInitialization();
//        if (!isTaggingEnabled()) {
//            return;
//        }
//        Map<String, Object> contextData = addAnalyticsDataObject();
//
//        Map tmp = new HashMap(map);
//        tmp.keySet().removeAll(contextData.keySet());
////        target.putAll(tmp);
//        contextData.putAll(map);
//        Analytics.trackAction(state, contextData);
//    }

//    public static void trackMultipleState(String state, Map<String, Object> map) {
//        validateAppTaggingInitialization();
//        if (!isTaggingEnabled()) {
//            return;
//        }
//        Map<String, Object> contextData = addAnalyticsDataObject();
//        contextData.putAll(map);
//        Analytics.trackState(state, contextData);
//    }

    private static Map<String, Object> addAnalyticsDataObject() {
        Map<String, Object> contextData = new HashMap<String, Object>();

//        contextData.put(AIAppTaggingConstants.CP_KEY, AIAppTaggingConstants.CP_VALUE);
//        contextData.put(AIAppTaggingConstants.APPNAME_KEY, mAppName);
//        contextData.put(AIAppTaggingConstants.VERSION_KEY, getAppVersion());
//        contextData.put(AIAppTaggingConstants.OS_KEY, AIAppTaggingConstants.OS_ANDROID);
//        contextData.put(AIAppTaggingConstants.COUNTRY_KEY, getCountry());
        contextData.put(AIAppTaggingConstants.LANGUAGE_KEY, getLanguage());
//        contextData.put(AIAppTaggingConstants.CURRENCY_KEY, getCurrency());

        contextData.put(AIAppTaggingConstants.APPSID_KEY, getAppsId());
        contextData.put(AIAppTaggingConstants.COMPONENT_ID, getComponentId());
        contextData.put(AIAppTaggingConstants.COMPONENT_VERSION, getComponentVersionVersionValue());
        contextData.put(AIAppTaggingConstants.LOCAL_TIMESTAMP_KEY, getLocalTimestamp());
        contextData.put(AIAppTaggingConstants.UTC_TIMESTAMP_KEY, getUTCTimestamp());
        if (null != getNewKey() && null != getNewValue()) {
//            contextData.put(getComponentVersionKey(), getComponentVersionVersionValue());

            if(!getNewKey().contains(",") && getNewValue().contains(",") )
            contextData.put(getNewKey(), getNewValue());
        }

//        if(overriden){
////            contextData.put(AIAppTaggingConstants.COUNTRY_KEY, getCountryOverriden());
//            contextData.put(AIAppTaggingConstants.LANGUAGE_KEY, getLanguageyOverridden());
////            contextData.put(AIAppTaggingConstants.CURRENCY_KEY, getCurrencyOverriden());
//
//            contextData.put(AIAppTaggingConstants.COMPONENT_VERSION, getComponentVersionVersionValue());
//            contextData.put(AIAppTaggingConstants.COMPONENT_ID, getComponentId());
//            contextData.put(AIAppTaggingConstants.TIMESTAMP_KEY, getTimeStampOverridden());
//            contextData.put(AIAppTaggingConstants.APPSID_KEY, getAppsIDkeyOverridden());
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
        AIAppTagging.newFieldKey = newFieldkey;

    }
    private static void setNewValue(String newFieldvalue) {
        AIAppTagging.newFieldValue = newFieldvalue;
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
//            currencyCode = AIAppTaggingConstants.DEFAULT_CURRENCY;
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
//        AIAppTagging.mCountry = country;
//    }

    private static void setAppsIdkeyOverridden(String appsIdkey) {
        AIAppTagging.mAppsIdkey = appsIdkey;
    }
    private static void setLocalTimeStampOverridden(String localTimestamp) {
        AIAppTagging.mLocalTimestamp = localTimestamp;
    }
    private static void setUTCTimeStampOverridden(String UTCTimestamp) {
        AIAppTagging.mUTCTimestamp = UTCTimestamp;
    }
    private static void setLanguageOverridden(String langue) {
        AIAppTagging.mLanguage=langue;
    }
//    private static void setCurrencyOverridden(String currency) {
//        AIAppTagging.mCurruncy=currency;
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
        AIAppTagging.trackingIdentifier = trackingIdentifier;
    }

    public static boolean isTaggingEnabled() {
        return isTagginEnabled;
    }

    public static void enableAppTagging(boolean isTagginEnabled) {
        AIAppTagging.isTagginEnabled = isTagginEnabled;
    }

    public static String getComponentId() {
        if(componentVersionKey == null){
            componentVersionKey = "DefaultText";
        }
        return componentVersionKey;
    }

    public static void setComponentID(String componentID) {
        AIAppTagging.componentVersionKey = componentID;
    }

    public static String getComponentVersionVersionValue() {
        if(componentVersionVersionValue == null){
            componentVersionVersionValue = "DefalutValue";
        }
        return componentVersionVersionValue;
    }

    public static void setComponentVersionVersionValue(String componentVersionVersionValue) {
        AIAppTagging.componentVersionVersionValue = componentVersionVersionValue;
    }

    public static String getLaunchingPageName() {
        return launchingPageName;
    }

    public static void setLaunchingPageName(String launchingPageName) {
        AIAppTagging.launchingPageName = launchingPageName;
    }

    public void pauseCollectingLifecycleData(){
        Config.pauseCollectingLifecycleData();
    }

    @Override
    public AIAppTaggingInterface createInstanceForComponent(String componentId, String componentVersion) {
        return new AIAppTaggingWrapper(mAppInfra, componentId, componentVersion);
    }

    @Override
    public void configureAnalyticsWithFilePath(String configFilePath) {

    }

    @Override
    public void setPrivacyConsent(PrivacyStatus privacyStatus) {
        switch (privacyStatus) {
            case OPTIN:
                Config.setPrivacyStatus(MobilePrivacyStatus.MOBILE_PRIVACY_STATUS_OPT_IN);
                break;
            case OPTOUT:
                Config.setPrivacyStatus(MobilePrivacyStatus.MOBILE_PRIVACY_STATUS_OPT_OUT);

                break;
            case UNKNOWN:
                Config.setPrivacyStatus(MobilePrivacyStatus.MOBILE_PRIVACY_STATUS_UNKNOWN);
                break;

        }


    }

    @Override
    public PrivacyStatus getPrivacyConsent() {
        return null;
    }

    @Override
    public void trackPageWithInfo(String pageName, String key, String value) {
        validateAppTaggingInitialization();
        if (!isTaggingEnabled()) {
            return;
        }

        if(Arrays.asList(defaultValues).contains(key)){

            switch (key){
                case AIAppTaggingConstants.LANGUAGE_KEY:
                    contextData = addAnalyticsDataObject();
                    contextData.put(AIAppTaggingConstants.LANGUAGE_KEY, value);
                    setLanguageOverridden(value);
                    break;
                case AIAppTaggingConstants.APPSID_KEY:
                    contextData = addAnalyticsDataObject();
                    contextData.put(AIAppTaggingConstants.APPSID_KEY, value);
                    setAppsIdkeyOverridden(value);
                    break;
                case AIAppTaggingConstants.LOCAL_TIMESTAMP_KEY:
                    contextData = addAnalyticsDataObject();
                    contextData.put(AIAppTaggingConstants.LOCAL_TIMESTAMP_KEY, value);
                    setLocalTimeStampOverridden(value);
                    break;
                case AIAppTaggingConstants.UTC_TIMESTAMP_KEY:
                    contextData = addAnalyticsDataObject();
                    contextData.put(AIAppTaggingConstants.UTC_TIMESTAMP_KEY, value);
                    setUTCTimeStampOverridden(value);
                    break;
                case AIAppTaggingConstants.COMPONENT_ID:
                    contextData = addAnalyticsDataObject();
                    contextData.put(AIAppTaggingConstants.COMPONENT_ID, value);
                    setComponentID(value);
                    break;
                case AIAppTaggingConstants.COMPONENT_VERSION:
                    contextData = addAnalyticsDataObject();
                    contextData.put(AIAppTaggingConstants.COMPONENT_VERSION, value);
                    setComponentVersionVersionValue(value);
                    break;

            }


        }else{
            setNewKey(key);
            setNewValue(value);
            contextData = addAnalyticsDataObject();
        }
//        if (null != prevPage) {
//            contextData.put(AIAppTaggingConstants.PREVIOUS_PAGE_NAME, prevPage);
//        }
        Analytics.trackState(pageName, contextData);

    }

    @Override
    public void trackPageWithInfo(String pageName, Map<String, String> paramMap) {
        validateAppTaggingInitialization();
        if (!isTaggingEnabled()) {
            return;
        }
        Map<String, Object> contextData = addAnalyticsDataObject();
        contextData.putAll(paramMap);
        Analytics.trackState(pageName, contextData);

    }

    @Override
    public void trackActionWithInfo(String pageName, String key, String value) {
        validateAppTaggingInitialization();
        if (!isTaggingEnabled()) {
            return;
        }
        Map<String, Object> contextData = addAnalyticsDataObject();
        if (null != key) {

            contextData.put(key, value);
        }
        Analytics.trackAction(pageName, contextData);
    }

    @Override
    public void trackActionWithInfo(String pageName, Map<String, String> paramMap) {
        validateAppTaggingInitialization();
        if (!isTaggingEnabled()) {
            return;
        }
        Map<String, Object> contextData = addAnalyticsDataObject();

        Map tmp = new HashMap(paramMap);
        tmp.keySet().removeAll(contextData.keySet());
//        target.putAll(tmp);
        contextData.putAll(paramMap);
        Analytics.trackAction(pageName, contextData);

    }

    public void collectLifecycleData(){
        Config.collectLifecycleData();
    }
//    public static void collectLifecycleData(Activity activity){
//        contextData = addAnalyticsDataObject(false);
//        Config.collectLifecycleData(activity,contextData);
//    }
}
