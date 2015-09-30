package com.philips.cdp.registration.configuration;

import java.util.HashMap;

/**
 * Created by 310190722 on 8/25/2015.
 */
public class HSDPConfiguration {

    private String applicationName;

    private HashMap<String, HSDPClientId> hsdpClientIds;


    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getApplicationName() {
        return applicationName;
    }


    public void setHsdpClientIds(HashMap<String, HSDPClientId> hsdpClientIds) {
        this.hsdpClientIds = hsdpClientIds;
    }

    public HSDPClientId getHSDPClientId(String environmentType) {
        if (hsdpClientIds == null) {
            return null;
        }
        return hsdpClientIds.get(environmentType);
    }
}
