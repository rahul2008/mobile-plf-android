package com.philips.platform.core.events;


import com.philips.platform.core.datatypes.Consent;
import com.philips.platform.core.listeners.DBRequestListener;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class DatabaseConsentSaveRequest extends Event {

    private Consent consent;
    private final boolean isUpdateSyncFlag;
    private final DBRequestListener dbRequestListener;

    public boolean isUpdateSyncFlag() {
        return isUpdateSyncFlag;
    }

    public DBRequestListener getDbRequestListener() {
        return dbRequestListener;
    }

    public DatabaseConsentSaveRequest(Consent consent, boolean isUpdateSyncFlag, DBRequestListener dbRequestListener) {
        this.consent = consent;
        this.isUpdateSyncFlag = isUpdateSyncFlag;
        this.dbRequestListener = dbRequestListener;

    }

    public Consent getConsent() {
        return consent;
    }
}
