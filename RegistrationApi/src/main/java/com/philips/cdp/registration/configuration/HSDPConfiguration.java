package com.philips.cdp.registration.configuration;

/**
 * Created by 310190722 on 8/25/2015.
 */
public class HSDPConfiguration {

    private String applicationName;

    private Development developmentIds;

    public Development getDevelopmentIds() {
        return developmentIds;
    }

    public void setDevelopmentIds(Development developmentIds) {
        this.developmentIds = developmentIds;
    }
    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

}
