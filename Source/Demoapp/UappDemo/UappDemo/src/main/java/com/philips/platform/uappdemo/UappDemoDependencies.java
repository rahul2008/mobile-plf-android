package com.philips.platform.uappdemo;


import com.philips.platform.appframework.flowmanager.base.BaseFlowManager;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.uappframework.uappinput.UappDependencies;


public class UappDemoDependencies extends UappDependencies {

    private BaseFlowManager flowManager;

    public UappDemoDependencies(final AppInfraInterface appInfra) {
        super(appInfra);
    }

    public BaseFlowManager getFlowManager() {
        return flowManager;
    }

    public void setFlowManager(BaseFlowManager flowManager) {
        this.flowManager = flowManager;
    }
}
