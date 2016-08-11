package com.philips.cdp.registration.ui.utils;

import com.philips.cdp.registration.settings.RegistrationFunction;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;

/**
 * Created by vinayak on 11/08/16.
 */
public class URLaunchInput extends UappLaunchInput {


    private boolean isAccountSettings;

    private RegistrationFunction registrationFunction;

    public RegistrationFunction getRegistrationFunction() {
        return registrationFunction;
    }

    public void setRegistrationFunction(RegistrationFunction registrationFunction) {
        this.registrationFunction = registrationFunction;
    }

    public void setAccountSettings(boolean isAccountSettings) {
        this.isAccountSettings = isAccountSettings;
    }

    public boolean isAccountSettings() {
        return isAccountSettings;
    }





}
