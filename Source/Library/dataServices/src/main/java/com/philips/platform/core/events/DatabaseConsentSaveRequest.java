package com.philips.platform.core.events;


import com.philips.platform.core.datatypes.Consent;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class DatabaseConsentSaveRequest extends Event {

    private Consent consent;
    private final boolean isUpdateSyncFlag;

    public boolean isUpdateSyncFlag() {
        return isUpdateSyncFlag;
    }

    public DatabaseConsentSaveRequest(Consent consent , boolean isUpdateSyncFlag) {
        this.consent = consent;
        this.isUpdateSyncFlag = isUpdateSyncFlag;
    }

    public Consent getConsent() {
        return consent;
    }
}
