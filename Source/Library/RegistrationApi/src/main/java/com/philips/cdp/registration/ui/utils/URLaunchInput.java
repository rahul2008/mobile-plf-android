package com.philips.cdp.registration.ui.utils;

import com.philips.cdp.registration.configuration.RegistrationLaunchMode;
import com.philips.cdp.registration.listener.UserRegistrationUIEventListener;
import com.philips.cdp.registration.settings.RegistrationFunction;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;


public class URLaunchInput extends UappLaunchInput {

    private RegistrationLaunchMode registrationLaunchMode;

    private RegistrationContentConfiguration registrationContentConfiguration;

    public boolean isAddtoBackStack() {
        return isAddToBackStack;
    }

    @Deprecated
    private boolean isAccountSettings;

    public void enableAddtoBackStack(boolean isAddToBackStack) {
        this.isAddToBackStack = isAddToBackStack;
    }

    private boolean isAddToBackStack;

    private RegistrationFunction registrationFunction;

    private UserRegistrationUIEventListener userRegistrationListener;

    public RegistrationFunction getRegistrationFunction() {
        return registrationFunction;
    }

    public void setRegistrationFunction(RegistrationFunction registrationFunction) {
        this.registrationFunction = registrationFunction;
    }

    public void setUserRegistrationUIEventListener(UserRegistrationUIEventListener
                                                           userRegistrationListener) {
        this.userRegistrationListener = userRegistrationListener;
    }

    public UserRegistrationUIEventListener getUserRegistrationUIEventListener() {
        return this.userRegistrationListener;
    }

    public RegistrationLaunchMode getEndPointScreen() {
        return registrationLaunchMode;
    }

    public void setEndPointScreen(RegistrationLaunchMode registrationLaunchMode) {
        this.registrationLaunchMode = registrationLaunchMode;
    }

    public void setRegistrationContentConfiguration(RegistrationContentConfiguration registrationContentConfiguration){
        this.registrationContentConfiguration = registrationContentConfiguration;
    }

    public RegistrationContentConfiguration getRegistrationContentConfiguration(){
        return this.registrationContentConfiguration;
    }

    @Deprecated
    public void setAccountSettings(boolean isAccountSettings) {
        this.isAccountSettings = isAccountSettings;
    }

    @Deprecated
    public boolean isAccountSettings() {
        return isAccountSettings;
    }

}
