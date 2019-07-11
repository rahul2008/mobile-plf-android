package com.philips.cdp.di.ecs.util;

import com.philips.cdp.di.ecs.integration.ECSInput;
import com.philips.cdp.di.ecs.integration.ECSListener;
import com.philips.platform.appinfra.AppInfra;

public enum ECSConfig {

    INSTANCE;

    ECSInput ecsInput;
    AppInfra appInfra;
    ECSListener ecsListener;

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

    public ECSListener getEcsListener() {
        return ecsListener;
    }

    public void setEcsListener(ECSListener ecsListener) {
        this.ecsListener = ecsListener;
    }
}
