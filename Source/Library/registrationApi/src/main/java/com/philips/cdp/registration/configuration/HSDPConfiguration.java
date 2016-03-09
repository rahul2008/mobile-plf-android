package com.philips.cdp.registration.configuration;

import java.util.HashMap;


public class HSDPConfiguration {
    private HashMap<Configuration, HSDPInfo> hsdpInfos = new HashMap<>();

    public HashMap<Configuration, HSDPInfo> getHsdpInfos() {
        return hsdpInfos;
    }

    public void setHsdpInfos(HashMap<Configuration, HSDPInfo> hsdpInfos) {
        this.hsdpInfos = hsdpInfos;
    }


    public HSDPInfo getHSDPInfo(Configuration configuration) {
        return hsdpInfos.get(configuration);
    }

    public void setHSDPInfo(Configuration configuration, HSDPInfo hsdpInfo) {
        hsdpInfos.put(configuration, hsdpInfo);
    }

}
