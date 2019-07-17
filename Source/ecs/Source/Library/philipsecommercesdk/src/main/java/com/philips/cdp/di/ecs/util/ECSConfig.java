package com.philips.cdp.di.ecs.util;

import com.philips.cdp.di.ecs.integration.ECSInput;
import com.philips.cdp.di.ecs.model.response.HybrisConfigResponse;
import com.philips.platform.appinfra.AppInfra;

public enum ECSConfig {

    INSTANCE;

    ECSInput ecsInput;
    AppInfra appInfra;
    HybrisConfigResponse config;

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

    public ECSInput getEcsInput() {
        return ecsInput;
    }

    public void setEcsInput(ECSInput ecsInput) {
        this.ecsInput = ecsInput;
    }

    public AppInfra getAppInfra() {
        return appInfra;
    }

    public void setAppInfra(AppInfra appInfra) {
        this.appInfra = appInfra;
    }


    public  boolean isHybrisFlow(){
        return this.baseURL!=null;
    }
}
