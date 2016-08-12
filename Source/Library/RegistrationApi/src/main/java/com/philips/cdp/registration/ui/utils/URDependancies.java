package com.philips.cdp.registration.ui.utils;

import com.philips.cdp.registration.configuration.RegistrationBaseConfiguration;
import com.philips.cdp.registration.configuration.RegistrationDynamicConfiguration;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.uappframework.uappinput.UappDependencies;


public class URDependancies extends UappDependencies {
    private AppInfraInterface appInfraInterface;

    public RegistrationBaseConfiguration getRegistrationConfiguration() {
        return registrationConfiguration;
    }

    public void setRegistrationConfiguration(RegistrationBaseConfiguration registrationConfiguration) {
        this.registrationConfiguration = registrationConfiguration;
        setDynamicConfiguration(registrationConfiguration);
    }

    private void setDynamicConfiguration(RegistrationBaseConfiguration registrationConfiguration) {
        RegistrationDynamicConfiguration.getInstance().
                setJanRainConfiguration(registrationConfiguration.getJanRainConfiguration());

        RegistrationDynamicConfiguration.getInstance().
                setPilConfiguration(registrationConfiguration. getPilConfiguration());

        RegistrationDynamicConfiguration.getInstance().
                setFlow(registrationConfiguration.getFlow());

        RegistrationDynamicConfiguration.getInstance().
                setSignInProviders(registrationConfiguration.getSignInProviders());

        RegistrationDynamicConfiguration.getInstance().
                setHsdpConfiguration(registrationConfiguration.getHsdpConfiguration());
    }

    private RegistrationBaseConfiguration registrationConfiguration;

    public AppInfraInterface getAppInfraInterface() {
        return appInfraInterface;
    }

    public void setAppInfraInterface(AppInfraInterface appInfraInterface) {
        this.appInfraInterface = appInfraInterface;
    }



}
