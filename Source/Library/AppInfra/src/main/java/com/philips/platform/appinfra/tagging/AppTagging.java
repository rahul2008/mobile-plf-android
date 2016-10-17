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
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.securestorage.SecureStorage;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;
import com.philips.platform.appinfra.timesync.TimeSyncSntpClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class AppTagging implements AppTaggingInterface {

    private String mLanguage;
    private String prevPage;

    AppInfra mAppInfra;
    protected String mComponentID;
    protected String mComponentVersion;

    private String[] defaultValues = {
            AppTaggingConstants.LANGUAGE_KEY,
            AppTaggingConstants.APPSID_KEY,
            AppTaggingConstants.COMPONENT_ID,
            AppTaggingConstants.COMPONENT_VERSION,

            AppTaggingConstants.UTC_TIMESTAMP_KEY,
            AppTaggingConstants.BUNDLE_ID


    };


    private Locale mlocale;

    private Context mcontext;

    private Map<String, Object> contextData;

    private AppConfigurationInterface.AppConfigurationError configError;
    private boolean sslValue = false;
    private String consentValueString = null;

    SecureStorageInterface ssi;
    SecureStorage.SecureStorageError mSecureStorageError;
    private final static String AIL_PRIVACY_CONSENT = "ailPrivacyConsentForSensitiveData";


    public AppTagging(AppInfra aAppInfra) {
        mAppInfra = aAppInfra;

        ssi = mAppInfra.getSecureStorage();
        mSecureStorageError = new SecureStorage.SecureStorageError();
        init(mAppInfra.getInternationalization().getUILocale(), mAppInfra.getAppInfraContext(), "TaggingPageInitialization");


        configError = new AppConfigurationInterface
                .AppConfigurationError();
        // Class shall not presume appInfra to be completely initialized at this point.
        // At any call after the constructor, appInfra can be presumed to be complete.


    }

    private boolean checkforSSLconnection() {

        if (sslValue == false) {
            JSONObject jSONObject = getMasterADBMobileConfig();

            try {
                sslValue = jSONObject.getJSONObject("analytics").optBoolean("ssl");
                if (jSONObject != null && sslValue) {
                    mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.VERBOSE, "ssl value",
                            "true");
                    return true;
                } else {
                    throw new AssertionError("ssl value in ADBMobileConfig.json should be true");
                }
            } catch (JSONException e) {
                mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, "AdobeMobile Configuration exception",
                        Log.getStackTraceString(e));
            }

            return sslValue;
        }
        return sslValue;
    }

    private void init(Locale locale, Context context, String appName) {
        mlocale = locale;
        mcontext = context;
        prevPage = appName;
        Config.setContext(context);

    }

    protected JSONObject getMasterADBMobileConfig() {
        JSONObject result = null;
        try {
            InputStream mInputStream = mAppInfra.getAppInfraContext().getAssets().open("ADBMobileConfig.json");
            BufferedReader r = new BufferedReader(new InputStreamReader(mInputStream));
            StringBuilder total = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                total.append(line).append('\n');
            }
            result = new JSONObject(total.toString());
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.VERBOSE, "Json",
                    result.toString());

        } catch (Exception e) {
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, "AppConfiguration exception",
                    Log.getStackTraceString(e));
        }
        return result;
    }

    private Map<String, Object> addAnalyticsDataObject() {
        Map<String, Object> contextData = new HashMap<String, Object>();

        contextData.put(AppTaggingConstants.LANGUAGE_KEY, getLanguage());

        contextData.put(AppTaggingConstants.APPSID_KEY, getAppsId());
        if (getComponentId() != null) {
            contextData.put(AppTaggingConstants.COMPONENT_ID, getComponentId());
        }
        if (getComponentVersionVersionValue() != null) {
            contextData.put(AppTaggingConstants.COMPONENT_VERSION, getComponentVersionVersionValue());
        }
        contextData.put(AppTaggingConstants.LOCAL_TIMESTAMP_KEY, getLocalTimestamp());
        contextData.put(AppTaggingConstants.UTC_TIMESTAMP_KEY, getUTCTimestamp());
        contextData.put(AppTaggingConstants.BUNDLE_ID, getAppStateFromConfig());

        // Removes Sensitive data from Context object taking Consent

        return removeSensitiveData(contextData);
    }

    private Map<String, Object> removeSensitiveData(Map<String, Object> data) {
        if (getPrivacyConsentForSensitiveData()) {
            ArrayList<String> taggingSensitiveData = (ArrayList) mAppInfra.getConfigInterface().getPropertyForKey("tagging.sensitiveData", "appinfra", configError);

            if (taggingSensitiveData != null && taggingSensitiveData.size() > 0) {
                data.keySet().removeAll(taggingSensitiveData);

            }
            return data;
        }

        return data;
    }

    private String getAppsId() {
        return Analytics.getTrackingIdentifier();
    }

    private String getAppStateFromConfig() {
        return mAppInfra.getAppIdentity().getAppState().toString();
    }

    private String getLanguage() {
        if (mLanguage == null) {
            mLanguage = mlocale.getLanguage();
        }
        return mLanguage;

    }

    private String getUTCTimestamp() {
        String mUTCTimestamp = null;
        String UTCtime = null;

        if (mAppInfra.getTime() != null) {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS a", Locale.ENGLISH);
            df.setTimeZone(TimeZone.getTimeZone(TimeSyncSntpClient.UTC));

            UTCtime = df.format(mAppInfra.getTime().getUTCTime());
            mUTCTimestamp = UTCtime;
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR,
                    "Tagging", mUTCTimestamp);

        }

        return mUTCTimestamp;
    }

    private String getLocalTimestamp() {

        String mLocalTimestamp = null;
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS a", Locale.ENGLISH);
        String formattedDate = df.format(c.getTime());
        mLocalTimestamp = formattedDate;
        return mLocalTimestamp;
    }

    private String getComponentId() {
//        if (mComponentID == null) {
//            mComponentID = "DefaultText";
//        }
        return mComponentID;
    }

    private String getComponentVersionVersionValue() {
//        if (mComponentVersion == null) {
//            mComponentVersion = "DefaultValue";
//        }
        return mComponentVersion;
    }

    @Override
    public AppTaggingInterface createInstanceForComponent(String componentId, String componentVersion) {
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
    }

    @Override
    public PrivacyStatus getPrivacyConsent() {

        MobilePrivacyStatus mMobilePrivacyStatus = Config.getPrivacyStatus();
        PrivacyStatus mPrivacyStatus = null;
        switch (mMobilePrivacyStatus) {
            case MOBILE_PRIVACY_STATUS_OPT_IN:
                mPrivacyStatus = PrivacyStatus.OPTIN;
                break;
            case MOBILE_PRIVACY_STATUS_OPT_OUT:
                mPrivacyStatus = PrivacyStatus.OPTOUT;
                break;
            case MOBILE_PRIVACY_STATUS_UNKNOWN:
                mPrivacyStatus = PrivacyStatus.UNKNOWN;
                break;

        }

        return mPrivacyStatus;
    }


    private void track(String pageName, Map<String, String> paramMap, boolean isTrackPage) {
        if (checkforAppStateandSSl()) {
            trackData(pageName, paramMap, isTrackPage);
        }
        trackData(pageName, paramMap, isTrackPage);

    }

    private boolean checkforAppStateandSSl() {
        if (mAppInfra.getAppIdentity().getAppState() != null && mAppInfra.getAppIdentity().getAppState().toString().equalsIgnoreCase("Production") && checkforSSLconnection()) {
            return true;
        }
        return false;
    }

    private void trackData(String pageName, Map<String, String> paramMap, boolean isTrackPage) {
        Map<String, Object> contextData = new HashMap<String, Object>();
        contextData = addAnalyticsDataObject();
        if (paramMap != null) {
            paramMap.putAll((Map)contextData);
            contextData = removeSensitiveData((Map)paramMap);
//            for (Map.Entry<String, String> entry : paramMap.entrySet()) {
//                if (!Arrays.asList(defaultValues).contains(entry.getKey())) {
//                    contextData.put(entry.getKey(), entry.getValue());
//                }
//            }
        }
//        contextData = removeSensitiveData(contextData);
        if (null != prevPage && isTrackPage) {
            contextData.put(AppTaggingConstants.PREVIOUS_PAGE_NAME, prevPage);
        }
        if (isTrackPage) {
            Analytics.trackState(pageName, contextData);
            prevPage = pageName;
        } else {
            Analytics.trackAction(pageName, contextData);
        }

    }

    @Override
    public void trackTimedActionStart(String actionStart) {

        if (checkforAppStateandSSl()) {
            Analytics.trackTimedActionStart(actionStart, contextData);
        } else {
            Analytics.trackTimedActionStart(actionStart, contextData);
        }


    }


    @Override
    public void trackTimedActionEnd(String actionEnd) {
        if (checkforAppStateandSSl()) {
            Analytics.trackTimedActionEnd(actionEnd, null);
        } else {
            Analytics.trackTimedActionEnd(actionEnd, null);
        }


    }

    // Sets the value of Privacy Consent For Sensitive Data and stores in preferences
    @Override
    public void setPrivacyConsentForSensitiveData(boolean valueContent) {
        ssi.storeValueForKey(AIL_PRIVACY_CONSENT, String.valueOf(valueContent), mSecureStorageError);
    }

    @Override
    public boolean getPrivacyConsentForSensitiveData() {
        boolean consentValue;
        consentValueString = ssi.fetchValueForKey(AIL_PRIVACY_CONSENT, mSecureStorageError);
        if (consentValueString != null && consentValueString.equalsIgnoreCase("true")) {
            consentValue = true;
        } else {
            consentValue = false;
        }
        return consentValue;
    }

