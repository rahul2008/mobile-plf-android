/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.pins.shinelib.utility;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.philips.pins.shinelib.BuildConfig;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;

import java.util.HashMap;
import java.util.Map;

public class SHNTagger {

    private static final String COMPONENT_ID = "bll";

    class Key {
        static final String KEY_MODEL = "Model";
        static final String KEY_OS = "Operating System";
        static final String KEY_MANUFACTURER = "Manufacturer";
        static final String KEY_LIBRARY_VERSION = "Library version";
    }

    @NonNull
    private final AppTaggingInterface appTaggingInterface;

    /**
     * Instantiates a new SHNTagger.
     *
     * @param appInfraInterface the appInfraInterface instance to create a tagging instance from
     */
    public SHNTagger(final @NonNull AppInfraInterface appInfraInterface) {
        this.appTaggingInterface = createTagging(appInfraInterface);
    }

    @VisibleForTesting
    AppTaggingInterface createTagging(AppInfraInterface appInfraInterface) {
        return appInfraInterface.getTagging().createInstanceForComponent(COMPONENT_ID, BuildConfig.VERSION_NAME);
    }

    /**
     * Track action.
     *
     * @param action the action
     * @param key    the key
     * @param value  the value
     */
    public void trackAction(final @NonNull String action, final @NonNull String key, final @NonNull String value) {
        final Map<String, String> map = new HashMap<>();
        map.put(Key.KEY_MODEL, Build.MODEL);
        map.put(Key.KEY_OS, Build.VERSION.RELEASE);
        map.put(Key.KEY_MANUFACTURER, Build.MANUFACTURER);
        map.put(Key.KEY_LIBRARY_VERSION, BuildConfig.VERSION_NAME);
        map.put(key, value);

        appTaggingInterface.trackActionWithInfo(action, map);
    }
}
