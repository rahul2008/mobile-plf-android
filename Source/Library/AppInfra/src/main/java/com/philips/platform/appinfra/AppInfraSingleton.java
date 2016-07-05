/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.appinfra;

/*
 * Created by 310209604 on 2016-07-01.
 */

import java.util.ConcurrentModificationException;

/**
 * Class to provide global access to one single instance of AppInfra.
 * @deprecated With the introduction of a dependency injection framework, this class will be deprecated.
 */
public final class AppInfraSingleton {
    private static AppInfraInterface mAppInfra = null;

    /**
     * Set the AppInfraInterface instance that is made available through this singleton class. Only to be used by application once.
     * @deprecated With the introduction of a dependency injection framework, this class will be deprecated.
     * @param appInfra AppInfraInterface to be made globally available
     */
    public static void setInstance(AppInfraInterface appInfra) {
        if (appInfra == null)
            throw new NullPointerException();
        if (mAppInfra == null && null!=appInfra){ // first run initialization
            mAppInfra = appInfra;
        }else { // once initialized appInfra cannot be modified
            throw new ConcurrentModificationException();
        }

    }

    /**
     * Get the AppInfraInterface instance that is made available through this singleton class.
     * @deprecated With the introduction of a dependency injection framework, this class will be deprecated.
     */
    public static AppInfraInterface getInstance() {
        return mAppInfra;
    }
}
