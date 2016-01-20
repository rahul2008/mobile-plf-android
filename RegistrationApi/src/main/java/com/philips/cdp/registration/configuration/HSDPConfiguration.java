package com.philips.cdp.registration.configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 310190722 on 8/25/2015.
 */
public class HSDPConfiguration {

    private HashMap<String, HSDPClientInfo> hsdpClientInfos;

    HSDPConfiguration(){

    }

    public HSDPConfiguration(final HashMap<String, HSDPClientInfo> pHsdpClientInfo){
        this.hsdpClientInfos = pHsdpClientInfo;

    }

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