//    private Analytics.TimedActionBlock trackTimedActionBlock(final String actionItemKey, final String actionItemValue) {
//        Analytics.TimedActionBlock<Boolean> timedActionBlock = new Analytics.TimedActionBlock<Boolean>() {
//            @Override
//            public Boolean call(long l, long l1, Map<String, Object> map) {
//                if (actionItemKey != null && actionItemValue != null) {
//
//                    map.put(actionItemKey, actionItemValue);
//                    return true;
//                }
//                return false;
//            }
//        };
//
//        return timedActionBlock;
//    }


    @Override
    public void trackPageWithInfo(String pageName, String key, String value) {

        Map<String, String> trackMap = new HashMap<String, String>();
        trackMap.put(key, value);
        track(pageName, trackMap, true);
    }

    @Override
    public void trackPageWithInfo(String pageName, Map<String, String> paramMap) {

        track(pageName, paramMap, true);

    }


    @Override
    public void trackActionWithInfo(String pageName, String key, String value) {

        Map<String, String> trackMap = new HashMap<String, String>();
        trackMap.put(key, value);
        track(pageName, trackMap, false);

    }


    @Override
    public void trackActionWithInfo(String pageName, Map<String, String> paramMap) {

        track(pageName, paramMap, false);
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

    @Override
    public void trackVideoStart(String videoName) {
        trackActionWithInfo("videoStart", "videoName", videoName);
    }

    @Override
    public void trackVideoEnd(String videoName) {
        trackActionWithInfo("videoEnd", "videoName", videoName);
    }

    @Override
    public void trackSocialSharing(SocialMedium medium, String sharedItem) {
        Map<String, String> trackMap = new HashMap<String, String>();
        trackMap.put("socialItem", sharedItem);
        trackMap.put("socialType", medium.toString());
        trackActionWithInfo("socialShare", trackMap);
    }

    @Override
    public void trackLinkExternal(String url) {
        trackActionWithInfo("sendData", "exitLinkName", url);
    }

    @Override
    public void trackFileDownload(String filename) {
        trackActionWithInfo("sendData", "fileName", filename);
    }


}
