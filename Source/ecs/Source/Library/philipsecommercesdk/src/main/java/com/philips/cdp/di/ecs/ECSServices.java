package com.philips.cdp.di.ecs;

import com.philips.cdp.di.ecs.integration.ECSInput;
import com.philips.cdp.di.ecs.integration.ECSListener;
import com.philips.cdp.di.ecs.util.ECSUtil;
import com.philips.platform.appinfra.AppInfra;

public class ECSServices {

    public void initialize(ECSInput ecsInput, AppInfra appInfra, ECSListener ecsListener){
        ECSUtil.INSTANCE.setAppInfra(appInfra);
        ECSUtil.INSTANCE.setEcsInput(ecsInput);
        ECSUtil.INSTANCE.setEcsListener(ecsListener);
    }

    public void hybrisOathAuthentication(){

    }

    public void getIAPConfig(){

    }

    public void getProductList(){

    }
}
