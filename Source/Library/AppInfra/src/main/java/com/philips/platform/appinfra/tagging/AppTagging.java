/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.tagging;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;

import com.adobe.mobile.Analytics;
import com.adobe.mobile.Config;
import com.adobe.mobile.MobilePrivacyStatus;
import com.philips.platform.appinfra.AppInfra;

import java.util.HashMap;
import java.util.Map;


/**
 * App Tagging classs .
 */
public class AppTagging implements AppTaggingInterface {

    static final String PAGE_NAME = "ailPageName";
    static final String ACTION_NAME = "ailActionName";
    static final String ACTION_TAGGING_DATA = "ACTION_TAGGING_DATA";
    static final String EXTRA_TAGGING_DATA = "TAGGING_DATA";
    static final String AIL_PRIVACY_CONSENT = "ailPrivacyConsentForSensitiveData";
    private static String prevPage;
    private final AppInfra mAppInfra;
    protected String mComponentID;
    protected String mComponentVersion;
    private AppTaggingHandler appTaggingHandle;

    public AppTagging(AppInfra aAppInfra) {
        mAppInfra = aAppInfra;
        init(mAppInfra.getAppInfraContext());
        // Class shall not presume appInfra to be completely initialized at this point.
        // At any call after the constructor, appInfra can be presumed to be complete.
    }

    private void init(Context context) {
        appTaggingHandle = new AppTaggingHandler(mAppInfra);
        Config.setContext(context);
    }


    @Override
    public String getTrackingIdentifier() {
        return Analytics.getTrackingIdentifier();
    }


    @Override
    public AppTaggingInterface createInstanceForComponent(String componentId, String componentVersion) {
        return new AppTaggingWrapper(mAppInfra, componentId, componentVersion);
    }

    @Override
    public PrivacyStatus getPrivacyConsent() {
        final MobilePrivacyStatus mMobilePrivacyStatus = Config.getPrivacyStatus();
        return appTaggingHandle.getMobilePrivacyStatus(mMobilePrivacyStatus);
    }

    @Override
    public void setPrivacyConsent(PrivacyStatus privacyStatus) {
        appTaggingHandle.setPrivacyStatus(privacyStatus);
    }

    @Override
    public void trackTimedActionStart(String actionStart) {
        appTaggingHandle.timeActionStart(actionStart);
    }

    @Override
    public void trackTimedActionEnd(String actionEnd) {
        appTaggingHandle.timeActionEnd(actionEnd);
    }

    @Override
    public boolean getPrivacyConsentForSensitiveData() {
        return appTaggingHandle.getPrivacyConsentSensitiveData();
    }

    // Sets the value of Privacy Consent For Sensitive Data and stores in preferences
    @Override
    public void setPrivacyConsentForSensitiveData(boolean valueContent) {
        appTaggingHandle.setPrivacyConsentSensitiveData(valueContent);
    }

    @Override
    public void trackPageWithInfo(String pageName, String key, String value) {
        appTaggingHandle.trackWithInfo(pageName, key, value, true);
    }

    @Override
    public void trackPageWithInfo(String pageName, Map<String, String> paramMap) {
        appTaggingHandle.track(pageName, paramMap, true);
    }

    @Override
    public void trackActionWithInfo(String pageName, String key, String value) {
        appTaggingHandle.trackWithInfo(pageName, key, value, false);
    }

    @Override
    public void trackActionWithInfo(String pageName, Map<String, String> paramMap) {
        appTaggingHandle.track(pageName, paramMap, false);
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
        socialSharing(medium, sharedItem);
    }

    @Override
    public void trackLinkExternal(String url) {
        trackActionWithInfo("sendData", "exitLinkName", url);
    }

    @Override
    public void trackFileDownload(String filename) {
        trackActionWithInfo("sendData", "fileName", filename);
    }

    @Override
    public void unregisterTaggingData(final BroadcastReceiver receiver) {
        appTaggingHandle.taggingDataUnregister(receiver);
    }

    @Override
    public void registerTaggingData(final BroadcastReceiver receiver) {
        appTaggingHandle.taggingDataRegister(receiver);
    }

    private void socialSharing(AppTaggingInterface.SocialMedium medium, String sharedItem) {
        final HashMap<String, String> trackMap = new HashMap<>();
        trackMap.put("socialItem", sharedItem);
        trackMap.put("socialType", medium.toString());
        trackActionWithInfo("socialShare", trackMap);
    }

    void setComponentIdAndVersion(String mComponentID, String mComponentVersion) {
        appTaggingHandle.setComponentIdVersion(mComponentID, mComponentVersion);
    }

    String getPreviousPage() {
        return prevPage;
    }

    @Override
    public void setPreviousPage(String previousPage) {
        prevPage = previousPage;
        appTaggingHandle.setPrevPage(prevPage);

    }

}
