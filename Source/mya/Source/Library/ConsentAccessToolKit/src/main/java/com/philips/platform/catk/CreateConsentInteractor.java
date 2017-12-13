/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.catk;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.philips.platform.catk.error.ConsentNetworkError;
import com.philips.platform.catk.listener.CreateConsentListener;
import com.philips.platform.catk.mapper.LocaleMapper;
import com.philips.platform.catk.model.BackendConsent;
import com.philips.platform.catk.model.ConsentDefinition;
import com.philips.platform.catk.model.ConsentStatus;
import com.philips.platform.catk.model.Consent;
import com.philips.platform.catk.utils.CatkLogger;

public class CreateConsentInteractor {

    public interface Callback {
        void onCreateConsentFailed(ConsentDefinition definition, ConsentNetworkError error);

        void onCreateConsentSuccess(Consent consent);
    }

    private final ConsentAccessToolKit consentAccessToolKit;

    public CreateConsentInteractor(ConsentAccessToolKit consentAccessToolKit) {
        this.consentAccessToolKit = consentAccessToolKit;
    }

    public void createConsentStatus(ConsentDefinition definition, Callback callback, boolean switchChecked) {
        ConsentStatus consentStatus = switchChecked ? ConsentStatus.active : ConsentStatus.rejected;
        List<BackendConsent> backendConsents = createConsents(definition, consentStatus);
        consentAccessToolKit.createConsent(backendConsents, new CreateConsentResponseListener(definition, backendConsents, callback));
    }

    private List<BackendConsent> createConsents(ConsentDefinition definition, ConsentStatus status) {
        Locale locale = LocaleMapper.toLocale(definition.getLocale());
        List<BackendConsent> backendConsents = new ArrayList<>();
        List<String> types = definition.getTypes();
        for (String type:types) {
            backendConsents.add(new BackendConsent(locale, status, type, definition.getVersion()));
        }
        return backendConsents;
    }

    static class CreateConsentResponseListener implements CreateConsentListener {

        private final ConsentDefinition definition;
        private final List<BackendConsent> backendConsents;
        private final Callback callback;

        CreateConsentResponseListener(ConsentDefinition definition, List<BackendConsent> backendConsents, Callback callback) {
            this.definition = definition;
            this.backendConsents = backendConsents;
            this.callback = callback;
        }

        @Override
        public void onSuccess() {
            CatkLogger.d(" Create BackendConsent: ", "Success");
            callback.onCreateConsentSuccess(new Consent(backendConsents, definition));
        }

        @Override
        public void onFailure(ConsentNetworkError error) {
            CatkLogger.d(" Create BackendConsent: ", "Failed : " + error.getCatkErrorCode());
            callback.onCreateConsentFailed(definition, error);
        }
    }
}
