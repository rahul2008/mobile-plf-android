package com.philips.cdp.registration.configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 310190722 on 8/25/2015.
 */
public class CurrentHSDPConfiguration extends HSDPConfiguration{


    CurrentHSDPConfiguration(){
        hsdpClientInfo = new HSDPClientInfo();
        hsdpClientInfos = new HashMap<>();
    }



    public void setHsdpClientInfos(HashMap<String, HSDPClientInfo> hsdpClientInfos) {
        this.hsdpClientInfos = hsdpClientInfos;
    }



    /*protected HSDPClientInfo getHSDPClientInfo(String environmentType) {
        if (hsdpClientInfos == null) {
            return null;
        }
        return hsdpClientInfos.get(environmentType);
    }*/

    protected void checkandSetConfiguration(final HSDPClientInfo hsdpClientInfo){
        if(hsdpClientInfo.isRequiredConfigSet() && mEnv != null){
            hsdpClientInfos.put(mEnv.getValue(), hsdpClientInfo);

        }
    }

    public void setApplicationName(final String pApplicationName){
        hsdpClientInfo.setApplicationName(pApplicationName);
        checkandSetConfiguration(hsdpClientInfo);
    }

    public void setEnvironment(Configuration pConfig){
        mEnv = pConfig;
        checkandSetConfiguration(hsdpClientInfo);
    }

    public void setSharedId(final String pSharedId){
        hsdpClientInfo.setShared(pSharedId);
        checkandSetConfiguration(hsdpClientInfo);
    }

    public void setSecret(final String pSecret){
        hsdpClientInfo.setSecret(pSecret);
        checkandSetConfiguration(hsdpClientInfo);
    }

    public void setBaseURL(final String pBaseURL){
        hsdpClientInfo.setBaseURL(pBaseURL);
        checkandSetConfiguration(hsdpClientInfo);
    }



}
