
package com.philips.platform.mya.demouapp;


import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.uappframework.uappinput.UappDependencies;

/**
 * MyAccount Demo App Dependances
 */
public class MyaDemouAppDependencies extends UappDependencies {

    public MyaDemouAppDependencies(final AppInfraInterface appInfra) {
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
