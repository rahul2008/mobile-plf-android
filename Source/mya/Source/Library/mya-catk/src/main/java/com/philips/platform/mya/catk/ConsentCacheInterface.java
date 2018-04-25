package com.philips.platform.appinfra.consentmanager;

import com.philips.platform.pif.chi.datamodel.ConsentDefinition;
import com.philips.platform.pif.chi.datamodel.ConsentDefinitionStatus;

/**
 * This interface is for getting and storing consents from cache.
 * The consent cache will be a read only cache.
 * The access to consent will first look up in cache retrieve from cache.
 * Only if the record is expired and user is online then the call to backend is made. Expiry period is configurable
 * All changes to consent will directly go the backend and on success will update the cache.
 *
 * @since 18.2.0
 */

public interface ConsentCacheInterface {
    /**
     * Store status of consentDefinition in cache
     * @param consentDefinition consentDefinition whos status must be cached
     * @param status            status of the consentDefinition as evaluated by consentManager
     * @param callback          callback when request is pro .
     * @since 18.2.0
     */
    public void storeCachedConsentState(final ConsentDefinition consentDefinition, ConsentDefinitionStatus status, PostConsentCallback callback);

    /**
     * Get status of consent from cache.
     * @param consentDefinition consentDefinition whos status must be fetched
     * @param callback callback when consent is fetched successfully
     */
    public void fetchCachedConsentState(ConsentDefinition consentDefinition, final FetchConsentCallback callback);

}
