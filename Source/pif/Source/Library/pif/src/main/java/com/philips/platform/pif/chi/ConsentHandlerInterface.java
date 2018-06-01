/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.pif.chi;

public interface ConsentHandlerInterface {
    /**
     * Fetch the status of the consent type
     * @param consentType given type
     * @param callback callback to be invoked after fetch
     * @since 2018.1.0
     */
    void fetchConsentTypeState(final String consentType, final FetchConsentTypeStateCallback callback);

    /**
     * Store the status of the given consent type
     * @param consentType given consent type
     * @param status given status to store
     * @param version given version
     * @param callback callback to be invoked after store
     * @since 2018.1.0
     */
    void storeConsentTypeState(final String consentType, boolean status, int version, PostConsentTypeCallback callback);

    default void registerConsentChangeListener(ConsentChangeListener consentChangeListener){

    }

    default void unregisterConsentChangeListener(ConsentChangeListener consentChangeListener){

    }

    /**
     * Created by abhishek on 5/21/18.
     */

    interface ConsentChangeListener {
        void onConsentChanged(String consentType, boolean status);
    }
}
