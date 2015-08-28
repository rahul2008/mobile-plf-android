package com.philips.cdp.registration.configuration;

/**
 * Created by 310190722 on 8/25/2015.
 */
public class Development {

    private String shared;
    private String secret;
    private String baseURL;

    public String getShared() {
        return shared;
    }

    public void setShared(String shared) {
        this.shared = shared;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getBaseURL() {
        return baseURL;
    }

    public void setBaseURL(String baseURL) {
        this.baseURL = baseURL;
    }
}
