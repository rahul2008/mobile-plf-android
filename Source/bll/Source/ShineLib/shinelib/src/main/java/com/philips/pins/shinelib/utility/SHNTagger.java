/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.pins.shinelib.utility;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.philips.pins.shinelib.BuildConfig;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.tagging.AppInfraTaggingUtil;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;

import static com.philips.platform.appinfra.tagging.AppInfraTaggingUtil.TECHNICAL_ERROR;

public class SHNTagger {

    interface MetaData {
        String COMPONENT_ID = "bll";
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
        return appInfraInterface.getTagging().createInstanceForComponent(MetaData.COMPONENT_ID, BuildConfig.VERSION_NAME);
    }

    /**
     * Send data.
     *
     * @param key   the key
     * @param value the value
     */
    public void sendData(final @NonNull String key, final @NonNull String value) {
        appTaggingInterface.trackActionWithInfo(AppInfraTaggingUtil.SEND_DATA, key, value);
    }

    /**
     * Send technical error.
     *
     * @param technicalError the technical error message
     */
    public void sendTechnicalError(final @NonNull String technicalError) {
        appTaggingInterface.trackActionWithInfo(AppInfraTaggingUtil.SEND_DATA, TECHNICAL_ERROR, technicalError);
    }
}
