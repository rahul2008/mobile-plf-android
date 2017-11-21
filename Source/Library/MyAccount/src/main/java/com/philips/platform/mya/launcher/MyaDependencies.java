/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.mya.launcher;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.csw.ConsentDefinition;
import com.philips.platform.uappframework.uappinput.UappDependencies;

import java.util.ArrayList;
import java.util.List;

public class MyaDependencies extends UappDependencies {

    public MyaDependencies(AppInfraInterface appInfra) {
        super(appInfra);
    }

    // TODO: Deepthi, why do we need app and proposition name to launch MYA

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getPropositionName() {
        return propositionName;
    }

    public void setPropositionName(String propositionName) {
        this.propositionName = propositionName;
    }

    public String getApplicationName() {
        return applicationName;
    }

    private String applicationName;
    private String propositionName;
}
