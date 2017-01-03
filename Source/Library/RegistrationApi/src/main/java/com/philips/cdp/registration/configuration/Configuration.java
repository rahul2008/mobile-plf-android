package com.philips.cdp.registration.configuration;


import com.philips.cdp.registration.ui.utils.RLog;

public enum Configuration {

    STAGING("Staging"),
    EVALUATION("Evaluation"), DEVELOPMENT("Development"),
    TESTING("Testing"),
    PRODUCTION("Production");

    private String value;

    private Configuration(final String pValue) {
        value = pValue;
    }

    public String getValue() {
        RLog.i("Enum value :","Environment : "+value);
        return value;
    }
}
