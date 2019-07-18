package com.philips.cdp.di.ecs;

import android.support.annotation.NonNull;

import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.platform.appinfra.AppInfra;

public class MockECSServices extends  ECSServices{



    MockECSManager mockECSManager;


    public MockECSServices(String propositionID, @NonNull AppInfra appInfra) {
        super(propositionID, appInfra);
    }

    @Override
    ECSManager getECSManager() {
        mockECSManager=new MockECSManager();
        return mockECSManager;
    }

    public void setJsonFileName(String jsonFileName) {

       // this.jsonFileName = jsonFileName;
        mockECSManager.setJsonFileNameMockECSManager(jsonFileName);
    }

    public String getJsonFileName() {
        return mockECSManager.getJsonFileNameMockECSManager();
    }
}
