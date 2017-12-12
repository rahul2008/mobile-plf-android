/**
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.demoapplication.microapp;

import android.support.annotation.NonNull;

import com.philips.cdp2.commlib.core.CommCentral;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.uappframework.uappinput.UappDependencies;

/**
 * EWSDependencies class is for initialisation of EWSInterface.
 * It is keeping productKeyMap and ContentConfiguration.
 */
@SuppressWarnings("WeakerAccess")
public abstract class DemoUappDependencies extends UappDependencies {
    /**
     * This will create EWSDependency Object
     * @param appInfra  AppInfraInterface
     */
    public DemoUappDependencies(@NonNull final AppInfraInterface appInfra) {
        super(appInfra);

    }

    public abstract CommCentral getCommCentral();
}
