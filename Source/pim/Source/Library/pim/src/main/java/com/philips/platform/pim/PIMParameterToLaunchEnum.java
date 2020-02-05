package com.philips.platform.pim;

public enum PIMParameterToLaunchEnum {
    PIM_ANALYTICS_CONSENT("analytics"),
    PIM_AB_TESTING_CONSENT("ab_testing");
    public String pimConsent;


    PIMParameterToLaunchEnum(String pimConsent) {
        this.pimConsent = pimConsent;
    }
}
