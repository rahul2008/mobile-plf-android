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

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AppTagging implements AppTaggingInterface {
    private static String newFieldKey;

    private static String newFieldValue;

    private String mLanguage;
    private String mAppsIdkey;
    private String mLocalTimestamp;
    private String mUTCTimestamp;
    private String prevPage;

    private boolean isTrackPage = false;
    private boolean isTrackAction = false;


    AppInfra mAppInfra;
    protected String mComponentID;
    protected String mComponentVersion;

    private static String[] defaultValues = {
            AppTaggingConstants.LANGUAGE_KEY,
            AppTaggingConstants.APPSID_KEY,
            AppTaggingConstants.COMPONENT_ID,
            AppTaggingConstants.COMPONENT_VERSION,

            AppTaggingConstants.UTC_TIMESTAMP_KEY


    };


    private static Locale mlocale;

    private static Context mcontext;

    private static String mAppName;
    private static Map<String, Object> contextData;

    public AppTagging(AppInfra aAppInfra) {
        mAppInfra = aAppInfra;
        init(Locale.getDefault(), mAppInfra.getAppInfraContext(), "TaggingPageInitialization");
        // Class shall not presume appInfra to be completely initialized at this point.
        // At any call after the constructor, appInfra can be presumed to be complete.

    }

    private void init(Locale locale, Context context, String appName) {
        mlocale = locale;
        mcontext = context;
        prevPage = appName;
        Config.setContext(context);
        if (appName == null) {
            throw new RuntimeException("Please set app name for tagging library");
        }
    }

    private Map<String, Object> addAnalyticsDataObject() {
        Map<String, Object> contextData = new HashMap<String, Object>();

        contextData.put(AppTaggingConstants.LANGUAGE_KEY, getLanguage());
//        contextData.put(AppTaggingConstants.CURRENCY_KEY, getCurrency());

        contextData.put(AppTaggingConstants.APPSID_KEY, getAppsId());
        contextData.put(AppTaggingConstants.COMPONENT_ID, getComponentId());
        contextData.put(AppTaggingConstants.COMPONENT_VERSION, getComponentVersionVersionValue());
        contextData.put(AppTaggingConstants.LOCAL_TIMESTAMP_KEY, getLocalTimestamp());
        contextData.put(AppTaggingConstants.UTC_TIMESTAMP_KEY, getUTCTimestamp());
        if (null != getNewKey() && null != getNewValue()) {

            if (!getNewKey().contains(",") && !getNewValue().contains(",")) {
                contextData.put(getNewKey(), getNewValue());
            }

        }

        return contextData;
    }

    private String getAppsId() {
        if (mAppsIdkey == null) {
            mAppsIdkey = Analytics.getTrackingIdentifier();
        }

        return mAppsIdkey;
    }

    private void setNewKey(String newFieldkey) {
        AppTagging.newFieldKey = newFieldkey;

    }

    private void setNewValue(String newFieldvalue) {
        AppTagging.newFieldValue = newFieldvalue;
    }

    private String getNewKey() {
        return newFieldKey;
    }

    private String getNewValue() {
        return newFieldValue;
    }

    private String getLanguage() {
        if (mLanguage == null) {
            mLanguage = mlocale.getLanguage();
        }
        return mLanguage;

    }

    private String getUTCTimestamp() {
        String UTCtime = null;

        if (mAppInfra.getTime() != null) {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS a", Locale.ENGLISH);

            UTCtime = df.format(mAppInfra.getTime().getUTCTime());
            mUTCTimestamp = UTCtime;
            Log.i("mUTCTimestamp", "" + mUTCTimestamp);
        }

        if (mUTCTimestamp != null) {
            return mUTCTimestamp;
        }
        return mUTCTimestamp;
    }

    private String getLocalTimestamp() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS a", Locale.ENGLISH);
        String formattedDate = df.format(c.getTime());
        mLocalTimestamp = formattedDate;
        if (mLocalTimestamp != null) {
            return mLocalTimestamp;
        }
        return mLocalTimestamp;
    }

    private String getComponentId() {
        if (mComponentID == null) {
            mComponentID = "DefaultText";
        }
        return mComponentID;
    }

    private String getComponentVersionVersionValue() {
        if (mComponentVersion == null) {
            mComponentVersion = "DefaultValue";
        }
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
        if (mPrivacyStatus != null) {
            return mPrivacyStatus;
        }
        return null;
    }


    private void track(String pageName, String key, String value, Map<String, String> paramMap) {

        if (key != null && value != null) {
            if (!Arrays.asList(defaultValues).contains(key)) {

                setNewKey(key);
                setNewValue(value);
            }
        } else if (paramMap != null) {
            for (Map.Entry<String, String> entry : paramMap.entrySet()) {
                if (!Arrays.asList(defaultValues).contains(entry.getKey())) {

                    setNewKey(entry.getKey());
                    setNewValue(entry.getValue());
                    contextData = addAnalyticsDataObject();
                }
            }
        }

        contextData = addAnalyticsDataObject();
        if (null != prevPage && isTrackPage) {
            contextData.put(AppTaggingConstants.PREVIOUS_PAGE_NAME, prevPage);
        }
        if (isTrackPage)
            Analytics.trackState(pageName, contextData);

        if (isTrackAction)
            Analytics.trackAction(pageName, contextData);

        prevPage = pageName;
    }

    @Override
    public void trackPageWithInfo(String pageName, String key, String value) {

        isTrackPage = true;
        isTrackAction = false;
        track(pageName, key, value, null);
    }

    @Override
    public void trackPageWithInfo(String pageName, Map<String, String> paramMap) {

        isTrackPage = true;
        isTrackAction = false;
        track(pageName, null, null, paramMap);

    }


    @Override
    public void trackActionWithInfo(String pageName, String key, String value) {

        isTrackAction = true;
        isTrackPage = false;
        track(pageName, key, value, null);

    }


    @Override
    public void trackActionWithInfo(String pageName, Map<String, String> paramMap) {

        isTrackAction = true;
        isTrackPage = false;
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
