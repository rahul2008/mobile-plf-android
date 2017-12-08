package com.philips.cdp.registration.configuration;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import static com.philips.cdp.registration.configuration.URConfigurationConstants.HSDP_CONFIGURATION_APPLICATION_NAME;
import static com.philips.cdp.registration.configuration.URConfigurationConstants.HSDP_CONFIGURATION_SHARED;
import static com.philips.cdp.registration.configuration.URConfigurationConstants.HSDP_CONFIGURATION_SECRET;
import static com.philips.cdp.registration.configuration.URConfigurationConstants.HSDP_CONFIGURATION_BASE_URL;


public class HSDPConfiguration extends BaseConfiguration {

    private static final String URL_ENCODING_FORMAT = "UTF-8";

    public String getHsdpAppName() {
        Object appNameObject = appInfraWrapper.getURProperty(HSDP_CONFIGURATION_APPLICATION_NAME);
        return getConfigPropertyValue(appNameObject);
    }

    public String getHsdpSharedId() {
        Object sharedIdObject = appInfraWrapper.getURProperty(HSDP_CONFIGURATION_SHARED);
        return getConfigPropertyValue(sharedIdObject);
    }

    public String getHsdpSecretId() {
        Object secretIdObject = appInfraWrapper.getURProperty(HSDP_CONFIGURATION_SECRET);
        return getConfigPropertyValue(secretIdObject);
    }

    public String getHsdpBaseUrl() {
        String baseUrlAppConfig = getBaseUrlFromHsdpConfig();
        if (baseUrlAppConfig == null || baseUrlAppConfig.isEmpty()) {
            return baseUrlServiceDiscovery;
        }
        return baseUrlAppConfig;
    }

    private String getBaseUrlFromHsdpConfig() {
        Object baseUrlObject = appInfraWrapper.getURProperty(HSDP_CONFIGURATION_BASE_URL);
        String baseUrl = getDecodedBaseUrl(baseUrlObject);
        return baseUrl;
    }

    private String baseUrlServiceDiscovery;

    public void setBaseUrlServiceDiscovery(String url) {
        baseUrlServiceDiscovery = url;
    }

    private String getDecodedBaseUrl(Object baseUrlObject) {
        String baseUrl = getConfigPropertyValue(baseUrlObject);
        if (baseUrl == null) return null;
        String decodedBaseUrl = null;
        try {
            decodedBaseUrl = URLDecoder.decode(baseUrl, URL_ENCODING_FORMAT);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return decodedBaseUrl;
    }
}
