package com.philips.cdp.registration.configuration;

import java.util.HashMap;

/**
 * Created by 310190722 on 9/30/2015.
 */
public class HSDPClientInfo {

    private static final String SHARED = "Shared";

    private static final String SECRET = "Secret";

    private static final String BASE_URL = "BaseURL";

    private final String HSDP_APPLICATION_NAME = "ApplicationName";

    private HashMap<String, String> infos;

    public String getSecret() {
        return secret;
    }

    public String getShared() {
        return shared;
    }

    private String shared;
    private String secret;

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    void setShared(String pShared){
        this.shared = pShared;
    }

    void setSecret(String pSecret){
        this.secret = pSecret;
    }

    public String getHSDPApplicationName(){
        return  applicationName;
    }

    public String getBaseURL() {
        return baseURL;
    }

    public void setBaseURL(String baseURL) {
        this.baseURL = baseURL;
    }

    private String applicationName;
    private String baseURL;


    HSDPClientInfo(){

    }

    public HSDPClientInfo(final String pShared, final String pSecret){
        this.shared = pShared;
        this.secret = pSecret;
    }

    public String getSharedId() {
        if (infos == null) {
            return null;
        }
        return infos.get(SHARED);
    }


    public String getSecretId() {
        if (infos == null) {
            return null;
        }
        return infos.get(SECRET);
    }


    public String getBaseUrl() {
        if (infos == null) {
            return null;
        }
        return infos.get(BASE_URL);
    }

    public String getApplicationName() {
        if (infos == null) {
            return null;
        }
        return infos.get(HSDP_APPLICATION_NAME);
    }

    public void setInfos(HashMap<String, String> infos) {
        this.infos = infos;
    }
}



