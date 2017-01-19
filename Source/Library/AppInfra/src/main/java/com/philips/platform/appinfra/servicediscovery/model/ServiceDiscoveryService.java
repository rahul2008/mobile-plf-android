package com.philips.platform.appinfra.servicediscovery.model;

/**
 * Created by 310243577 on 1/2/2017.
 */

public class ServiceDiscoveryService {

    String locale;
    String configUrl;

    public void init(String localeParam, String configUrlParam) {
        locale = localeParam;
        configUrl = configUrlParam;
    }

    public String getLocale() {
        return locale;
    }

    public String getConfigUrls() {
        return configUrl;
    }

}
