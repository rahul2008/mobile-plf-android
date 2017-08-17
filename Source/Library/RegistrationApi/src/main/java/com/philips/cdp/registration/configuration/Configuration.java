/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.configuration;


public enum Configuration {

    STAGING("Staging"),
    EVALUATION("Evaluation"),
    DEVELOPMENT("Development"),
    TESTING("Testing"),
    PRODUCTION("Production");

    private String value;

    Configuration(final String pValue) {
        value = pValue;
    }

    public String getValue() {
        return value;
    }
}
