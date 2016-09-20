/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.tagging;

import android.app.Activity;
import android.content.Context;

import com.adobe.mobile.Analytics;
import com.adobe.mobile.Config;
import com.adobe.mobile.MobilePrivacyStatus;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.timesync.TimeSyncSntpClient;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class AppTagging implements AppTaggingInterface {
//    private String newFieldKey;
//    private String newFieldValue;

    private String mLanguage;
    private String mAppsIdkey;
    private String prevPage;

    AppInfra mAppInfra;
    protected String mComponentID;
    protected String mComponentVersion;

    private String[] defaultValues = {
            AppTaggingConstants.LANGUAGE_KEY,
            AppTaggingConstants.APPSID_KEY,
            AppTaggingConstants.COMPONENT_ID,
            AppTaggingConstants.COMPONENT_VERSION,

            AppTaggingConstants.UTC_TIMESTAMP_KEY


    };


    private Locale mlocale;

    private Context mcontext;

    private Map<String, Object> contextData;

    public static AppTaggingInterface mAppTaggingInterface;

    public AppTagging(AppInfra aAppInfra) {
        mAppInfra = aAppInfra;
        init(Locale.getDefault(), mAppInfra.getAppInfraContext(), "TaggingPageInitialization");

        mAppTaggingInterface = mAppInfra.getTagging();
        // Class shall not presume appInfra to be completely initialized at this point.
        // At any call after the constructor, appInfra can be presumed to be complete.

    }

    private void init(Locale locale, Context context, String appName) {
        mlocale = locale;
        mcontext = context;
        prevPage = appName;
        Config.setContext(context);

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
        return contextData;
    }

    private String getAppsId() {
        if (mAppsIdkey == null) {
            mAppsIdkey = Analytics.getTrackingIdentifier();
        }

        return mAppsIdkey;
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
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ", Locale.ENGLISH);
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
        Map<String, Object> contextData = new HashMap<String, Object>();
        contextData = addAnalyticsDataObject();
        if (paramMap != null) {
            for (Map.Entry<String, String> entry : paramMap.entrySet()) {
                if (!Arrays.asList(defaultValues).contains(entry.getKey())) {
                    contextData.put(entry.getKey(), entry.getValue());
                }
            }
        }
        contextData = addAnalyticsDataObject();
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

}
