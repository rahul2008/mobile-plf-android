
/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.baseapp.screens.utility;

public enum AppStateConfiguration {
    STAGING("STAGING"),
    DEVELOPMENT("DEVELOPMENT"),
    TEST("TEST");

    private String value;

    AppStateConfiguration(String pValue) {
        this.value = pValue;
    }

    public String getValue() {
        return this.value;
    }
}
