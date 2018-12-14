package com.philips.cdp.registration.configuration;

import com.philips.cdp.registration.ui.utils.RLog;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import static com.philips.cdp.registration.configuration.URConfigurationConstants.HSDP_CONFIGURATION_APPLICATION_NAME;
import static com.philips.cdp.registration.configuration.URConfigurationConstants.HSDP_CONFIGURATION_BASE_URL;
import static com.philips.cdp.registration.configuration.URConfigurationConstants.HSDP_CONFIGURATION_SECRET;
import static com.philips.cdp.registration.configuration.URConfigurationConstants.HSDP_CONFIGURATION_SHARED;


public class HSDPConfiguration extends BaseConfiguration {

    private static String TAG = "HSDPConfiguration";

    private static final String URL_ENCODING_FORMAT = "UTF-8";

    private String baseUrlServiceDiscovery;

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
        RLog.d(TAG, "getHsdpBaseUrl : " + baseUrlAppConfig);
        return baseUrlAppConfig;
    }

    private String getBaseUrlFromHsdpConfig() {
        Object baseUrlObject = appInfraWrapper.getURProperty(HSDP_CONFIGURATION_BASE_URL);
        String baseUrl = getDecodedBaseUrl(baseUrlObject);
        RLog.d(TAG, "getBaseUrlFromHsdpConfig : " + baseUrl);
        return baseUrl;
    }


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
            RLog.e(TAG, "getDecodedBaseUrl : UnsupportedEncodingException " + e.getMessage());
        }
        RLog.d(TAG, "getDecodedBaseUrl : " + decodedBaseUrl);
        return decodedBaseUrl;
    }
}
