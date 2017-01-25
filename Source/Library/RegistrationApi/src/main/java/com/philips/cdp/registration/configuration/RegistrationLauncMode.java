package com.philips.cdp.registration.configuration;

import com.philips.cdp.registration.ui.utils.RLog;

/**
 * Created by 310190722 on 1/25/2017.
 */

public enum RegistrationLauncMode {

    DEFAULT("Default"),
    ACCOUNTSETTING("AccountSetting"),
    MARKETINGOPT("MarketingOpt");

    private String mValue;

    private RegistrationLauncMode(final String value) {
        mValue = value;
    }

    public String getValue() {
        RLog.i("RegistrationLauncMode :","value : "+mValue);
        return mValue;
    }
}
