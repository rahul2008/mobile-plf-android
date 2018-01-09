package com.philips.cdp.registration.configuration;

/**
 * Registration Launch mode for UI lanch mode as default or account settings or Marketing Option .
 * @since 1.0.0
 */
public enum RegistrationLaunchMode {

    /**
     * By enabling User does not accepts to receive philips marketing campaign
     */
    DEFAULT("Default"),
    /**
     * By enabling User accepts to receive philips marketing campaign
     */
    MARKETING_OPT("MarketingOpt");

    private String mValue;

    /**
     *
     * @param value pass either Default or MarketingOpt
     *              By enabling User accepts to receive philips marketing campaign or else does not
     */
    RegistrationLaunchMode(final String value) {
        mValue = value;
    }

    /**
     *
     * @return mValue value of RegistrationLaunchMode
     */
    public String getValue() {
        return mValue;
    }
}
