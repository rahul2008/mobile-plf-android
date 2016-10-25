/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.configuration;


public enum Configuration {

    STAGING("STAGING"),
    EVALUATION("EVALUATION"), DEVELOPMENT("DEVELOPMENT"),
    TESTING("TESTING"),
    TEST("TEST"),
    PRODUCTION("PRODUCTION");

    private String value;

    private Configuration(final String pValue) {
        value = pValue;
    }

    public String getValue() {
        System.out.println("Enum value " + value);
        return value;
    }
}
