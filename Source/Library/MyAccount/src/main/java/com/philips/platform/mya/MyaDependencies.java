package com.philips.platform.mya;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.uappframework.uappinput.UappDependencies;


public class MyaDependencies extends UappDependencies {

    public MyaDependencies(AppInfraInterface appInfra) {
        super(appInfra);
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

    public String getApplicationName() {
        return applicationName;
    }

    private String applicationName;
    private String propositionName;


}
