/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.pif.chi;

import java.util.List;

public interface ConsentHandlerInterface {
    void fetchConsentTypeState(final String consentType, final FetchConsentTypeStateCallback callback);

    void fetchConsentTypeStates(List<String> consentTypes, final FetchConsentTypesStateCallback callback);

    void storeConsentTypeState(final String consentType, boolean status, PostConsentTypeCallback callback);
}
