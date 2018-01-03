/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.csw;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.mya.chi.ConsentConfiguration;
import com.philips.platform.uappframework.uappinput.UappDependencies;

import java.util.List;

public class CswDependencies extends UappDependencies {

    private final List<ConsentConfiguration> consentConfigurationList;

    public CswDependencies(AppInfraInterface appInfra, List<ConsentConfiguration> consentConfigurationList) {
        super(appInfra);
        this.consentConfigurationList = consentConfigurationList;
    }

    public List<ConsentConfiguration> getConsentConfigurationList() {
        return consentConfigurationList;
    }
}
