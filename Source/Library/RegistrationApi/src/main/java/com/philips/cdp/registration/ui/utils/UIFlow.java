package com.philips.cdp.registration.ui.utils;


public enum UIFlow {
    STRING_EXPERIENCE_A("OriginalOptInText"),
    STRING_EXPERIENCE_B("OptInInSeparateScreen"),
    STRING_EXPERIENCE_C("AddBenefitsToOptInInRegistrationScreen");

    private final String text;

    /**
     * @param text
     */
    private UIFlow(final String text) {
        this.text = text;
    }

    /* (non-Javadoc)
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return text;
    }
}
