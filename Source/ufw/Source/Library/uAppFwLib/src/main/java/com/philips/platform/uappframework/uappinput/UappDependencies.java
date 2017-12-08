package com.philips.platform.uappframework.uappinput;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInterface;


/**
This class needs to be extended for defining the input Type prerequisites for launching micro app
 */
public class UappDependencies  {

/**
 Instance of AppInfraInterface
 */
    protected AppInfraInterface mAppInfraInterface;

    /**
     * Contructor for UappDependencies
     * @param appInfra : Requires appInfraInterface object
     */

    public UappDependencies(AppInfraInterface appInfra)
    {
        this.mAppInfraInterface=appInfra;

    }

    /**
     * To retrieve AppInfra Interface object
     * @return AppInfraInterface Object
     */
    public AppInfraInterface getAppInfra() {
        return mAppInfraInterface;
    }



}
