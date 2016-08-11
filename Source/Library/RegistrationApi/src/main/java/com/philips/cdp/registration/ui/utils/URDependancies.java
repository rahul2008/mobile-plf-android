package com.philips.cdp.registration.ui.utils;

import com.philips.cdp.registration.configuration.RegistrationBaseConfiguration;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.uappframework.uappinput.UappDependencies;


public class URDependancies extends UappDependencies {
    private AppInfraInterface appInfraInterface;

    public RegistrationBaseConfiguration getRegistrationConfiguration() {
        return registrationConfiguration;
    }

    public void setRegistrationConfiguration(RegistrationBaseConfiguration registrationConfiguration) {
        this.registrationConfiguration = registrationConfiguration;
    }

    private RegistrationBaseConfiguration registrationConfiguration;

    public AppInfraInterface getAppInfraInterface() {
        return appInfraInterface;
    }

    public void setAppInfraInterface(AppInfraInterface appInfraInterface) {
        this.appInfraInterface = appInfraInterface;
    }



}
