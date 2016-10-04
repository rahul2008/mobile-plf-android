package com.philips.cdp.registration.coppa.utils;

import com.philips.cdp.registration.coppa.listener.UserRegistrationCoppaUIEventListener;
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

    public boolean isAddtoBackStack() {
        return isAddToBackStack;
    }

    public void enableAddtoBackStack(boolean isAddToBackStack) {
        this.isAddToBackStack = isAddToBackStack;
    }

    private boolean isAddToBackStack;

    private boolean isParentalFragment;

    private RegistrationFunction registrationFunction;

    private UserRegistrationCoppaUIEventListener userRegistrationListener;

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


    public void setUserRegistrationCoppaUIEventListener(UserRegistrationCoppaUIEventListener
                                                           userRegistrationListener) {
        this.userRegistrationListener = userRegistrationListener;
    }

    public UserRegistrationCoppaUIEventListener getUserRegistrationUIEventListener() {
        return this.userRegistrationListener;
    }
}
