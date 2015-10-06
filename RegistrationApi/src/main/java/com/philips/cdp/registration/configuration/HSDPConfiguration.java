package com.philips.cdp.registration.configuration;

import java.util.HashMap;

/**
 * Created by 310190722 on 8/25/2015.
 */
public class HSDPConfiguration {

    private HashMap<String, HSDPClientInfo> hsdpClientInfos;

    public void setHsdpClientInfos(HashMap<String, HSDPClientInfo> hsdpClientInfos) {
        this.hsdpClientInfos = hsdpClientInfos;
    }

    public HSDPClientInfo getHSDPClientInfo(String environmentType) {
        if (hsdpClientInfos == null) {
            return null;
        }
        return hsdpClientInfos.get(environmentType);
    }
}
