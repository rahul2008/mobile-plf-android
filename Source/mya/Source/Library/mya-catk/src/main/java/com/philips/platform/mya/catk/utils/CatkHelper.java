package com.philips.platform.mya.catk.utils;


import com.philips.platform.pif.chi.datamodel.BackendConsent;
import com.philips.platform.pif.chi.datamodel.Consent;
import com.philips.platform.pif.chi.datamodel.ConsentDefinition;
import com.philips.platform.pif.chi.datamodel.ConsentStatus;

import java.util.Locale;

public class CatkHelper {

    public static ConsentStatus toStatus(boolean enableDeviceStorage) {
        return enableDeviceStorage ? ConsentStatus.active : ConsentStatus.rejected;
    }

    public static Consent createConsentFromDefinition(ConsentDefinition definition, ConsentStatus consentStatus, String consentLanguage) {
        final BackendConsent backendConsent = new BackendConsent(consentLanguage, consentStatus, definition.getTypes().get(0), definition.getVersion());
        return new Consent(backendConsent, definition);
    }
}
