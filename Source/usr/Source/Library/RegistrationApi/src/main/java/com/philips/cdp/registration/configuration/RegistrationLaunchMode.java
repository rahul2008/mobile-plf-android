package com.philips.cdp.registration.configuration;

/**
 * Registration Launch mode indicates to launch USR in  either of these modes(UI launch mode(default) , account settings mode , in Marketing Option mode).
 * @since 1.0.0
 */
public enum RegistrationLaunchMode {

    /**
     * By enabling User does not accepts to receive philips marketing campaign
     * @since 1.0.0
     */
    DEFAULT("Default"),
    /**
     * By enabling User accepts to receive philips marketing campaign
     * @since 1.0.0
     */
    MARKETING_OPT("MarketingOpt");

    private String mValue;

    /**
     *
     * @param value pass either Default or MarketingOpt
     *              By enabling User accepts to receive philips marketing campaign or else does not
     *              @since 1.0.0
     */
    RegistrationLaunchMode(final String value) {
        mValue = value;
    }

    /**
     *
     * @return mValue value of RegistrationLaunchMode
     * @since 1.0.0
     */
    public String getValue() {
        return mValue;
    }
}
