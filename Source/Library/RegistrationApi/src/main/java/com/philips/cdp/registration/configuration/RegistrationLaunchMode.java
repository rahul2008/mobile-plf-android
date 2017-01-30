package com.philips.cdp.registration.configuration;

import com.philips.cdp.registration.ui.utils.RLog;

public enum RegistrationLaunchMode {

    Default("Default"),
    AccountSettings("AccountSettings"),
    MarketingOpt("MarketingOpt");

    private String mValue;

    RegistrationLaunchMode(final String value) {
        mValue = value;
    }

    public String getValue() {
        return mValue;
    }
}
