package com.philips.cdp.di.iap.utils;

/**
 * Created by 310164421 on 3/14/2016.
 */
public class IAPConfiguration {
    private static IAPConfiguration iapConfiguration;

    public static IAPConfiguration getInstance() {
        if (iapConfiguration == null) {
            iapConfiguration = new IAPConfiguration();
        }
        return iapConfiguration;
    }

}
