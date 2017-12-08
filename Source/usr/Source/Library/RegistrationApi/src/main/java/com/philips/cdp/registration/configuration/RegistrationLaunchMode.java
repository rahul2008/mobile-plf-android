package com.philips.cdp.registration.configuration;

/**
 * Registrtaion Launch mode for UI lanch mode as default or account settings or Marketiitng Option .
 */
public enum RegistrationLaunchMode {

    DEFAULT("Default"),

    MARKETING_OPT("MarketingOpt");

    private String mValue;

    RegistrationLaunchMode(final String value) {
        mValue = value;
    }

    public String getValue() {
        return mValue;
    }
}
