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
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class AppTagging implements AppTaggingInterface {

    private String mLanguage;
    private String prevPage;

    private final AppInfra mAppInfra;
    protected String mComponentID;
    protected String mComponentVersion;

    private Locale mLocale;

    private final AppConfigurationInterface.AppConfigurationError configError;

    private final SecureStorageInterface ssi;
    private final SecureStorage.SecureStorageError mSecureStorageError;
    private final static String AIL_PRIVACY_CONSENT = "ailPrivacyConsentForSensitiveData";


    public AppTagging(AppInfra aAppInfra) {
        mAppInfra = aAppInfra;

        ssi = mAppInfra.getSecureStorage();
        mSecureStorageError = new SecureStorage.SecureStorageError();
        init(mAppInfra.getInternationalization().getUILocale(), mAppInfra.getAppInfraContext());


        configError = new AppConfigurationInterface
                .AppConfigurationError();
        // Class shall not presume appInfra to be completely initialized at this point.
        // At any call after the constructor, appInfra can be presumed to be complete.


    }

    /*
    * Checks for SSL connection value from Adobe json
    * */

    private boolean checkForSslConnection() {
        boolean sslValue = false;

        JSONObject jSONObject = getMasterADBMobileConfig();

        try {
            if (jSONObject != null) {
                sslValue = jSONObject.getJSONObject("analytics").optBoolean("ssl");
                if (sslValue) {
                    mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.VERBOSE, "ssl value",
                            "true");
                    return true;
                } else {
                    if(!checkForProductionState())
                    throw new AssertionError("ssl value in ADBMobileConfig.json should be true");
                }
            }
        } catch (JSONException e) {
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, "AdobeMobile Configuration exception",
                    Log.getStackTraceString(e));
        }

        return sslValue;
    }

    private void init(Locale locale, Context context) {
        mLocale = locale;
        Config.setContext(context);

    }

    /**
     * Reading from Adobe json
     */
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

    /**
     * Constructing default Object data
     **/

    private Map<String, Object> addAnalyticsDataObject() {
        Map<String, Object> contextData = new HashMap<>();

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

    /**
     * Removes Sensitive data declared in Adobe json
     **/

    private Map<String, Object> removeSensitiveData(Map<String, Object> data) {
        if (getPrivacyConsentForSensitiveData()) {
            if (mAppInfra.getConfigInterface() != null) {
                try {
                    Object object = mAppInfra.getConfigInterface().getPropertyForKey
                            ("tagging.sensitiveData", "appinfra", configError);
                    if (object instanceof ArrayList) {
                        ArrayList<String> taggingSensitiveData = (ArrayList<String>) object;
                        if (taggingSensitiveData.size() > 0) {
                            data.keySet().removeAll(taggingSensitiveData);
                        }
                    }
                } catch (Exception e) {
                    mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR,
                            "Tagging", "" + e);
                }
            }
            return data;
        }

        return data;
    }

    private String getAppsId() {
        return Analytics.getTrackingIdentifier();
    }

    private String getAppStateFromConfig() {

        if (mAppInfra.getAppIdentity() != null) {
            try {
                return mAppInfra.getAppIdentity().
                        getAppState().toString();
            } catch (Exception e) {
                mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR,
                        "Tagging", "" + e);
            }
        }

        return null;
    }

    private String getLanguage() {
        if (mLanguage == null) {
            mLanguage = mLocale.getLanguage();
        }
        return mLanguage;

    }

    private String getUTCTimestamp() {
        String mUTCTimestamp = null;
        String UTCtime;

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

        String mLocalTimestamp;
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS a", Locale.ENGLISH);
        mLocalTimestamp = df.format(c.getTime());
        return mLocalTimestamp;
    }

    private String getComponentId() {
        return mComponentID;
    }

    private String getComponentVersionVersionValue() {
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
        if (checkForSslConnection()) {
            trackData(pageName, paramMap, isTrackPage);
        } else if (checkForProductionState()) {
            trackData(pageName, paramMap, isTrackPage);
        }
    }

    private boolean checkForProductionState() {

        if (mAppInfra.getAppIdentity() != null) {
            try {
                return !mAppInfra.getAppIdentity().
                        getAppState().toString().equalsIgnoreCase("Production");
            } catch (Exception e) {
                mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR,
                        "Tagging", "" + e);
            }
        }

        return false;
    }

    private void trackData(String pageName, Map<String, String> paramMap, boolean isTrackPage) {
        Map<String, Object> contextData;
        contextData = addAnalyticsDataObject();
        if (paramMap != null) {
            paramMap.putAll((Map) contextData);
            contextData = removeSensitiveData((Map) paramMap);
        }
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

        if (checkForSslConnection()) {
            Analytics.trackTimedActionStart(actionStart, addAnalyticsDataObject());
        } else if (checkForProductionState()) {
            Analytics.trackTimedActionStart(actionStart, addAnalyticsDataObject());
        }
    }

    @Override
    public void trackTimedActionEnd(String actionEnd) {
        if (checkForSslConnection()) {
            Analytics.trackTimedActionEnd(actionEnd, null);
        } else if (checkForProductionState()) {
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
        String consentValueString = ssi.fetchValueForKey(AIL_PRIVACY_CONSENT, mSecureStorageError);
        consentValue = consentValueString != null && consentValueString.equalsIgnoreCase("true");
        return consentValue;
    }


    @Override
    public void trackPageWithInfo(String pageName, String key, String value) {

        Map<String, String> trackMap = new HashMap<>();
        trackMap.put(key, value);
        track(pageName, trackMap, true);
    }

    @Override
    public void trackPageWithInfo(String pageName, Map<String, String> paramMap) {

        track(pageName, paramMap, true);

    }


    @Override
    public void trackActionWithInfo(String pageName, String key, String value) {

        Map<String, String> trackMap = new HashMap<>();
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
        Map<String, String> trackMap = new HashMap<>();
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
