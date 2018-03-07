/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.mya.csw;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.consentmanager.ConsentManagerInterface;
import com.philips.platform.pif.chi.datamodel.ConsentDefinition;
import com.philips.platform.uappframework.uappinput.UappDependencies;

import java.util.List;

public class CswDependencies extends UappDependencies {

    private final ConsentManagerInterface consentManagerInterface;
    private final List<ConsentDefinition> consentDefinitionList;


    public CswDependencies(AppInfraInterface appInfra, ConsentManagerInterface consentManagerInterface, List<ConsentDefinition> consentDefinitionList) {
        super(appInfra);
        this.consentManagerInterface = consentManagerInterface;
        this.consentDefinitionList = consentDefinitionList;
    }

    public ConsentManagerInterface getConsentRegistry() {
        return consentManagerInterface;
    }

    public List<ConsentDefinition> getConsentDefinitionList() {
        return consentDefinitionList;
    }
}
