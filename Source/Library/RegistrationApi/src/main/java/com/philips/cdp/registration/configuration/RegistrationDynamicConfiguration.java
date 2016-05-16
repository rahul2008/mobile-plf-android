
package com.philips.cdp.registration.configuration;

public class RegistrationDynamicConfiguration extends RegistrationBaseConfiguration {
    private static RegistrationDynamicConfiguration registrationConfiguration;

    private RegistrationDynamicConfiguration() {

    }

    /**
     * Get Single instance of RegistrationDynamicConfiguration
     *
     * @return RegistrationDynamicConfiguration
     */
    public static RegistrationDynamicConfiguration getInstance() {
        if (registrationConfiguration == null) {
            synchronized (RegistrationDynamicConfiguration.class) {
                if (registrationConfiguration == null) {        // Double checked
                    registrationConfiguration = new RegistrationDynamicConfiguration();
                }
            }
        }
        return registrationConfiguration;
    }

    /**
     * Reset an dynamic configuration
     */
    public void resetDynamicConfiguration() {
        registrationConfiguration = new RegistrationDynamicConfiguration();
    }
}
