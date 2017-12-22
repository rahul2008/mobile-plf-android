package com.philips.platform.uappframework.uappinput;

import com.philips.platform.appinfra.AppInfraInterface;

import java.io.Serializable;


/**
This class needs to be extended for defining the input Type prerequisites for launching micro app
 */
public class UappDependencies implements Serializable {

    private static final long serialVersionUID = -4372956863316605918L;
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
