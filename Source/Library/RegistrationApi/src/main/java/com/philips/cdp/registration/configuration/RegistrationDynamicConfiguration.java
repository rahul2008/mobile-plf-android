
package com.philips.cdp.registration.configuration;

public class RegistrationDynamicConfiguration extends RegistrationBaseConfiguration {
    private static volatile RegistrationDynamicConfiguration registrationConfiguration;

    private RegistrationDynamicConfiguration() {

    }

    /**
     * Get Single instance of RegistrationDynamicConfiguration
     *
     * @return RegistrationDynamicConfiguration
     */
    public static synchronized RegistrationDynamicConfiguration getInstance() {
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
    public synchronized void  resetDynamicConfiguration() {
        registrationConfiguration = new RegistrationDynamicConfiguration();
    }
}
