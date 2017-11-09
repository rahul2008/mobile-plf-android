/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.tagging;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.adobe.mobile.Analytics;
import com.adobe.mobile.Config;
import com.adobe.mobile.MobilePrivacyStatus;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraLogEventID;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.securestorage.SecureStorage;
import com.philips.platform.appinfra.timesync.TimeSyncSntpClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import static com.adobe.mobile.Analytics.getTrackingIdentifier;
import static com.adobe.mobile.MobilePrivacyStatus.MOBILE_PRIVACY_STATUS_OPT_IN;
import static com.adobe.mobile.MobilePrivacyStatus.MOBILE_PRIVACY_STATUS_OPT_OUT;
import static com.adobe.mobile.MobilePrivacyStatus.MOBILE_PRIVACY_STATUS_UNKNOWN;
import static com.philips.platform.appinfra.tagging.AppTagging.ACTION_NAME;
import static com.philips.platform.appinfra.tagging.AppTagging.ACTION_TAGGING_DATA;
import static com.philips.platform.appinfra.tagging.AppTagging.AIL_PRIVACY_CONSENT;
import static com.philips.platform.appinfra.tagging.AppTagging.EXTRA_TAGGING_DATA;
import static com.philips.platform.appinfra.tagging.AppTagging.PAGE_NAME;
import static com.philips.platform.appinfra.tagging.AppTaggingInterface.PrivacyStatus.OPTIN;
import static com.philips.platform.appinfra.tagging.AppTaggingInterface.PrivacyStatus.OPTOUT;
import static com.philips.platform.appinfra.tagging.AppTaggingInterface.PrivacyStatus.UNKNOWN;


