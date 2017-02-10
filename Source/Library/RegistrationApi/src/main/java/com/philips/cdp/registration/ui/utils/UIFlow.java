package com.philips.cdp.registration.ui.utils;


public enum UIFlow {
    FLOW_A("OriginalOptInText"),
    FLOW_B("OptInInSeparateScreen"),
    FLOW_C("AddBenefitsToOptInInRegistrationScreen");

    private final String flow;

    UIFlow(final String flow) {
        this.flow = flow;
    }

    public String getValue() {
        return flow;
    }
}
