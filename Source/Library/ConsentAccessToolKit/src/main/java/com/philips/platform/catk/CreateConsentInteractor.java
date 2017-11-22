/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.catk;

import com.philips.platform.catk.error.ConsentNetworkError;
import com.philips.platform.catk.listener.CreateConsentListener;
import com.philips.platform.catk.mapper.LocaleMapper;
import com.philips.platform.catk.model.Consent;
import com.philips.platform.catk.model.ConsentDefinition;
import com.philips.platform.catk.model.ConsentStatus;
import com.philips.platform.catk.model.RequiredConsent;
import com.philips.platform.catk.utils.CatkLogger;

import java.util.Locale;

public class CreateConsentInteractor {

    public interface Callback {
        void onCreateConsentFailed(ConsentDefinition definition, ConsentNetworkError error);

        void onCreateConsentSuccess(RequiredConsent consent);
    }

    private final ConsentAccessToolKit consentAccessToolKit;

    public CreateConsentInteractor(ConsentAccessToolKit consentAccessToolKit) {
        this.consentAccessToolKit = consentAccessToolKit;
    }

    public void createConsentStatus(ConsentDefinition definition, Callback callback, boolean switchChecked) {
        ConsentStatus consentStatus = switchChecked ? ConsentStatus.active : ConsentStatus.rejected;
        Consent consent = createConsent(definition, consentStatus);
        consentAccessToolKit.createConsent(consent, new CreateConsentResponseListener(definition, consent, callback));
    }

    private Consent createConsent(ConsentDefinition definition, ConsentStatus status) {
        Locale locale = LocaleMapper.toLocale(definition.getLocaleString());
        return new Consent(locale, status, definition.getType(), definition.getVersion());
    }

    static class CreateConsentResponseListener implements CreateConsentListener {

        private final ConsentDefinition definition;
        private final Consent consent;
        private final Callback callback;

        CreateConsentResponseListener(ConsentDefinition definition, Consent consent, Callback callback) {
            this.definition = definition;
            this.consent = consent;
            this.callback = callback;
        }

        @Override
        public void onSuccess() {
            CatkLogger.d(" Create Consent: ", "Success");
            callback.onCreateConsentSuccess(new RequiredConsent(consent, definition));
        }

        @Override
        public void onFailure(ConsentNetworkError error) {
            CatkLogger.d(" Create Consent: ", "Failed : " + error.getCatkErrorCode());
            callback.onCreateConsentFailed(definition, error);
        }
    }
}
