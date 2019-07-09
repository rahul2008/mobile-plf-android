package com.philips.cdp.di.ecs.util;

import com.philips.cdp.di.ecs.integration.ECSInput;
import com.philips.cdp.di.ecs.integration.ECSListener;
import com.philips.platform.appinfra.AppInfra;

public enum ECSUtil {

    INSTANCE;

    ECSInput ecsInput;
    AppInfra appInfra;
    ECSListener ecsListener;

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