/**
 * A Wrapper class forAppTaggingHandler.
 */
 public class AppTaggingHandler {
    private static String prevPage;
    private final AppInfra mAppInfra;
    private String mLanguage;
    private String mComponentID;
    private String mComponentVersion;

    public AppTaggingHandler(AppInfra aAppInfra) {
        mAppInfra = aAppInfra;
    }

    /*
    * Checks for SSL connection value from Adobe json
    * */
    protected boolean checkForSslConnection() {
        boolean sslValue = false;
        final JSONObject jSONObject = getMasterADBMobileConfig();
        try {
            if (jSONObject != null) {
                sslValue = jSONObject.getJSONObject("analytics").optBoolean("ssl");
                if (sslValue) {
                    mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.DEBUG, AppInfraLogEventID.AI_TAGGING, "ssl value true");
                    return sslValue;
                } else if (!checkForProductionState()) {
                    throw new AssertionError("ssl value in ADBMobileConfig.json should be true");
                }
            }
        } catch (JSONException e) {
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, AppInfraLogEventID.AI_TAGGING, "AdobeMobile Configuration exception" +
                    Log.getStackTraceString(e));
        }

        return sslValue;
    }

    /**
     * Reading from Adobe json
     */
     protected JSONObject getMasterADBMobileConfig() {
        JSONObject result = null;
        try {
            final InputStream mInputStream = mAppInfra.getAppInfraContext().getAssets().open("ADBMobileConfig.json");
            final BufferedReader mBufferedReader = new BufferedReader(new InputStreamReader(mInputStream));
            final StringBuilder mStringBuilder = new StringBuilder();
            String line;
            while ((line = mBufferedReader.readLine()) != null) {
                mStringBuilder.append(line).append('\n');
            }
            result = new JSONObject(mStringBuilder.toString());
           /* mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.DEBUG, AppInfraLogEventID.AI_TAGGING, "Master ADB Mobile Config Json" +
                    result.toString());*/
        } catch (Exception e) {
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, AppInfraLogEventID.AI_TAGGING, "Tagging ADBMobileConfig file reading exception" +
                    Log.getStackTraceString(e));
        }
        return result;
    }

    /**
     * Constructing default Object data
     **/
    private Map<String, Object> addAnalyticsDataObject() {
        final Map<String, Object> contextData = new HashMap<>();
        contextData.put(AppTaggingConstants.LANGUAGE_KEY, getLanguage());
        contextData.put(AppTaggingConstants.APPSID_KEY, getTrackingIdentifier());
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
     * Removes Sensitive data declared in Adobe json.
     **/
    private Map<String, Object> removeSensitiveData(Map<String, Object> data) {
        final AppConfigurationInterface.AppConfigurationError configError = new AppConfigurationInterface
                .AppConfigurationError();
        if (getPrivacyConsentSensitiveData() && mAppInfra.getConfigInterface() != null) {
            try {
                final Object object = mAppInfra.getConfigInterface().getPropertyForKey
                        ("tagging.sensitiveData", "appinfra", configError);
                if (object instanceof ArrayList<?>) {
                    final ArrayList<?> taggingSensitiveData = (ArrayList<?>) object;
                    if (taggingSensitiveData.size() > 0) {
                        data.keySet().removeAll(taggingSensitiveData);
                    }
                }
            } catch (Exception e) {
                mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR,
                        AppInfraLogEventID.AI_TAGGING, "Tagging" + e);
            }
        }
        return data;
    }


    private String getAppStateFromConfig() {
        if (mAppInfra.getAppIdentity() != null) {
            try {
                return mAppInfra.getAppIdentity().
                        getAppState().toString();
            } catch (Exception e) {
                mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR,
                        AppInfraLogEventID.AI_TAGGING, "Tagging" + e);
            }
        }
        return null;
    }

    private String getLanguage() {
        if (mLanguage == null) {
            final String uiLocale = mAppInfra.getInternationalization().getUILocaleString();
            mLanguage = uiLocale.substring(0, 2);
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.DEBUG,
                    AppInfraLogEventID.AI_TAGGING, "Tagging" + mLanguage);
        }
        return mLanguage;
    }

    private String getUTCTimestamp() {
        String mUTCTimestamp = null;
        if (mAppInfra.getTime() != null) {
            final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS Z", Locale.ENGLISH);
            dateFormat.setTimeZone(TimeZone.getTimeZone(TimeSyncSntpClient.UTC));
            mUTCTimestamp = dateFormat.format(mAppInfra.getTime().getUTCTime());
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.DEBUG,
                    AppInfraLogEventID.AI_TAGGING, "Tagging" + mUTCTimestamp);
        }
        return mUTCTimestamp;
    }

    private String getLocalTimestamp() {
        final Calendar calendar = Calendar.getInstance();
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS Z", Locale.ENGLISH);
        return dateFormat.format(calendar.getTime());
    }

    AppTaggingInterface.PrivacyStatus getMobilePrivacyStatus(MobilePrivacyStatus mMobilePrivacyStatus) {
        AppTaggingInterface.PrivacyStatus mPrivacyStatus = null;
        switch (mMobilePrivacyStatus) {
            case MOBILE_PRIVACY_STATUS_OPT_IN:
                mPrivacyStatus = OPTIN;
                break;
            case MOBILE_PRIVACY_STATUS_OPT_OUT:
                mPrivacyStatus = OPTOUT;
                break;
            case MOBILE_PRIVACY_STATUS_UNKNOWN:
                mPrivacyStatus = UNKNOWN;
                break;
        }
        return mPrivacyStatus;
    }

     void setPrivacyStatus(AppTaggingInterface.PrivacyStatus privacyStatus) {
        switch (privacyStatus) {
            case OPTIN:
                Analytics.trackAction("analyticsOptIn", null);
                Config.setPrivacyStatus(MOBILE_PRIVACY_STATUS_OPT_IN);
                break;
            case OPTOUT:
                Analytics.trackAction("analyticsOptOut", null);
                Config.setPrivacyStatus(MOBILE_PRIVACY_STATUS_OPT_OUT);
                break;
            case UNKNOWN:
                Analytics.trackAction("analyticsUnkown", null);
                Config.setPrivacyStatus(MOBILE_PRIVACY_STATUS_UNKNOWN);
                break;
        }
    }

    void track(String pageName, Map<String, String> paramMap, boolean isTrackPage) {
        if (checkForSslConnection() || checkForProductionState()) {
            trackData(pageName, paramMap, isTrackPage);
        }
    }

    protected boolean checkForProductionState() {
            if (mAppInfra.getAppIdentity() != null) {
                try {
                    return !mAppInfra.getAppIdentity().
                            getAppState().toString().equalsIgnoreCase("Production");
                } catch (Exception e) {
                    mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR,
                            AppInfraLogEventID.AI_TAGGING, "Tagging" + e);
                }
            }
        return false;
    }

    private void trackData(String pageName, Map<String, String> paramMap, boolean isTrackPage) {
        Map contextData = addAnalyticsDataObject();
        if (paramMap != null) {
            paramMap.putAll(contextData);
            contextData = removeSensitiveData((HashMap) paramMap);
        }
        if (null != prevPage && isTrackPage) {
            contextData.put(AppTaggingConstants.PREVIOUS_PAGE_NAME, prevPage);
        }
        if (isTrackPage) {
            if (pageName != null && !pageName.isEmpty()) {
                if (pageName.getBytes().length > 100) {
                    mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.DEBUG, AppInfraLogEventID.AI_TAGGING, "Page name exceeds 100 bytes in length");
                }
                if (pageName.equalsIgnoreCase(prevPage)) {
                    mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.DEBUG, AppInfraLogEventID.AI_TAGGING, "Page name and previous page name shouldn't be same");
                }
                Analytics.trackState(pageName, contextData);
            } else {
                mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.DEBUG, AppInfraLogEventID.AI_TAGGING, "Page name should not  be empty ");
            }
            contextData.put(PAGE_NAME, pageName);
            prevPage = pageName;
        } else {
            final String event = pageName.replaceAll("\\s+", "");
            if (event != null && !event.isEmpty()) {
                if (event.getBytes().length > 255) {
                    mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.DEBUG, AppInfraLogEventID.AI_TAGGING, "Event  exceeds 255 bytes in length");
                }
                Analytics.trackAction(event, contextData);
            } else {
                mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.DEBUG, AppInfraLogEventID.AI_TAGGING, "Event  is null ");
            }
            contextData.put(ACTION_NAME, event);
        }
        sendBroadcast(contextData);  // sending broadcast
    }


    void timeActionStart(String actionStart) {
        if (checkForSslConnection() || checkForProductionState()) {
            Analytics.trackTimedActionStart(actionStart, addAnalyticsDataObject());
        }
    }

    void timeActionEnd(String actionEnd) {
        if (checkForSslConnection() || checkForProductionState()) {
            Analytics.trackTimedActionEnd(actionEnd, null);
        }
    }

    boolean getPrivacyConsentSensitiveData() {
        final String consentValueString = mAppInfra.getSecureStorage().fetchValueForKey(AIL_PRIVACY_CONSENT, getSecureStorageErrorValue());
        final boolean consentValue = consentValueString != null && consentValueString.equalsIgnoreCase("true");
        mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.DEBUG,
                AppInfraLogEventID.AI_TAGGING, "Tagging-consentValue" + consentValue);
        return consentValue;
    }

    void setPrivacyConsentSensitiveData(boolean valueContent) {
        mAppInfra.getSecureStorage().storeValueForKey(AIL_PRIVACY_CONSENT, String.valueOf(valueContent), getSecureStorageErrorValue());
    }

    private SecureStorage.SecureStorageError getSecureStorageErrorValue() {
        return new SecureStorage.SecureStorageError();
    }

    void trackWithInfo(String pageName, String key, String value, boolean isTrackPage) {
        final HashMap<String, String> trackMap = new HashMap<>();
        trackMap.put(key, value);
        track(pageName, trackMap, isTrackPage);
    }

    /**
     * Sending the broadcast event .
     * @param data Map consists of Tagging Data
     */
    private void sendBroadcast(final Map data) {
        final Intent intent = new Intent(ACTION_TAGGING_DATA);
        intent.putExtra(EXTRA_TAGGING_DATA, (Serializable) data);
        LocalBroadcastManager.getInstance(mAppInfra.getAppInfraContext())
                .sendBroadcast(intent);
    }

    void taggingDataUnregister(final BroadcastReceiver receiver) {
        if (receiver != null && mAppInfra.getAppInfraContext() != null) {
            LocalBroadcastManager.getInstance(mAppInfra.getAppInfraContext())
                    .unregisterReceiver(receiver);
        } else {
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.DEBUG,
                    AppInfraLogEventID.AI_TAGGING, "unregisterTaggingData" + "context is null");
        }
    }

    void taggingDataRegister(final BroadcastReceiver receiver) {
        if (receiver != null && mAppInfra.getAppInfraContext() != null) {
            LocalBroadcastManager.getInstance(mAppInfra.getAppInfraContext())
                    .registerReceiver(receiver, new IntentFilter(ACTION_TAGGING_DATA));
        } else {
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.DEBUG,
                    AppInfraLogEventID.AI_TAGGING, "registerTaggingData" + "context is null");
        }
    }

    private String getComponentId() {
        return mComponentID;
    }

    private String getComponentVersionVersionValue() {
        return mComponentVersion;
    }

    void setComponentIdVersion(String mComponentID, String mComponentVersion) {
        this.mComponentID = mComponentID;
        this.mComponentVersion = mComponentVersion;
    }

    void setPrevPage(String prevPage) {
        this.prevPage = prevPage;
    }

    boolean enableAdobeLogs(){
        final AppConfigurationInterface.AppConfigurationError configError = new AppConfigurationInterface
                .AppConfigurationError();
        /*if (mAppInfra.getConfigInterface() != null) {*/
            try {
                final Object loglevelEnabled = mAppInfra.getConfigInterface().getPropertyForKey
                        ("enableAdobeLogs", "appinfra", configError);

                if(loglevelEnabled!=null && loglevelEnabled instanceof Boolean) {
                    final Boolean adobeLogLevelEnabled = (Boolean) loglevelEnabled;
                    if(adobeLogLevelEnabled){
                        Config.setDebugLogging(true);
                        return true;
                    }
                    else
                    {
                        Config.setDebugLogging(false);
                        return false;
                    }

                }

            } catch (Exception e) {
                if (mAppInfra != null) {
                    mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR,
                            AppInfraLogEventID.AI_TAGGING, "Error in Enable Adobe Log" + e.getMessage());
                }
            }
            return false;
    }
}
