package com.philips.platform.core.events;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.philips.platform.core.datatypes.Consent;


public class ConsentBackendSaveResponse extends BackendResponse {
    @Nullable
    private final Consent consent;

    private final int responseCode;

    public ConsentBackendSaveResponse(@NonNull final int refernceId, @Nullable final Consent consent, final int responseCode) {
        super(refernceId);
        this.consent = consent;
        this.responseCode = responseCode;
    }

    @Nullable
    public Consent getConsent() {
        return consent;
    }

    public int getResponseCode() {
        return responseCode;
    }
}
