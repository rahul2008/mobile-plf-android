package com.philips.platform.core.events;


import com.philips.platform.core.datatypes.Consent;

import java.util.List;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ConsentBackendListSaveRequest extends Event {

    private List<? extends Consent> consentList;

    public ConsentBackendListSaveRequest(List<? extends Consent> consentList) {
        this.consentList = consentList;
    }

    public List<? extends Consent> getConsentList() {
        return consentList;
    }
}