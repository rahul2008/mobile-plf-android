package com.philips.cdp.registration.configuration;

import com.philips.cdp.registration.ui.utils.RLog;

public enum RegistrationLaunchMode {

    DEFAULT("Default"),
    ACCOUNT_SETTINGS("AccountSettings"),
    MARKETING_OPT("MarketingOpt");

    private String mValue;

    RegistrationLaunchMode(final String value) {
        mValue = value;
    }

    public String getValue() {
        return mValue;
    }
}
