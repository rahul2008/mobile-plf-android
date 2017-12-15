/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.csw;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.consenthandlerinterface.ConsentHandlerInterface;
import com.philips.platform.uappframework.uappinput.UappDependencies;

public class CswDependencies extends UappDependencies {

    private final ConsentHandlerInterface consentHandlerInterface;

    public CswDependencies(AppInfraInterface appInfra, ConsentHandlerInterface consentHandlerInterface) {
        super(appInfra);
        this.consentHandlerInterface = consentHandlerInterface;
    }

    public ConsentHandlerInterface getConsentHandlerInterface() {
        return consentHandlerInterface;
    }
}
