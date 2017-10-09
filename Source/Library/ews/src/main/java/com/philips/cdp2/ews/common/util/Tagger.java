/*
 * Copyright (c) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 */

package com.philips.cdp2.ews.common.util;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;

import com.adobe.mobile.Config;
import com.philips.cdp2.ews.BuildConfig;
import com.philips.cdp2.ews.tagging.Actions;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Wrapper around Tagging.
 */
public class Tagger {

    private static final String TAG = "Tagger";

    private static final String CONFIG_FILE = "ADBMobileConfig.json";
    private static AppTaggingInterface taggingInterface;

    /**
     * Setup Tagging. Uses {@link BuildConfig#DEBUG} to setup
     * tagging for DEBUG or RELEASE
     *
     * @param context applicationContext
     */
    public static void init(@NonNull final Context context, @NonNull final AppTaggingInterface taggInterface) {
        taggInterface.setPrivacyConsent(AppTaggingInterface.PrivacyStatus.UNKNOWN);
        setTagInterface(taggInterface);
        setupTagging(context, BuildConfig.DEBUG);
    }

    /**
     * Call when Activity created
     *
     * @param activity that is created
     */
    public static void collectLifecycleInfo(Activity activity) {
        getTagInterface().collectLifecycleInfo(activity);
    }

    /**
     * Call when Activity destroyed
     */
    public static void pauseLifecycleInfo() {
        getTagInterface().pauseLifecycleInfo();
    }

    public static void trackPage(@NonNull final String pageName) {
        getTagInterface().trackPageWithInfo(pageName, null);
    }

    public static void trackAction(@NonNull final String action) {
        trackAction(action, new HashMap<String, String>(0));
    }

    public static void trackAction(@NonNull final String action, @NonNull final Map<String, String> map) {
        getTagInterface().trackActionWithInfo(action, map);
    }

    public static void trackAction(@NonNull final String action, @NonNull final String key, @NonNull final String value) {
        Map<String, String> map = new HashMap<>();
        map.put(key, value);
        trackAction(action, map);
    }

    public static void trackActionSendData(@NonNull final String key, @NonNull final String value) {
        Map<String, String> map = new HashMap<>();
        map.put(key, value);
        trackAction(Actions.Key.SEND_DATA, map);
    }

    public static void trackActionDialog(String dialogValue, String actionValue) {
        Map<String, String> map = new HashMap<>();
        map.put(Actions.Key.IN_APP_NOTIFICATION, dialogValue);
        map.put(Actions.Key.IN_APP_NOTIFICATION_RESPONSE, actionValue);

        trackAction(Actions.Key.SEND_DATA, map);
    }

    public static void trackActionPopover(String value) {
        Map<String, String> map = new HashMap<>();
        map.put(Actions.Key.IN_APP_NOTIFICATION, value);

        trackAction(Actions.Key.SEND_DATA, map);
    }

    public static void trackActionPopover(String value, String action) {
        Map<String, String> map = new HashMap<>();
        map.put(Actions.Key.IN_APP_NOTIFICATION, value);
        map.put(Actions.Key.IN_APP_NOTIFICATION_RESPONSE, action);

        trackAction(Actions.Key.SEND_DATA, map);
    }

    private static AppTaggingInterface getTagInterface() {
        return taggingInterface;
    }

    public static void setTagInterface(AppTaggingInterface tagInterface) {
        taggingInterface = tagInterface;
    }

    private static void setupTagging(@NonNull final Context context, boolean debug) {
        InputStream in;
        try {
            in = context.getAssets().open(CONFIG_FILE);
            Config.overrideConfigStream(in);
            Config.setDebugLogging(debug);
        } catch (IOException e) {
            Logger.e(TAG, "Error setting up tagging interface!", e);

        }
    }

    public static void startTimedAction(String action) {
        getTagInterface().trackTimedActionStart(action);
    }

    public static void stopTimedAction(String action) {
        getTagInterface().trackTimedActionEnd(action);
    }
}
