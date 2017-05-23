package com.example.cdpp.bluelibexampleapp.uapp;


import com.philips.platform.uappframework.uappinput.UappLaunchInput;

public class BleDemoAppMicroAppLaunchInput extends UappLaunchInput {


    private String welcomeMessage;


    public BleDemoAppMicroAppLaunchInput(String welcomeMessage) {
        this.welcomeMessage = welcomeMessage;
    }

    public String getWelcomeMessage() {
        return welcomeMessage;
    }
}
