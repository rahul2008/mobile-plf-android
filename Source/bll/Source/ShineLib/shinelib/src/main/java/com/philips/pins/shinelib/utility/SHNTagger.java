/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.pins.shinelib.utility;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;

import java.util.HashMap;
import java.util.Map;

import static com.philips.pins.shinelib.BuildConfig.LIBRARY_VERSION;
import static com.philips.pins.shinelib.BuildConfig.TLA;
import static com.philips.pins.shinelib.BuildConfig.VERSION_NAME;
import static com.philips.pins.shinelib.utility.SHNTagger.Key.SEND_DATA;
import static com.philips.pins.shinelib.utility.SHNTagger.Key.TECHNICAL_ERROR;
import static com.philips.platform.appinfra.tagging.AppTaggingConstants.COMPONENT_VERSION;

/**
 * Tagger for BlueLib.
 * <p>
 * Makes use of AppInfra's {@link AppTaggingInterface} to send debug data.
 *
 * @publicApi
 */
public class SHNTagger {

    private static final String TAG = "SHNTagger";

    static final String DELIMITER = ":";
    static final String TAGGING_VERSION_STRING = TLA + DELIMITER + LIBRARY_VERSION;

    interface Key {
        String SEND_DATA = "sendData";
        String TECHNICAL_ERROR = "TechnicalError";
    }

    @Nullable
    static AppTaggingInterface taggingInstance;

    private SHNTagger() {
        // Prevent instances to be created.
    }

    /**
     * Initializes SHNTagger with an instance of {@link AppInfraInterface}.
     *
     * @param appInfraInterface the {@link AppInfraInterface} instance to create a tagging instance from
     */
    public static void initialize(final @NonNull AppInfraInterface appInfraInterface) {
        taggingInstance = appInfraInterface.getTagging().createInstanceForComponent(TLA, VERSION_NAME);
    }

    /**
     * Send a technical error.
     * <p>
     * The technical error and any additional explanations will be prepended with the component's TLA and colon-delimited before sending.
     *
     * @param technicalError the technical error message
     * @param explanations   the explanations
     */
    public static void sendTechnicalError(final @NonNull String technicalError, @NonNull final String... explanations) {
        if (taggingInstance == null) {
            SHNLogger.w(TAG, "Tagger not initialized, call SHNTagger.initialize(AppInfraInterface) first.");
            return;
        }
        final StringBuilder builder = new StringBuilder(TLA);

        // Add calling class and method
        final StackTraceElement caller = Thread.currentThread().getStackTrace()[3];
        builder.append(DELIMITER).append(caller.getClassName()).append(".").append(caller.getMethodName());

        // Add the error message
        builder.append(DELIMITER).append(technicalError.trim());

        // Add optional explanations
        for (final String explanation : explanations) {
            builder.append(DELIMITER).append(explanation.trim());
        }

        final Map<String, String> data = new HashMap<>();
        data.put(TECHNICAL_ERROR, builder.toString());
        data.put(COMPONENT_VERSION, TAGGING_VERSION_STRING);

        taggingInstance.trackActionWithInfo(SEND_DATA, data);
    }
}
