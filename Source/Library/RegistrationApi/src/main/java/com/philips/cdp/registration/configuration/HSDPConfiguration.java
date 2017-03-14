package com.philips.cdp.registration.configuration;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

import static com.philips.cdp.registration.configuration.URConfigurationConstants.HSDP_CONFIGURATION_APPLICATION_NAME;
import static com.philips.cdp.registration.configuration.URConfigurationConstants.HSDP_CONFIGURATION_BASE_URL;
import static com.philips.cdp.registration.configuration.URConfigurationConstants.HSDP_CONFIGURATION_SECRET;
import static com.philips.cdp.registration.configuration.URConfigurationConstants.HSDP_CONFIGURATION_SHARED;
import static com.philips.cdp.registration.configuration.URConfigurationConstants.UR;

public class HSDPConfiguration {

    private static final String DEFAULT_PROPERTY_KEY = "default";

    public static String getHsdpAppName() {
        return getAppNameFromHsdpConfig();
    }

    public static String getHsdpSharedId() {
        return getSharedIdFromHsdpConfig();
    }

    public static String getHsdpSecretId() {
        return getSecredIdHsdpConfig();
    }

    public static String getHsdpBaseUrl() {
        String baseUrlAppConfig = getBaseUrlFromHsdpConfig();
        if(TextUtils.isEmpty(baseUrlAppConfig)) {
            return baseUrlServiceDiscovery;
        }
        return baseUrlAppConfig;
    }

    private static String getSecredIdHsdpConfig() {
        Object secretIdObject = getConfigPropertyForKey(HSDP_CONFIGURATION_SECRET);
        String secretId = getProperty(secretIdObject);
        if(secretId != null) {
            return secretId;
        }
        return null;
    }

    private static String getBaseUrlFromHsdpConfig() {
        Object baseUrlObject = getConfigPropertyForKey(HSDP_CONFIGURATION_BASE_URL);
        String baseUrl = getDecodedBaseUrl(baseUrlObject);
        return baseUrl;
    }

    private static String baseUrlServiceDiscovery;

    public static void setBaseUrlServiceDiscovery(String url) {
        baseUrlServiceDiscovery = url;
    }

    private static String getDecodedBaseUrl(Object baseUrlObject) {
        String baseUrl = getProperty(baseUrlObject);
        if(baseUrl == null) return null;
        String decodedBaseUrl = null;
        try {
            decodedBaseUrl = URLDecoder.decode(baseUrl, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return decodedBaseUrl;
    }

    private static String getAppNameFromHsdpConfig() {
        Object appNameObject = getConfigPropertyForKey(HSDP_CONFIGURATION_APPLICATION_NAME);
        String appName = getProperty(appNameObject);
        if (appName != null) {
            return appName;
        }
        return null;
    }

    private static String getSharedIdFromHsdpConfig() {
        Object sharedIdObject = getConfigPropertyForKey(HSDP_CONFIGURATION_SHARED);
        String sharedId = getProperty(sharedIdObject);
        if (sharedId != null) {
            return sharedId;
        }
        return null;
    }

    @Nullable
    private static String getProperty(Object property) {
        if (property instanceof String) {
            return (String) property;
        }
        if (property instanceof Map) {
            return getPropertyValueFromMap((Map) property);
        }
        return null;
    }

    private static String getPropertyValueFromMap(Map property) {
        String propertyValue = (String) property.get(RegistrationHelper.getInstance().getCountryCode());
        if (TextUtils.isEmpty(propertyValue)) {
            propertyValue = (String) property.get(DEFAULT_PROPERTY_KEY);
        }
        return propertyValue;
    }

    private static Object getConfigPropertyForKey(String key) {
        return RegistrationHelper.
                getInstance().getAppInfraInstance().
                getConfigInterface().
                getPropertyForKey(key, UR, getAppConfigurationError());
    }

    @NonNull
    private static AppConfigurationInterface.AppConfigurationError getAppConfigurationError() {
        return new AppConfigurationInterface.AppConfigurationError();
    }
}
