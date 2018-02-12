/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.mya.chi;

import java.util.List;

import com.philips.platform.mya.chi.datamodel.ConsentDefinition;

public class ConsentConfiguration {
    private final List<ConsentDefinition> consentDefinitionList;
    private final ConsentHandlerInterface consentHandlerInterface;

    public ConsentConfiguration(List<ConsentDefinition> consentDefinitionList, ConsentHandlerInterface consentHandlerInterface) {
        this.consentDefinitionList = consentDefinitionList;
        this.consentHandlerInterface = consentHandlerInterface;
    }

    public List<ConsentDefinition> getConsentDefinitionList() {
        return consentDefinitionList;
    }

    public ConsentHandlerInterface getHandlerInterface() {
        return consentHandlerInterface;
    }
}
