package com.philips.platform.appinfra.servicediscovery.model;

/**
 * Created by 310243577 on 1/2/2017.
 */

public class ServiceDiscoveryService {

    private String mLocale;
    private String mConfigUrl;
    private String mError;

    public void init(String localeParam, String configUrlParam) {
        mLocale = localeParam;
        mConfigUrl = configUrlParam;
    }

    public String getmError() {
        return mError;
    }

    public void setmError(String mError) {
        this.mError = mError;
    }

    public String getLocale() {
        return mLocale;
    }

    public String getConfigUrls() {
        return mConfigUrl;
    }

    public void setConfigUrl(String mConfigUrl) {
        this.mConfigUrl = mConfigUrl;
    }

}
