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

    public CswDependencies(AppInfraInterface appInfra) {
        super(appInfra);
    }
}
