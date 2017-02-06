package com.philips.platform.core.events;

import com.philips.platform.core.datatypes.ConsentDetail;
import com.philips.platform.core.listeners.DBRequestListener;

import java.util.List;


public class ConsentBackendSaveResponse extends Event{

    private final List<ConsentDetail> consentDetailList;

    private final int responseCode;

    private final DBRequestListener dbRequestListener;

    public ConsentBackendSaveResponse(List<ConsentDetail> consentDetailList, final int responseCode, DBRequestListener dbRequestListener) {

        this.consentDetailList = consentDetailList;
        this.responseCode = responseCode;
        this.dbRequestListener = dbRequestListener;
    }

    public List<ConsentDetail> getConsentDetailList() {
        return consentDetailList;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public DBRequestListener getDbRequestListener() {
        return dbRequestListener;
    }
}
