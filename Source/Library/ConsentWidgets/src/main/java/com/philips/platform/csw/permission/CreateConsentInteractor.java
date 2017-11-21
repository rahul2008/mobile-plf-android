package com.philips.platform.csw.permission;

import com.philips.platform.catk.ConsentAccessToolKit;
import com.philips.platform.catk.listener.CreateConsentListener;
import com.philips.platform.catk.mapper.LocaleMapper;
import com.philips.platform.catk.model.Consent;
import com.philips.platform.catk.model.ConsentStatus;
import com.philips.platform.catk.model.ConsentDefinition;
import com.philips.platform.csw.utils.CswLogger;

import java.util.Locale;

public class CreateConsentInteractor implements CreateConsentListener {

    private final ConsentAccessToolKit consentAccessToolKit;

    CreateConsentInteractor(ConsentAccessToolKit consentAccessToolKit) {
        this.consentAccessToolKit = consentAccessToolKit;
    }

    void createConsentStatus(ConsentDefinition definition, boolean switchChecked) {
        ConsentStatus consentStatus = switchChecked ? ConsentStatus.active : ConsentStatus.rejected;
        Consent consent = createConsent(definition, consentStatus);
        consentAccessToolKit.createConsent(consent, this);
    }

    private Consent createConsent(ConsentDefinition definition, ConsentStatus status) {
        Locale locale = LocaleMapper.toLocale(definition.getLocaleString());
        return new Consent(locale, status, definition.getType(), definition.getVersion());
    }

    @Override
    public void onSuccess(int code) {
        CswLogger.d(" Create Consent: ", "Success : " + code);
    }

    @Override
    public int onFailure(int errCode) {
        CswLogger.d(" Create Consent: ", "Failed : " + errCode);
        return errCode;
    }
}
