package com.philips.platform.core.events;


import com.philips.platform.core.datatypes.Consent;
import com.philips.platform.core.listeners.DBRequestListener;

import java.util.List;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class DatabaseConsentSaveRequest extends Event {


    private final List<Consent> consents;

    private final DBRequestListener dbRequestListener;


    public DBRequestListener getDbRequestListener() {
        return dbRequestListener;
    }

    public DatabaseConsentSaveRequest(List<Consent> consents , DBRequestListener dbRequestListener) {
        this.consents = consents;
        this.dbRequestListener = dbRequestListener;

    }

    public List<Consent> getConsents() {
        return consents;
    }
}
