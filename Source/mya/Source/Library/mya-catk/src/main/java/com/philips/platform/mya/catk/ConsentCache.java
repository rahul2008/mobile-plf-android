package com.philips.platform.appinfra.consentmanager;

import com.philips.platform.pif.chi.datamodel.ConsentDefinition;
import com.philips.platform.pif.chi.datamodel.ConsentDefinitionStatus;

/**
 * Consent cache implementation.
 *
 * @since 18.2.0s
 */

public class ConsentCache implements ConsentCacheInterface {


    @Override
    public void storeCachedConsentState(ConsentDefinition consentDefinition, ConsentDefinitionStatus status, PostConsentCallback callback) {

    }

    @Override
    public void fetchCachedConsentState(ConsentDefinition consentDefinition, FetchConsentCallback callback) {

    }
}
