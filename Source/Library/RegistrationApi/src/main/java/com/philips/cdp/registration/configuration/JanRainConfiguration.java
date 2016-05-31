
/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

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

    public String getClientId(Configuration configurationType) {

        switch (configurationType) {
            case EVALUATION:
                return getClientIds().getEvaluationId();
            case DEVELOPMENT:
                return getClientIds().getDevelopmentId();
            case PRODUCTION:
                return getClientIds().getProductionId();
            case STAGING:
                return getClientIds().getStagingId();
            case TESTING:
                return getClientIds().getTestingId();
            default:
                return null;

        }

    }

}
