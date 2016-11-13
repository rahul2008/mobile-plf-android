package com.philips.platform.core.events;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class DatabaseConsentSaveResponse extends Event {
    private boolean saved;

    //TODO: Spoorti: Is this event used or not, since there is no defaut constructor also
    public DatabaseConsentSaveResponse(final int referenceId, final boolean saved) {
        super(referenceId);
        this.saved = saved;
    }

    //TODO: Spoorti: Is it used?
    public boolean isSaved() {
        return saved;
    }
}
