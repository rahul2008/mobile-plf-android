/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.mya.chi;

import com.philips.platform.mya.chi.datamodel.ConsentDefinition;

import java.util.List;

public interface ConsentHandlerInterface {

    void fetchConsentState(ConsentDefinition consentDefinition, final CheckConsentsCallback callback);

    void fetchConsentStates(List<ConsentDefinition> consentDefinitions, final CheckConsentsCallback callback);

    void storeConsentState(final ConsentDefinition definition, boolean status, PostConsentCallback callback);
}
