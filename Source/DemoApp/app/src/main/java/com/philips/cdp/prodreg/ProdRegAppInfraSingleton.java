package com.philips.cdp.prodreg;

import com.philips.platform.appinfra.AppInfraInterface;

import java.util.ConcurrentModificationException;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ProdRegAppInfraSingleton {
    private static AppInfraInterface mAppInfra = null;

    public ProdRegAppInfraSingleton() {
    }

    public static AppInfraInterface getInstance() {
        return mAppInfra;
    }

    public static void setInstance(AppInfraInterface appInfra) {
        if (appInfra == null) {
            throw new NullPointerException();
        } else if (mAppInfra == null && null != appInfra) {
            mAppInfra = appInfra;
        } else {
            throw new ConcurrentModificationException();
        }
    }
}
