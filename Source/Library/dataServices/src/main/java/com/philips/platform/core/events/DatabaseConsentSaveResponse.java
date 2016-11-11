package com.philips.platform.core.events;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class DatabaseConsentSaveResponse extends Event {
    private boolean saved;

    public DatabaseConsentSaveResponse(final int referenceId, final boolean saved) {
        super(referenceId);
        this.saved = saved;
    }

    public boolean isSaved() {
        return saved;
    }
}
