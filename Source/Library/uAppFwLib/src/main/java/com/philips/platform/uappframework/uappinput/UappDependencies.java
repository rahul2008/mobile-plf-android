package com.philips.platform.uappframework.uappinput;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInterface;


/*
This class needs to be extended for defining the input Type prerequisites for launching micro app
 */
public class UappDependencies  {


    protected AppInfraInterface mAppInfraInterface;

    public UappDependencies(AppInfraInterface appInfra)
    {
        this.mAppInfraInterface=appInfra;

    }
    public AppInfraInterface getAppInfra() {
        return mAppInfraInterface;
    }



}
