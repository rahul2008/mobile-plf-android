/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.mya.catk.listener;

import com.philips.platform.mya.catk.error.ConsentNetworkError;
import com.philips.platform.mya.chi.ConsentCallback;
import com.philips.platform.mya.chi.ConsentError;
import com.philips.platform.mya.chi.datamodel.BackendConsent;
import com.philips.platform.mya.chi.datamodel.Consent;
import com.philips.platform.mya.chi.datamodel.ConsentDefinition;

import java.util.List;

public class GetConsentForTypeResponseListener implements ConsentResponseListener {

    private ConsentDefinition definition;

    private ConsentCallback callback;

    public GetConsentForTypeResponseListener(ConsentCallback callback, ConsentDefinition definition) {
        this.callback = callback;
        this.definition = definition;
    }

    @Override
    public void onResponseSuccessConsent(List<BackendConsent> responseData) {
        BackendConsent backendConsent = null;
        if (responseData != null && !responseData.isEmpty()) {
            backendConsent = responseData.get(0);
        }
        callback.onGetConsentRetrieved(new Consent(backendConsent, definition));
    }

    @Override
    public void onResponseFailureConsent(ConsentNetworkError error) {
        callback.onGetConsentFailed(new ConsentError(error.getMessage(), error.getCatkErrorCode()));
    }

}
