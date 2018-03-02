/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.mya.launcher;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.uappframework.uappinput.UappDependencies;

/**
 * This class is used to provide dependencies for myaccount.
 * @since 2017.5.0
 */
public class MyaDependencies extends UappDependencies {

    /**
     * Constructor of MyaDependencies
     * @since 2017.5.0
     * @param appInfra Appinfra instance
     */
    public MyaDependencies(AppInfraInterface appInfra) {
        super(appInfra);
    }

}
