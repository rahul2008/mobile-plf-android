package com.philips.platform.appinfra.servicediscovery.model;

import java.util.Map;

/**
 * Created by 310238655 on 8/8/2016.
 */
public class ServiceDiscoveyService {


    String locale;
    String configUrl;

    public void init(String localeParam, String configUrlParam){
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
