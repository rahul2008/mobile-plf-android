package com.philips.platform.core.events;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.philips.platform.core.datatypes.Consent;
import com.philips.platform.core.listeners.DBRequestListener;


public class ConsentBackendSaveResponse extends Event{
    @Nullable
    private final Consent consent;

    private final int responseCode;

    private final DBRequestListener dbRequestListener;

    public DBRequestListener getDbRequestListener() {
        return dbRequestListener;
    }

    public ConsentBackendSaveResponse(@NonNull final int refernceId, @Nullable final Consent consent, final int responseCode, DBRequestListener dbRequestListener) {
        this.consent = consent;
        this.responseCode = responseCode;
        this.dbRequestListener = dbRequestListener;

    }

    @Nullable
    public Consent getConsent() {
        return consent;
    }

    //TODO: Spoorti: is it required
    public int getResponseCode() {
        return responseCode;
    }
}
