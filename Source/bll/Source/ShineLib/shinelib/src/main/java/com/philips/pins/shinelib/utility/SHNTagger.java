/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.pins.shinelib.utility;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;

import java.util.HashMap;
import java.util.Map;

import static com.philips.pins.shinelib.BuildConfig.LIBRARY_VERSION;
import static com.philips.pins.shinelib.BuildConfig.TLA;
import static com.philips.pins.shinelib.BuildConfig.VERSION_NAME;
import static com.philips.platform.appinfra.tagging.AppInfraTaggingUtil.SEND_DATA;
import static com.philips.platform.appinfra.tagging.AppInfraTaggingUtil.TECHNICAL_ERROR;
import static com.philips.platform.appinfra.tagging.AppTaggingConstants.COMPONENT_VERSION;

/**
 * Tagger for BlueLib.
 * <p>
 * Makes use of AppInfra's {@link AppTaggingInterface} to send debug data.
 *
 * @publicApi
 */
public class SHNTagger {

    static final String DELIMITER = ":";
    static final String TAGGING_VERSION_STRING = TLA + DELIMITER + LIBRARY_VERSION;

    @NonNull
    private final AppTaggingInterface taggingInstance;

    /**
     * Instantiates a new SHNTagger.
     *
     * @param appInfraInterface the appInfraInterface instance to create a tagging instance from
     */
    public SHNTagger(final @NonNull AppInfraInterface appInfraInterface) {
        this.taggingInstance = createTaggingInstance(appInfraInterface);
    }

    @VisibleForTesting
    AppTaggingInterface createTaggingInstance(AppInfraInterface appInfraInterface) {
        return appInfraInterface.getTagging().createInstanceForComponent(TLA, VERSION_NAME);
    }

    /**
     * Send a technical error.
     * <p>
     * The error string will be prepended with the component's TLA and the delimiter character before sending.
     *
     * @param technicalError the technical error message
     */
    public void sendTechnicalError(final @NonNull String technicalError) {
        final Map<String, String> data = new HashMap<>();
        data.put(COMPONENT_VERSION, TAGGING_VERSION_STRING);
        data.put(TECHNICAL_ERROR, TLA + DELIMITER + technicalError);

        taggingInstance.trackActionWithInfo(SEND_DATA, data);
    }
}
