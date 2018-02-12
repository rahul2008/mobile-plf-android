/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.mya.catk.listener;

import com.philips.platform.mya.catk.error.ConsentNetworkError;
import com.philips.platform.mya.catk.utils.CatkLogger;
import com.philips.platform.mya.chi.ConsentError;
import com.philips.platform.mya.chi.PostConsentCallback;
import com.philips.platform.mya.chi.datamodel.BackendConsent;
import com.philips.platform.mya.chi.datamodel.Consent;
import com.philips.platform.mya.chi.datamodel.ConsentDefinition;

import java.util.List;

public class CreateConsentResponseListener implements CreateConsentListener {

    private final ConsentDefinition definition;

    private final List<BackendConsent> backendConsents;
    private final PostConsentCallback callback;

    public CreateConsentResponseListener(ConsentDefinition definition, List<BackendConsent> backendConsents, PostConsentCallback postConsentCallback) {
        this.definition = definition;
        this.backendConsents = backendConsents;
        this.callback = postConsentCallback;
    }

    @Override
    public void onSuccess() {
        CatkLogger.d(" Create BackendConsent: ", "Success");
        callback.onPostConsentSuccess(new Consent(backendConsents, definition));
    }

    @Override
    public void onFailure(ConsentNetworkError error) {
        CatkLogger.d(" Create BackendConsent: ", "Failed : " + error.getCatkErrorCode());
        callback.onPostConsentFailed(definition, new ConsentError(error.getMessage(), error.getCatkErrorCode()));
    }
}
