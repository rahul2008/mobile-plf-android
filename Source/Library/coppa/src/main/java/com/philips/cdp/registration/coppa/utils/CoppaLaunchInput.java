package com.philips.cdp.registration.coppa.utils;

import com.philips.cdp.registration.listener.UserRegistrationUIEventListener;
import com.philips.cdp.registration.settings.RegistrationFunction;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;


public class CoppaLaunchInput extends UappLaunchInput {


    private boolean isAccountSettings;

    public boolean isParentalFragment() {
        return isParentalFragment;
    }

    public void setParentalFragment(boolean parentalFragment) {
        isParentalFragment = parentalFragment;
    }

    private boolean isParentalFragment;

    private RegistrationFunction registrationFunction;

    private UserRegistrationUIEventListener userRegistrationListener;

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


    public void setUserRegistrationUIEventListener(UserRegistrationUIEventListener
                                                           userRegistrationListener) {
        this.userRegistrationListener = userRegistrationListener;
    }

    public UserRegistrationUIEventListener getUserRegistrationUIEventListener() {
        return this.userRegistrationListener;
    }
}
