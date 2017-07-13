package com.philips.platform.samplemicroapp;


import com.philips.platform.uappframework.uappinput.UappLaunchInput;

public class SampleMicroAppLaunchInput extends UappLaunchInput {


    private String welcomeMessage;


    public SampleMicroAppLaunchInput(String welcomeMessage) {
        this.welcomeMessage = welcomeMessage;
    }

    public String getWelcomeMessage() {
        return welcomeMessage;
    }
}
