/**
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.platform.ews.tagging;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.philips.platform.appinfra.tagging.AppTaggingInterface;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

@SuppressWarnings("WeakerAccess")
@Singleton
public class EWSTagger {

    public AppTaggingInterface getAppTaggingInterface() {
        return appTaggingInterface;
    }

    private AppTaggingInterface appTaggingInterface;

    @Inject
    public EWSTagger(@NonNull final AppTaggingInterface appTaggingInterface) {
        this.appTaggingInterface = appTaggingInterface;
    }

    public void collectLifecycleInfo(Activity activity) {
        getAppTaggingInterface().collectLifecycleInfo(activity);
    }

    public void pauseLifecycleInfo() {
        getAppTaggingInterface().pauseLifecycleInfo();
    }

    public void trackAction(@NonNull final String action, @NonNull final Map<String, String> map) {
        getAppTaggingInterface().trackActionWithInfo(action, map);
    }

    public void trackPage(@NonNull String pageName) {
        getAppTaggingInterface().trackPageWithInfo(pageName, null);
    }

    public void trackActionSendData(@NonNull final String key, @NonNull final String value) {
        Map<String, String> map = new HashMap<>();
        map.put(key, value);
        trackAction(Tag.KEY.SEND_DATA, map);
    }

    public void trackInAppNotificationResponse(@NonNull final String value){
        Map<String, String> map = new HashMap<>();
        map.put(Tag.KEY.IN_APP_NOTIFICATION_RESPONSE, value);
        trackAction(Tag.KEY.SEND_DATA, map);
    }

    public void trackInAppNotification(@NonNull String pageName, @NonNull final String value){
        Map<String, String> map = new HashMap<>();
        map.put(Tag.KEY.IN_APP_NOTIFICATION, value);
        getAppTaggingInterface().trackPageWithInfo(pageName, map);
    }

    @SuppressWarnings("unchecked")
    public  void trackAction(String action, String key, String value) {
        Map<String, String> commonGoalsMap = new HashMap<>();
        commonGoalsMap.put(key, value);
        getAppTaggingInterface().trackActionWithInfo(action, commonGoalsMap);
    }

    public void startTimedAction(String action) {
        getAppTaggingInterface().trackTimedActionStart(action);
    }

    public void stopTimedAction(String action) {
        getAppTaggingInterface().trackTimedActionEnd(action);
    }

}