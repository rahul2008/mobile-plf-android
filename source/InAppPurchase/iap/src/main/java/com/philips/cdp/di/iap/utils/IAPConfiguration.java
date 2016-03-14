package com.philips.cdp.di.iap.utils;

/**
 * Created by 310164421 on 3/14/2016.
 */
public class IAPConfiguration {
    private static IAPConfiguration iapConfiguration;
    private String hostport;
    private String site;
    private int theme;

    public static IAPConfiguration getInstance() {
        if (iapConfiguration == null) {
            iapConfiguration = new IAPConfiguration();
        }
        return iapConfiguration;
    }

    public String getHostport() {
        return hostport;
    }

    public String getSite() {
        return site;
    }

    public int getTheme() {
        return theme;
    }
}
