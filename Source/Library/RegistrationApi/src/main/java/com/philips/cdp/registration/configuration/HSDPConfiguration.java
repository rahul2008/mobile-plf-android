package com.philips.cdp.registration.configuration;

import android.text.TextUtils;

import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;

import static com.philips.cdp.registration.configuration.URConfigurationConstants.HSDP_CONFIGURATION_BASE_URL;
import static com.philips.cdp.registration.configuration.URConfigurationConstants.UR;

public class HSDPConfiguration {

    private static String baseUrlAppConfig = setBaseUrl();

    private static String setBaseUrl() {
        AppConfigurationInterface.AppConfigurationError configError = new
                AppConfigurationInterface.AppConfigurationError();
        String baseUrl = (String) RegistrationHelper.
                getInstance().getAppInfraInstance().
                getConfigInterface().
                getPropertyForKey(HSDP_CONFIGURATION_BASE_URL
                        , UR, configError);
        return baseUrl;
    }

    private static String baseUrlServiceDiscovery;

    public static void setBaseUrlServiceDiscovery(String url) {
        baseUrlServiceDiscovery = url;
    }

    public static String getBaseUrl() {
        if(TextUtils.isEmpty(baseUrlAppConfig)) {
            return baseUrlServiceDiscovery;
        }
        return baseUrlAppConfig;
    }
}
