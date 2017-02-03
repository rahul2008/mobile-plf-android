package com.philips.platform.core.events;

import com.philips.platform.core.datatypes.Consent;

import java.util.List;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ConsentBackendGetRequest extends Event {

    private int referenceId;
    private final List<Consent> consents;

    public ConsentBackendGetRequest(int referenceId, List<Consent> consents) {
        this.referenceId = referenceId;
        this.consents = consents;
    }
    public int getReferenceId() {
        return referenceId;
    }

    public List<Consent> getConsents() {
        return consents;
    }
}
