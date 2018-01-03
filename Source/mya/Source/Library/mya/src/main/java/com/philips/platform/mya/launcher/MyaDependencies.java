/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.mya.launcher;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.mya.chi.ConsentConfiguration;
import com.philips.platform.uappframework.uappinput.UappDependencies;

import java.util.ArrayList;
import java.util.List;

public class MyaDependencies extends UappDependencies {

    private final List<ConsentConfiguration> consentConfigurationList;

    public MyaDependencies(AppInfraInterface appInfra, List<ConsentConfiguration> consentConfigurationList) {
        super(appInfra);
        this.consentConfigurationList = consentConfigurationList == null ? new ArrayList<ConsentConfiguration>() : consentConfigurationList;
    }

    public List<ConsentConfiguration> getConsentConfigurationList() {
        return consentConfigurationList;
    }
}
