/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.csw.permission;

import java.util.Locale;

import com.philips.platform.catk.ConsentAccessToolKit;
import com.philips.platform.catk.listener.CreateConsentListener;
import com.philips.platform.catk.mapper.LocaleMapper;
import com.philips.platform.catk.model.Consent;
import com.philips.platform.catk.model.ConsentDefinition;
import com.philips.platform.catk.model.ConsentStatus;
import com.philips.platform.csw.utils.CswLogger;

public class CreateConsentInteractor {

    interface Callback {
        void onCreateConsentFailed(ConsentDefinition definition, int errorCode);

        void onCreateConsentSuccess(ConsentDefinition definition, Consent consent, int code);
    }

    private final ConsentAccessToolKit consentAccessToolKit;

    CreateConsentInteractor(ConsentAccessToolKit consentAccessToolKit) {
        this.consentAccessToolKit = consentAccessToolKit;
    }

    void createConsentStatus(ConsentDefinition definition, Callback callback, boolean switchChecked) {
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
        public void onSuccess(int code) {
            CswLogger.d(" Create Consent: ", "Success : " + code);
            callback.onCreateConsentSuccess(definition, consent, code);
        }

        @Override
        public int onFailure(int errorCode) {
            CswLogger.d(" Create Consent: ", "Failed : " + errorCode);
            callback.onCreateConsentFailed(definition, errorCode);
            return errorCode;
        }
    }
}
