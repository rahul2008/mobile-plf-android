package com.philips.cdp.di.ecs;

import android.support.annotation.NonNull;

import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.integration.ECSInput;
import com.philips.platform.appinfra.AppInfra;

public class MockECSServices extends  ECSServices{


    MockECSServices(ECSInput ecsInput, @NonNull AppInfra appInfra, ECSCallback<ECSServices, Exception> iapsdkCallback) {
        super(ecsInput, appInfra, iapsdkCallback);
    }

    @Override
    ECSManager getECSManager() {
        return new MockECSManager();
    }
}
