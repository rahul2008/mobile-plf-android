package com.philips.cdp.registration.configuration;


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
        System.out.println("Enum value " + value);
        return value;
    }
}
