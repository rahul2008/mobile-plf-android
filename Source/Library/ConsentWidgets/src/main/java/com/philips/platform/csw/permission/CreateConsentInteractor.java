package com.philips.platform.csw.permission;

import com.philips.platform.catk.ConsentAccessToolKit;
import com.philips.platform.catk.listener.CreateConsentListener;
import com.philips.platform.catk.mapper.DtoToConsentMapper;
import com.philips.platform.catk.model.Consent;
import com.philips.platform.catk.model.ConsentStatus;
import com.philips.platform.csw.ConsentDefinition;
import com.philips.platform.csw.utils.CswLogger;

import java.util.Locale;

/**
 * Created by Entreco on 17/11/2017.
 */

public class CreateConsentInteractor implements CreateConsentListener {

    private final ConsentAccessToolKit consentAccessToolKit;

    CreateConsentInteractor() {
        consentAccessToolKit = ConsentAccessToolKit.getInstance();
    }

    void createConsentStatus(ConsentView consentView, boolean switchChecked) {
        ConsentStatus consentStatus = switchChecked ? ConsentStatus.active : ConsentStatus.rejected;
        Consent consent = createConsent(consentView.getDefinition(), consentStatus);
        consentAccessToolKit.createConsent(consent, this);
    }

    private Consent createConsent(ConsentDefinition definition, ConsentStatus status) {
        Locale locale = DtoToConsentMapper.getLocale(definition.getLocale());
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
