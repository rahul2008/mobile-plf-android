package com.philips.cdp.registration.ui.utils;

/**
 * It is used to determine to display Opt-in screen in a separate screen or with in  account screen .
 * @since 1.0.0
 */
public enum UIFlow {

    /**
     * Informs `PhilipsRegistration` to display Opt-in in create account screen along with other fields
     * @since 1.0.0
     */
    FLOW_A("OriginalOptInText"),

    /**
     * Informs `PhilipsRegistration` to display Opt-in in separate screen after account creation
     * @since 1.0.0
     */
    FLOW_B("OptInInSeparateScreen");

    private final String flow;

    /**
     * Creates UIFlow instance
     * @param flow  String flow
     * @since 1.0.0
     */
    UIFlow(final String flow) {
        this.flow = flow;
    }

    /**
     * Returns flow value in string
     * @return flow  String flow
     * @since 1.0.0
     */
    public String getValue() {
        return flow;
    }
}
