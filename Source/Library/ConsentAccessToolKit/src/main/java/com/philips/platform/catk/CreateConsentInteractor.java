/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.catk;

import com.philips.platform.catk.listener.CreateConsentListener;
import com.philips.platform.catk.mapper.LocaleMapper;
import com.philips.platform.catk.model.Consent;
import com.philips.platform.catk.model.ConsentDefinition;
import com.philips.platform.catk.model.ConsentStatus;
import com.philips.platform.catk.utils.CatkLogger;

import java.util.Locale;

public class CreateConsentInteractor {

    private final ConsentAccessToolKit consentAccessToolKit;

    public CreateConsentInteractor(ConsentAccessToolKit consentAccessToolKit) {
        this.consentAccessToolKit = consentAccessToolKit;
    }

    public void createConsentStatus(ConsentDefinition definition, boolean switchChecked, CreateConsentListener listener) {
        ConsentStatus consentStatus = switchChecked ? ConsentStatus.active : ConsentStatus.rejected;
        Consent consent = createConsent(definition, consentStatus);
        consentAccessToolKit.createConsent(consent, listener);
    }

    private Consent createConsent(ConsentDefinition definition, ConsentStatus status) {
        Locale locale = LocaleMapper.toLocale(definition.getLocaleString());
        return new Consent(locale, status, definition.getType(), definition.getVersion());
    }
}
