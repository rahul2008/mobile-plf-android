package com.philips.amwelluapp;


import com.philips.platform.uappframework.uappinput.UappLaunchInput;

public class PTHMicroAppLaunchInput extends UappLaunchInput {


    private String welcomeMessage;


    public PTHMicroAppLaunchInput(String welcomeMessage) {
        this.welcomeMessage = welcomeMessage;
    }

    public String getWelcomeMessage() {
        return welcomeMessage;
    }
}
