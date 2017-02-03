package com.philips.platform.core.events;


import com.philips.platform.core.datatypes.Consent;
import com.philips.platform.core.listeners.DBRequestListener;

import java.util.List;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class DatabaseConsentUpdateRequest extends Event {

    private final List<? extends Consent> consentDetails;

    private final DBRequestListener dbRequestListener;

    public List<? extends Consent> getConsentDetails() {
        return consentDetails;
    }

    public DatabaseConsentUpdateRequest(List<? extends Consent> consentDetails, DBRequestListener dbRequestListener) {

        this.consentDetails = consentDetails;

        this.dbRequestListener = dbRequestListener;
    }

    public DBRequestListener getDbRequestListener() {
        return dbRequestListener;
    }
}
