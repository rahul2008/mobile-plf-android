package com.philips.platform.core.events;


import com.philips.platform.core.datatypes.Consent;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class DatabaseConsentSaveRequest extends Event {

    private Consent consent;

    public DatabaseConsentSaveRequest(Consent consent) {
        this.consent = consent;
    }

    public Consent getConsent() {
        return consent;
    }
}
