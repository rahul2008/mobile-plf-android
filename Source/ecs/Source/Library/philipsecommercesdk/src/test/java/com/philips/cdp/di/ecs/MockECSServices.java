package com.philips.cdp.di.ecs;

import androidx.annotation.NonNull;

import com.philips.platform.appinfra.AppInfra;

public class MockECSServices extends  ECSServices{

    public MockECSServices(String propositionID, @NonNull AppInfra appInfra) {
        super(propositionID, appInfra);
    }
}
