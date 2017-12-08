package com.philips.cdp.registration.coppa.utils;

import com.philips.cdp.registration.configuration.RegistrationLaunchMode;
import com.philips.cdp.registration.listener.UserRegistrationUIEventListener;
import com.philips.cdp.registration.settings.RegistrationFunction;
import com.philips.cdp.registration.ui.utils.RegistrationContentConfiguration;
import com.philips.cdp.registration.ui.utils.UIFlow;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;


public class CoppaLaunchInput extends UappLaunchInput {


    private RegistrationLaunchMode registrationLaunchMode;


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

    private RegistrationContentConfiguration registrationContentConfiguration;

    public void setRegistrationContentConfiguration(RegistrationContentConfiguration registrationContentConfiguration){
        this.registrationContentConfiguration = registrationContentConfiguration;
    }

    public RegistrationContentConfiguration getRegistrationContentConfiguration(){
        return this.registrationContentConfiguration;
    }

    UIFlow uiFlow;

    public void setUIFlow(UIFlow uiFlow){
        this.uiFlow = uiFlow;
    }

    public UIFlow getUIflow(){
        return  uiFlow;
    }
}
