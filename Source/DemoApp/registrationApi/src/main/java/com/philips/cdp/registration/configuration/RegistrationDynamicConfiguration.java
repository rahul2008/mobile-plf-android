
package com.philips.cdp.registration.configuration;

public class RegistrationDynamicConfiguration extends RegistrationBaseConfiguration {
    private static RegistrationDynamicConfiguration registrationConfiguration;

    private RegistrationDynamicConfiguration() {
    }

    public static RegistrationDynamicConfiguration getInstance() {
        if (registrationConfiguration == null) {
            registrationConfiguration = new RegistrationDynamicConfiguration();
        }
        return registrationConfiguration;
    }


    public void resetDynamicConfiguration() {
        registrationConfiguration = new RegistrationDynamicConfiguration();
    }


}
