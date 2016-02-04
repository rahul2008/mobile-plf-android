package com.philips.cdp.registration.configuration;

import java.util.HashMap;


public class HSDPConfiguration {
    private HashMap<Configuration, HSDPInfo> hsdpInfos = new HashMap<>();

    /**
     * Get All HSDP information with all configuration
     *
     * @return Map of Configuration and its informantion.
     */
    public HashMap<Configuration, HSDPInfo> getHsdpInfos() {
        return hsdpInfos;
    }

    /**
     * Set HSDP Informations for different Configurations
     *
     * @param hsdpInfos Map of Configuration and its informantion.
     */
    public void setHsdpInfos(HashMap<Configuration, HSDPInfo> hsdpInfos) {
        this.hsdpInfos = hsdpInfos;
    }

    /**
     * Get HSDP information for specified configuration
     *
     * @param configuration
     * @return HSDPInfo Object
     */
    public HSDPInfo getHSDPInfo(Configuration configuration) {
        return hsdpInfos.get(configuration);
    }

    /**
     * Set HSDP Information for specific Configuraton
     *
     * @param configuration
     * @param hsdpInfo      HSDP Information
     */
    public void setHSDPInfo(Configuration configuration, HSDPInfo hsdpInfo) {
        hsdpInfos.put(configuration, hsdpInfo);
    }

}
