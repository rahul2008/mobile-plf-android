package com.philips.platform.mya.catk.utils;

import com.philips.platform.mya.chi.datamodel.BackendConsent;
import com.philips.platform.mya.chi.datamodel.Consent;
import com.philips.platform.mya.chi.datamodel.ConsentDefinition;
import com.philips.platform.mya.chi.datamodel.ConsentStatus;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class CatkHelper {

    public static ConsentStatus toStatus(boolean status) {
        return status ? ConsentStatus.active : ConsentStatus.rejected;
    }

    public static Consent createConsentFromDefinition(ConsentDefinition definition, ConsentStatus consentStatus) {
        final BackendConsent backendConsent = new BackendConsent(new Locale(definition.getLocale()), consentStatus, definition.getTypes().get(0), definition.getVersion());
        return new Consent(backendConsent, definition);
    }

    public static List<Consent> getSuccessConsentForStatus(ConsentDefinition consentDefinition, ConsentStatus status) {
        return Collections.singletonList(CatkHelper.createConsentFromDefinition(consentDefinition, status));
    }
}
