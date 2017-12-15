package com.philips.platform.consenthandlerinterface;

import com.philips.platform.consenthandlerinterface.datamodel.ConsentDefinition;

import java.util.List;

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
