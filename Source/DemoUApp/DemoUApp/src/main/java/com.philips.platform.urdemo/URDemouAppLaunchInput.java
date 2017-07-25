
package com.philips.platform.urdemo;


import com.philips.cdp.registration.configuration.RegistrationLaunchMode;
import com.philips.cdp.registration.listener.UserRegistrationUIEventListener;
import com.philips.cdp.registration.settings.RegistrationFunction;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;

/**
 * USR Demo App Launch input
 */
public class URDemouAppLaunchInput extends UappLaunchInput {

    private RegistrationLaunchMode registrationLaunchMode;

    public boolean isAddtoBackStack() {
        return isAddToBackStack;
    }

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

}
