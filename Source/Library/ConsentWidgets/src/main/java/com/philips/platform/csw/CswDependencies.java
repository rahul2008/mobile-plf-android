package com.philips.platform.csw;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.uappframework.uappinput.UappDependencies;


public class CswDependencies extends UappDependencies {

    public CswDependencies(AppInfraInterface appInfra) {
        super(appInfra);
    }


    private String applicationName;
    private String propositionName;

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getPropositionName() {
        return propositionName;
    }

    public void setPropositionName(String propositionName) {
        this.propositionName = propositionName;
    }

}
