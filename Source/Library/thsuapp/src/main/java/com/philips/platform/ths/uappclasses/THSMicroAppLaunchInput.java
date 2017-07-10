package com.philips.platform.ths.uappclasses;


import com.philips.platform.uappframework.uappinput.UappLaunchInput;

public class THSMicroAppLaunchInput extends UappLaunchInput {


    private String welcomeMessage;


    public THSMicroAppLaunchInput(String welcomeMessage) {
        this.welcomeMessage = welcomeMessage;
    }

    public String getWelcomeMessage() {
        return welcomeMessage;
    }
}
