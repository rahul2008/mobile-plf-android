package com.philips.cdp.registration.ui.utils;

import com.philips.cdp.registration.listener.UserRegistrationListener;
import com.philips.cdp.registration.settings.RegistrationFunction;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;


public class URLaunchInput extends UappLaunchInput {


    private boolean isAccountSettings;

    private RegistrationFunction registrationFunction;

    private UserRegistrationListener userRegistrationListener;

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


    public void setUserRegistrationListener(UserRegistrationListener userRegistrationListener) {
        this.userRegistrationListener = userRegistrationListener;
    }

    public UserRegistrationListener getUserRegistrationListener() {
        return this.userRegistrationListener;
    }
}
