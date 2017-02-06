package com.philips.platform.core.events;

import com.philips.platform.core.datatypes.ConsentDetail;

import java.util.List;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ConsentBackendGetRequest extends Event {

    private int referenceId;
    private final List<ConsentDetail> consentDetails;

    public ConsentBackendGetRequest(int referenceId, List<ConsentDetail> consentDetails) {
        this.referenceId = referenceId;
        this.consentDetails = consentDetails;
    }
    public int getReferenceId() {
        return referenceId;
    }

    public List<ConsentDetail> getConsentDetails() {
        return consentDetails;
    }
}
