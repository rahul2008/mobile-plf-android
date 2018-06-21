/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.pins.shinelib.tagging;

import android.support.annotation.NonNull;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Tagger for BlueLib.
 * <p>
 * Used to supply debug information to a remote analytics server.
 *
 * @publicApi
 */
public class SHNTagger {

    private static final Set<Tagger> taggers = new CopyOnWriteArraySet<>();

    public interface Tagger {
        void sendTechnicalError(final @NonNull String technicalError, @NonNull final String... explanations);
    }

    private SHNTagger() {
        // Prevent instances to be created.
    }

    /**
     * Register a tagger implementation.
     *
     * @param taggerImplementation the tagging implementation
     */
    public static void registerTagger(final @NonNull Tagger taggerImplementation) {
        taggers.add(taggerImplementation);
    }

    /**
     * Send a BlueLib technical error.
     *
     * @param technicalError the technical error message
     * @param explanations   the explanations
     */
    public static void sendTechnicalError(final @NonNull String technicalError, @NonNull final String... explanations) {
        for (Tagger tagger : taggers) {
            tagger.sendTechnicalError(technicalError, explanations);
        }
    }
}
