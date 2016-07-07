/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.tagging;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.adobe.mobile.Analytics;
import com.adobe.mobile.Config;
import com.adobe.mobile.MobilePrivacyStatus;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.GlobalStore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class AppTagging implements AIAppTaggingInterface {
    private static String componentVersionKey;

    private static String newFieldKey;

    private static String newFieldValue;

    private static String componentVersionVersionValue ;
    private static String mLanguage;
//    private static String mCountry;
    private static String mAppsIdkey;
    private static String mLocalTimestamp;
    private static String mUTCTimestamp;
    private static String prevPage;


    private AppInfra mAppInfra;
    protected String mComponentID;
    protected String mComponentVersion;
    Context context ;

    private static String[] defaultValues = {
            AIAppTaggingConstants.LANGUAGE_KEY,
            AIAppTaggingConstants.APPSID_KEY,
            AIAppTaggingConstants.COMPONENT_ID,
            AIAppTaggingConstants.COMPONENT_VERSION,

            AIAppTaggingConstants.UTC_TIMESTAMP_KEY


    };


    private static Locale mlocale;
    private static GlobalStore mGlobalStore;

    private static Context mcontext;

    private static String mAppName;
    private static Map<String, Object> contextData;

    public AppTagging(AppInfra aAppInfra) {
        mAppInfra = aAppInfra;
        init(Locale.getDefault(), mAppInfra.getAppInfraContext(), "TaggingPageInitialization");
        // Class shall not presume appInfra to be completely initialized at this point.
        // At any call after the constructor, appInfra can be presumed to be complete.

    }

    private void init(Locale locale, Context context,String appName){
        mlocale = locale;
        mcontext = context;
        prevPage = appName;
        Config.setContext(context);
        mGlobalStore = GlobalStore.getInstance();
        if(mGlobalStore.getValue()!=null){
            prevPage= mGlobalStore.getValue();
        }
        if(appName == null){
            throw new RuntimeException("Please set app name for tagging library");
        }
    }



    public void setDebuggable(final boolean enable){
        Config.setDebugLogging(enable);
    }

    private Map<String, Object> addAnalyticsDataObject() {
        Map<String, Object> contextData = new HashMap<String, Object>();

        contextData.put(AIAppTaggingConstants.LANGUAGE_KEY, getLanguage());
//        contextData.put(AIAppTaggingConstants.CURRENCY_KEY, getCurrency());

        contextData.put(AIAppTaggingConstants.APPSID_KEY, getAppsId());
        contextData.put(AIAppTaggingConstants.COMPONENT_ID, getComponentId());
        contextData.put(AIAppTaggingConstants.COMPONENT_VERSION, getComponentVersionVersionValue());
        contextData.put(AIAppTaggingConstants.LOCAL_TIMESTAMP_KEY, getLocalTimestamp());
        contextData.put(AIAppTaggingConstants.UTC_TIMESTAMP_KEY, getUTCTimestamp());
        if (null != getNewKey() && null != getNewValue()) {

            if(!getNewKey().contains(",") && !getNewValue().contains(",") ){
                contextData.put(getNewKey(), getNewValue());
            }

        }

        return contextData;
    }
    private String getAppsId(){
        if(mAppsIdkey == null){
            mAppsIdkey= Analytics.getTrackingIdentifier();
        }

        return mAppsIdkey;
    }

    private void setNewKey(String newFieldkey) {
        AppTagging.newFieldKey = newFieldkey;

    }
    private void setNewValue(String newFieldvalue) {
        AppTagging.newFieldValue = newFieldvalue;
    }
    private String getNewKey(){
        return newFieldKey;
    }
    private String getNewValue(){
        return newFieldValue;
    }

    private String getLanguage(){
        if(mLanguage == null){
            mLanguage = mlocale.getLanguage();
        }
        return mLanguage;

    }

    private String getUTCTimestamp() {
        String UTCtime = null;

        if(mAppInfra.getTimeSync() != null){
            UTCtime=mAppInfra.getTimeSync().getUTCTime();
            mUTCTimestamp = UTCtime;
            Log.i("mUTCTimestamp", ""+mUTCTimestamp);
        }

        if(mUTCTimestamp!=null){
            return mUTCTimestamp;
        }
        return mUTCTimestamp;
    }

    private String getLocalTimestamp() {


            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS a");
            String formattedDate = df.format(c.getTime());
            mLocalTimestamp = formattedDate;


        if(mLocalTimestamp != null){
            return mLocalTimestamp;
        }
        return mLocalTimestamp;
    }

    private String getComponentId() {
        if(mComponentID == null){
            mComponentID = "DefaultText";
        }
        return mComponentID;
    }

    private String getComponentVersionVersionValue() {
        if(mComponentVersion == null){
            mComponentVersion = "DefalutValue";
        }
        return mComponentVersion;
    }

    @Override
    public AIAppTaggingInterface createInstanceForComponent(String componentId, String componentVersion) {
        return new AppTaggingWrapper(mAppInfra, componentId, componentVersion);
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
    public void setPreviousPage(String previousPage) {
        prevPage = previousPage;
        mGlobalStore.setValue(prevPage);
    }
    @Override
    public MobilePrivacyStatus getPrivacyConsent() {
        return Config.getPrivacyStatus();
    }


    @Override
    public void trackPageWithInfo(String pageName, String key, String value) {

        track(pageName, key, value, null);
    }
    private void track(String pageName, String key, String value, Map<String, String> paramMap){

        if(key!=null && value!=null){
            if(!Arrays.asList(defaultValues).contains(key)){

                setNewKey(key);
                setNewValue(value);
            }
        }else{
            for (Map.Entry<String, String> entry : paramMap.entrySet()) {
                if (!Arrays.asList(defaultValues).contains(entry.getKey())) {

                    setNewKey(entry.getKey());
                    setNewValue(entry.getValue());
                    contextData = addAnalyticsDataObject();
                }
            }
        }

        contextData = addAnalyticsDataObject();
        if (null != prevPage) {
            contextData.put(AIAppTaggingConstants.PREVIOUS_PAGE_NAME, prevPage);
        }
        Analytics.trackState(pageName, contextData);

        prevPage = pageName;
    }

    @Override
    public void trackPageWithInfo(String pageName, Map<String, String> paramMap) {

       track(pageName, null, null, paramMap);

    }


    @Override
    public void trackActionWithInfo(String pageName, String key, String value) {

        track(pageName, key, value, null);

        }


    @Override
    public void trackActionWithInfo(String pageName, Map<String, String> paramMap) {
        track(pageName, null, null, paramMap);
    }
    @Override
    public void collectLifecycleInfo(Activity context, Map<String, Object> paramDict) {
        Config.collectLifecycleData(context, paramDict);
    }

    @Override
    public void collectLifecycleInfo(Activity context) {
        Config.collectLifecycleData(context);
    }

    @Override
    public void pauseLifecycleInfo() {
        Config.pauseCollectingLifecycleData();
    }

}
