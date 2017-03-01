package com.philips.cdp.registration.configuration;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;

import java.util.Map;

import static com.philips.cdp.registration.configuration.URConfigurationConstants.HSDP_CONFIGURATION_APPLICATION_NAME;
import static com.philips.cdp.registration.configuration.URConfigurationConstants.HSDP_CONFIGURATION_BASE_URL;
import static com.philips.cdp.registration.configuration.URConfigurationConstants.HSDP_CONFIGURATION_SECRET;
import static com.philips.cdp.registration.configuration.URConfigurationConstants.HSDP_CONFIGURATION_SHARED;
import static com.philips.cdp.registration.configuration.URConfigurationConstants.UR;

public class HSDPConfiguration {

    private static final String appName = getAppNameFromHsdpConfig();

    public static String getHsdpAppName() {
        return appName;
    }

    private static final String sharedId = getSharedIdFromHsdpConfig();

    public static String getHsdpSharedId() {
        return sharedId;
    }

    private static final String secretId = getSecredIdHsdpConfig();

    public static String getHsdpSecretId() {
        return secretId;
    }

    private static String getSecredIdHsdpConfig() {
        Object secretIdObject = getConfigPropertyForKey(HSDP_CONFIGURATION_SECRET);
        String secretId = getProperty(secretIdObject);
        if(secretId != null) {
            return secretId;
        }
        return null;
    }


    private static String baseUrlAppConfig = getBaseUrlFromHsdpConfig();

    public static String getHsdpBaseUrl() {
        if(TextUtils.isEmpty(baseUrlAppConfig)) {
            return baseUrlServiceDiscovery;
        }
        return baseUrlAppConfig;
    }

    private static String getBaseUrlFromHsdpConfig() {
        String baseUrl = (String) getConfigPropertyForKey(HSDP_CONFIGURATION_BASE_URL);
        return baseUrl;
    }

    private static String baseUrlServiceDiscovery;

    public static void setBaseUrlServiceDiscovery(String url) {
        baseUrlServiceDiscovery = url;
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
            String appNameFromMap = (String) ((Map) property).get(RegistrationHelper.getInstance().getCountryCode());
            if (TextUtils.isEmpty(appNameFromMap)) {
                appNameFromMap = (String) ((Map) property).get("default");
            }
            return appNameFromMap;
        }
        return null;
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
