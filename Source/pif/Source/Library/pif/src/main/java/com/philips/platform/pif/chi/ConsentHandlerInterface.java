/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.pif.chi;

public interface ConsentHandlerInterface {
    void fetchConsentTypeState(final String consentType, final FetchConsentTypeStateCallback callback);

    void storeConsentTypeState(final String consentType, boolean status, int version, PostConsentTypeCallback callback);
}
