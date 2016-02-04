
package com.philips.cdp.registration.configuration;

public class JanRainConfiguration {

    private RegistrationClientId clientIds;

    public RegistrationClientId getClientIds() {
        return clientIds;
    }

    public JanRainConfiguration() {

    }

    /**
     * Constructor Janrain Configuaraion with Registartion clientIds
     *
     * @param clientIds Object of RegistrationClientId
     */
    public JanRainConfiguration(RegistrationClientId clientIds) {
        this.clientIds = clientIds;
    }

    /**
     * Set Janrain client Ids
     *
     * @param clientIds Object of RegistrationClientId
     */
    public void setClientIds(RegistrationClientId clientIds) {
        this.clientIds = clientIds;
    }


}
