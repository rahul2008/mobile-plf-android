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

    interface Key {
        String SEND_DATA = "sendData";
        String TECHNICAL_ERROR = "TechnicalError";
    }

    static final String DELIMITER = ":";
    static final String TAGGING_VERSION_STRING = TLA + DELIMITER + LIBRARY_VERSION;

    @Nullable
    static AppTaggingInterface taggingInstance;

    private SHNTagger() {
        // Prevent instances to be created.
    }

    /**
     * Send a technical error.
     * <p>
     * The error string will be prepended with the component's TLA and the delimiter character before sending.
     *
     * @param technicalError the technical error message
     */
    public static void sendTechnicalError(final @NonNull String technicalError) {
        if (taggingInstance == null) {
            return;
        }

        final Map<String, String> data = new HashMap<>();
        data.put(COMPONENT_VERSION, TAGGING_VERSION_STRING);
        data.put(TECHNICAL_ERROR, TLA + DELIMITER + technicalError);

        taggingInstance.trackActionWithInfo(SEND_DATA, data);
    }

    /**
     * Initializes SHNTagger with an instance of {@link AppInfraInterface}.
     *
     * @param appInfraInterface the {@link AppInfraInterface} instance to create a tagging instance from
     */
    public static void initialize(final @NonNull AppInfraInterface appInfraInterface) {
        taggingInstance = appInfraInterface.getTagging().createInstanceForComponent(TLA, VERSION_NAME);
    }
}
