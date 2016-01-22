package com.philips.cdp.registration.configuration;

import java.util.HashMap;

/**
 * Created by 310202337 on 1/22/2016.
 */
public abstract class HSDPConfiguration {

    protected HashMap<String, HSDPClientInfo> hsdpClientInfos;
    protected HSDPClientInfo hsdpClientInfo;
    protected Configuration mEnv;

    protected abstract void checkandSetConfiguration(final HSDPClientInfo hsdpClientInfo);

    public    HSDPClientInfo getHSDPClientInfo(String environmentType){
        if (hsdpClientInfos == null) {
            return null;
        }
        return hsdpClientInfos.get(environmentType);
    };
}
