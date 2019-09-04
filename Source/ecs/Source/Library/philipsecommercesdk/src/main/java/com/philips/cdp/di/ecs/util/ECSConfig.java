package com.philips.cdp.di.ecs.util;

import com.philips.cdp.di.ecs.model.config.HybrisConfigResponse;
import com.philips.cdp.di.ecs.model.oauth.OAuthResponse;
import com.philips.platform.appinfra.AppInfra;

public enum ECSConfig {

    INSTANCE;


    String  propositionID;
    AppInfra appInfra;
    HybrisConfigResponse config;
    private String country;

    private OAuthResponse oAuthResponse;

    public String getAccessToken() {
        return accessToken;
    }

    String accessToken;

    public String getBaseURL() {
        return baseURL;
    }

    public void setBaseURL(String baseURL) {
        this.baseURL = baseURL;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    String baseURL;

    public String getLocale() {
        return locale;
    }

    String locale;

    public boolean isAppConfigured() {
        return rootCategory!=null && siteId!=null;
    }


    public String getRootCategory() {
        return rootCategory;
    }

    public void setRootCategory(String rootCategory) {
        this.rootCategory = rootCategory;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    String rootCategory;
    String siteId;



    public AppInfra getAppInfra() {
        return appInfra;
    }

    public void setAppInfra(AppInfra appInfra) {
        this.appInfra = appInfra;
    }


    public  boolean isHybrisFlow(){
        return this.baseURL!=null;
    }

    public String getPropositionID() {
        return propositionID;
    }

    public void setPropositionID(String propositionID) {
        this.propositionID = propositionID;
    }

    public void setAuthToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public OAuthResponse getAuthResponse() {
        return oAuthResponse;
    }

    public void setAuthResponse(OAuthResponse oAuthResponse) {
        this.oAuthResponse = oAuthResponse;
    }
}
