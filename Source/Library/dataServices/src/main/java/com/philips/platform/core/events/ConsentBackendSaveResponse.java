package com.philips.platform.core.events;

import com.philips.platform.core.datatypes.Consent;
import com.philips.platform.core.listeners.DBRequestListener;

import java.util.List;


public class ConsentBackendSaveResponse extends Event{

    private final List<Consent> consentList;

    private final int responseCode;

    private final DBRequestListener dbRequestListener;

    public ConsentBackendSaveResponse(List<Consent> consentList, final int responseCode, DBRequestListener dbRequestListener) {

        this.consentList = consentList;
        this.responseCode = responseCode;
        this.dbRequestListener = dbRequestListener;
    }

    public List<Consent> getConsentList() {
        return consentList;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public DBRequestListener getDbRequestListener() {
        return dbRequestListener;
    }
}
