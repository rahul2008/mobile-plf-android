package com.philips.cdp.registration.ui.utils;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.uappframework.uappinput.UappDependencies;


public class URDependancies extends UappDependencies {

    public URDependancies(AppInfraInterface appInfra) {
        super(appInfra);
    }







    public AppInfraInterface getAppInfraInterface() {
        return getAppInfra();
    }





}
