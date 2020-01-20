/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.mec.integration;

import android.support.annotation.NonNull;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.pif.DataInterface.USR.UserDataInterface;
import com.philips.platform.uappframework.uappinput.UappDependencies;

/**
 * MECDependencies handles the dependency required for MEC. So right now, MEC has one dependency i.e AppInfra. So vertical needs to initialize MECDependencies and set the app infra object. This app infra object will be responsible for logging, tagging and some configuration.
 * @since 1.0.0
 */
public class MECDependencies extends UappDependencies {

    private UserDataInterface userDataInterface;

    /**
     * Create MECDependencies instance from AppInfraInterface and UserDataInterface object
     * @param appInfra  to pass the instance of AppInfraInterface
     * @param userDataInterface to pass the instance of UserDataInterface
     * @since 1903
     */
    public MECDependencies(@NonNull AppInfraInterface appInfra, @NonNull UserDataInterface userDataInterface) {
        super(appInfra);
        this.userDataInterface = userDataInterface;
    }

    /**
     * @return userDataInterface
     */
    public UserDataInterface getUserDataInterface() {
        return userDataInterface;
    }
}
