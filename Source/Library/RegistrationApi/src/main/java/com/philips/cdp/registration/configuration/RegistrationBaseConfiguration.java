
/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

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

    /**
     * Get Janrain Configuration
     *
     * @return RegistrationDynamicConfiguration
     */
    public JanRainConfiguration getJanRainConfiguration() {
        return janRainConfiguration;
    }

    /**
     * Set Janrain Configuration
     *
     * @param janRainConfiguration
     */
    public void setJanRainConfiguration(JanRainConfiguration janRainConfiguration) {
        this.janRainConfiguration = janRainConfiguration;
    }

    /**
     * Get PIL Configuration
     *
     * @return PILConfiguration
     */
    public PILConfiguration getPilConfiguration() {
        return pilConfiguration;
    }

    /**
     * Set PIL Configuration
     *
     * @param pilConfiguration
     */
    public void setPilConfiguration(PILConfiguration pilConfiguration) {
        this.pilConfiguration = pilConfiguration;
    }

    /**
     * Get Sign in providers
     *
     * @return SigninProviders
     */
    public SigninProviders getSignInProviders() {
        return signinProviders;
    }

    /**
     * Set Social providers
     *
     * @param socialProviders
     */
    public void setSignInProviders(SigninProviders socialProviders) {
        this.signinProviders = socialProviders;
    }

    /**
     * Get Flow
     *
     * @return Flow
     */
    public Flow getFlow() {
        return flow;
    }

    /**
     * Set Flow
     *
     * @param flow
     */
    public void setFlow(Flow flow) {
        this.flow = flow;
    }

    /**
     * Get HSDP configuration
     *
     * @return HSDPConfiguration
     */
    public HSDPConfiguration getHsdpConfiguration() {
        if (hsdpConfiguration == null) {
            hsdpConfiguration = new HSDPConfiguration();
        }

        return hsdpConfiguration;
    }

    /**
     * Set HSDP configuration
     *
     * @param hsdpConfiguration
     */
    public void setHsdpConfiguration(HSDPConfiguration hsdpConfiguration) {
        this.hsdpConfiguration = hsdpConfiguration;
    }
}
