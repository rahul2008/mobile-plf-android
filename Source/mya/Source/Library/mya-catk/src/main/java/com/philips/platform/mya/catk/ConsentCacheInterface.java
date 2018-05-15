package com.philips.platform.mya.catk;

import com.philips.platform.mya.catk.datamodel.CachedConsentStatus;
import com.philips.platform.pif.chi.ConsentError;
import com.philips.platform.pif.chi.PostConsentTypeCallback;
import com.philips.platform.pif.chi.datamodel.ConsentStates;

/**
 * This interface is for getting and storing consents from cache.
 * The consent cache will be a read only cache.
 * The access to consent will first look up in cache retrieve from cache.
 * Only if the record is expired and user is online then the call to backend is made. Expiry period is configurable
 * All changes to consent will directly go the backend and on success will update the cache.
 *
 * @since 18.2.0
 */

interface ConsentCacheInterface {
    /**
     *  Get status of consent from cache.
     *
     * @param consentType consent type whose status must be fetched from the cache
     * @since 18.2.0
     */
    CachedConsentStatus fetchConsentTypeState(final String consentType);

    /**
     *  Store status of consentDefinition in cache
     *
     * @param consentType consent type whose status must be cached
     * @param status
     * @param version
     * @since 18.2.0
     */
    void storeConsentTypeState(final String consentType, ConsentStates status, int version);

    /**
     * Clear cache
     *
     * @since 18.2.0
     */
    void clearCache();
}


