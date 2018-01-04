/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.mya.launcher;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.consenthandlerinterface.ConsentHandlerMapping;
import com.philips.platform.uappframework.uappinput.UappDependencies;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to provide dependencies for myaccount.
 *
 * @since 2017.5.0
 */
public class MyaDependencies extends UappDependencies {

    private final List<ConsentHandlerMapping> consentHandlerMappingList;

    public MyaDependencies(AppInfraInterface appInfra, List<ConsentHandlerMapping> consentHandlerMappingList) {
        super(appInfra);
        this.consentHandlerMappingList = consentHandlerMappingList == null ? new ArrayList<ConsentHandlerMapping>() : consentHandlerMappingList;
    }

    public List<ConsentHandlerMapping> getConsentHandlerMappingList() {
        return consentHandlerMappingList;
    }
}
