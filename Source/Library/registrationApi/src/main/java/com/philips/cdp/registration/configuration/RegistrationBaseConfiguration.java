
package com.philips.cdp.registration.configuration;

import java.io.InputStream;
import java.util.Scanner;

public class RegistrationBaseConfiguration {

    public JanRainConfiguration janRainConfiguration;

    public PILConfiguration pilConfiguration;

    public SigninProviders signinProviders;

    public Flow flow;


    public HSDPConfiguration hsdpConfiguration;

    public RegistrationBaseConfiguration() {
        janRainConfiguration = new JanRainConfiguration();
        pilConfiguration = new PILConfiguration();
        signinProviders = new SigninProviders();
        flow = new Flow();

    }


    public static String convertStreamToString(InputStream is) {
        Scanner s = new Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }


    public JanRainConfiguration getJanRainConfiguration() {
        return janRainConfiguration;
    }

    public void setJanRainConfiguration(JanRainConfiguration janRainConfiguration) {
        this.janRainConfiguration = janRainConfiguration;
    }

    public PILConfiguration getPilConfiguration() {
        return pilConfiguration;
    }

    public void setPilConfiguration(PILConfiguration pilConfiguration) {
        this.pilConfiguration = pilConfiguration;
    }

    public SigninProviders getSignInProviders() {
        return signinProviders;
    }

    public void setSignInProviders(SigninProviders socialProviders) {
        this.signinProviders = socialProviders;
    }

    public Flow getFlow() {
        return flow;
    }

    public void setFlow(Flow flow) {
        this.flow = flow;
    }

    public HSDPConfiguration getHsdpConfiguration() {
        if (hsdpConfiguration == null) {
            hsdpConfiguration = new HSDPConfiguration();
        }

        return hsdpConfiguration;
    }


    public void setHsdpConfiguration(HSDPConfiguration hsdpConfiguration) {
        this.hsdpConfiguration = hsdpConfiguration;
    }
}
