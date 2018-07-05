/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.pins.shinelib.tagging;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;

import java.util.HashMap;
import java.util.Map;

import static com.philips.pins.shinelib.BuildConfig.LIBRARY_VERSION;
import static com.philips.pins.shinelib.BuildConfig.TLA;
import static com.philips.pins.shinelib.BuildConfig.VERSION_NAME;
import static com.philips.platform.appinfra.tagging.AppTaggingConstants.COMPONENT_VERSION;

public class AppInfraTagger implements SHNTagger.Tagger {

    static final String DELIMITER = ":";
    static final String TAGGING_VERSION_STRING = TLA + DELIMITER + LIBRARY_VERSION;

    private static final String SEND_DATA = "sendData";
    private static final String TECHNICAL_ERROR = "TechnicalError";

    @NonNull
    private AppTaggingInterface taggingInstance;

    /**
     * Create a new AppInfraTagger with an instance of {@link AppInfraInterface}.
     *
     * @param appInfraInterface the {@link AppInfraInterface} instance to create a tagging instance from
     */
    public AppInfraTagger(final @NonNull AppInfraInterface appInfraInterface) {
         taggingInstance = createTaggingInstance(appInfraInterface);
    }

    @VisibleForTesting
    AppTaggingInterface createTaggingInstance(@NonNull AppInfraInterface appInfraInterface) {
        return appInfraInterface.getTagging().createInstanceForComponent(TLA, VERSION_NAME);
    }

    /**
     * Send a BlueLib technical error using {@link AppTaggingInterface}.
     * <p>
     * The technical error and any additional explanations will be prepended with BlueLib's component TLA and colon-delimited before sending.
     *
     * @param technicalError the technical error message
     * @param explanations   the explanations
     */
    @Override
    public void sendTechnicalError(@NonNull String technicalError, @NonNull String... explanations) {
        final StringBuilder builder = new StringBuilder(TLA);

        // Add calling class and method, if available
        final StackTraceElement caller = getCaller();
        if (caller != null) {
            builder.append(DELIMITER).append(caller.getClassName()).append(".").append(caller.getMethodName());
        }

        // Add the error message
        builder.append(DELIMITER).append(technicalError.trim().replace(DELIMITER, ""));

        // Add optional explanations
        for (final String explanation : explanations) {
            builder.append(DELIMITER).append(explanation.trim().replace(DELIMITER, ""));
        }

        final Map<String, String> data = new HashMap<>();
        data.put(TECHNICAL_ERROR, builder.toString());
        data.put(COMPONENT_VERSION, TAGGING_VERSION_STRING);

        taggingInstance.trackActionWithInfo(SEND_DATA, data);
    }

    @Nullable
    private StackTraceElement getCaller() {
        StackTraceElement[] stackTrace = new Throwable().getStackTrace();

        for (int i = 0; i < stackTrace.length; i++) {
            final StackTraceElement el = stackTrace[i];

            if (AppInfraTagger.class.getName().equals(el.getClassName())) {
                // Get item at index + 2 to account for the local method calls in this class
                return stackTrace[i + 2];
            }
        }
        return null;
    }
}
