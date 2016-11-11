package com.philips.platform.core.events;


import com.philips.platform.core.datatypes.Consent;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class DatabaseConsentGetResponse extends Event {
    private Consent consent;

    public DatabaseConsentGetResponse(int referenceId, Consent consent) {
        super(referenceId);
        this.consent = consent;
    }

    public Consent getConsent() {
        return consent;
    }
}
