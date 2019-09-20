package com.philips.cdp.di.ecs;

import android.support.annotation.NonNull;

import com.philips.platform.appinfra.AppInfra;

public class MockInputValidator extends ECSCallValidator {



    MockECSManager mockECSManager;


    public MockInputValidator(){
        mockECSManager=new MockECSManager();
    }
    @Override
    ECSManager getECSManager() {
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