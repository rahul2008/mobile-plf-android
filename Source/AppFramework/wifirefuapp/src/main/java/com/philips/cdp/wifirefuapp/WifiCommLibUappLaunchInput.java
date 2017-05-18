package com.philips.cdp.wifirefuapp;


import com.philips.platform.uappframework.uappinput.UappLaunchInput;

public class WifiCommLibUappLaunchInput extends UappLaunchInput {


    private String welcomeMessage;


    public WifiCommLibUappLaunchInput(String welcomeMessage) {
        this.welcomeMessage = welcomeMessage;
    }

    public String getWelcomeMessage() {
        return welcomeMessage;
    }
}
