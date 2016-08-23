package com.philips.cdp.registration.coppa.utils;

import android.content.Context;

import com.philips.cdp.registration.configuration.RegistrationBaseConfiguration;
import com.philips.cdp.registration.configuration.RegistrationDynamicConfiguration;
import com.philips.platform.uappframework.uappinput.UappSettings;


public class CoppaSettings extends UappSettings {

    private RegistrationBaseConfiguration registrationConfiguration;

    public CoppaSettings(Context applicationContext) {
        super(applicationContext);
    }

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

}
