package com.philips.cdp.di.ecs;

import android.support.annotation.NonNull;

import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.platform.appinfra.AppInfra;

public class MockECSServices extends  ECSServices{

    public MockECSServices(String propositionID, @NonNull AppInfra appInfra) {
        super(propositionID, appInfra);
    }
}
