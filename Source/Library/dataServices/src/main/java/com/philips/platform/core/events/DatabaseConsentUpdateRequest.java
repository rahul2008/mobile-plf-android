package com.philips.platform.core.events;


import com.philips.platform.core.datatypes.ConsentDetail;
import com.philips.platform.core.listeners.DBRequestListener;

import java.util.List;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class DatabaseConsentUpdateRequest extends Event {

    private final List<? extends ConsentDetail> consentDetails;

    private final DBRequestListener<ConsentDetail> dbRequestListener;

    public List<? extends ConsentDetail> getConsentDetails() {
        return consentDetails;
    }

    public DatabaseConsentUpdateRequest(List<? extends ConsentDetail> consentDetails, DBRequestListener<ConsentDetail> dbRequestListener) {

        this.consentDetails = consentDetails;

        this.dbRequestListener = dbRequestListener;
    }

    public DBRequestListener<ConsentDetail> getDbRequestListener() {
        return dbRequestListener;
    }
}
