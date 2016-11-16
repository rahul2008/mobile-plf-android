package com.philips.cdp.registration.ui.utils;


public enum UIFlow {
    STRING_EXPERIENCE_A("Experience A"),
    STRING_EXPERIENCE_B("Experience B"),
    STRING_EXPERIENCE_C("Experience C");

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
