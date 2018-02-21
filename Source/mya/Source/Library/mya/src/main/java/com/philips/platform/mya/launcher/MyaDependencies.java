/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.mya.launcher;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.pif.chi.ConsentRegistryInterface;
import com.philips.platform.pif.chi.datamodel.ConsentDefinition;
import com.philips.platform.uappframework.uappinput.UappDependencies;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to provide dependencies for myaccount.
 *
 * @since 2017.5.0
 */
public class MyaDependencies extends UappDependencies {

    private final ConsentRegistryInterface consentRegistryInterface;
    private final List<ConsentDefinition> consentDefinitionList;

    public MyaDependencies(AppInfraInterface appInfra, ConsentRegistryInterface consentRegistryInterface, List<ConsentDefinition> consentDefinitionList) {
        super(appInfra);
        this.consentRegistryInterface = consentRegistryInterface;
        this.consentDefinitionList = consentDefinitionList == null ? new ArrayList<ConsentDefinition>() : consentDefinitionList;
    }

    public ConsentRegistryInterface getConsentRegistryInterface() {
        return consentRegistryInterface;
    }

    public List<ConsentDefinition> getConsentDefinitionList() {
        return consentDefinitionList;
    }
}
