
package com.philips.cdp.registration.configuration;

public class PILConfiguration {

    private String micrositeId;

    private String registrationEnvironment;

    private String campaignID;

    /**
     * Get Microsite Id
     *
     * @return String
     */
    public String getMicrositeId() {
        return micrositeId;
    }

    /**
     * Set Microsite Id
     *
     * @param micrositeId String
     */
    public void setMicrositeId(String micrositeId) {
        this.micrositeId = micrositeId;
    }

    /**
     * Get Registarion Environment
     *
     * @return String
     */
    public String getRegistrationEnvironment() {
        return registrationEnvironment;
    }


    /**
     * Set Registration Environment
     *
     * @param registrationEnvironment
     */
    public void setRegistrationEnvironment(final Configuration registrationEnvironment) {
        this.registrationEnvironment = registrationEnvironment.getValue();
    }


    public String getCampaignID() {
        return campaignID;
    }

    public void setCampaignID(String campaignID) {
        this.campaignID = campaignID;
    }

}
