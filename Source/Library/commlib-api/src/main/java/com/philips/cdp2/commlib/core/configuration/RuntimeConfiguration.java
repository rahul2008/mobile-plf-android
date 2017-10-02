/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.core.configuration;

import android.support.annotation.Nullable;

import com.philips.cdp2.commlib_api.BuildConfig;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;

import java.util.Map;

import static com.philips.platform.appinfra.appidentity.AppIdentityInterface.AppState.PRODUCTION;

@SuppressWarnings("unchecked")
public class RuntimeConfiguration {

    public static final String CONFIG_PROPERTY_APPINFRA = "appinfra";

    public static final String CONFIG_KEY_LOG_CONFIG_DEBUG = "logging.debugConfig";
    public static final String CONFIG_KEY_LOG_CONFIG_RELEASE = "logging.releaseConfig";
    public static final String CONFIG_KEY_CONSOLE_LOG_ENABLED = "consoleLogEnabled";

    private AppInfraInterface appInfraInterface;

    public RuntimeConfiguration(final @Nullable AppInfraInterface appInfra) {
        this.appInfraInterface = appInfra;
    }

    public boolean isLogEnabled() {
        if (appInfraInterface == null) {
            return true;
        }

        boolean isLogEnabled = true;
        boolean isAppInfraStateProduction = appInfraInterface.getAppIdentity().getAppState() == PRODUCTION;
        boolean isAppInfraUsingConsoleLog = true;

        final String logConfigKey = BuildConfig.DEBUG ? CONFIG_KEY_LOG_CONFIG_DEBUG : CONFIG_KEY_LOG_CONFIG_RELEASE;
        final AppConfigurationInterface.AppConfigurationError configurationError = new AppConfigurationInterface.AppConfigurationError();
        final Map<String, Object> appInfraLogConfig = (Map<String, Object>) appInfraInterface.getConfigInterface().getPropertyForKey(logConfigKey, CONFIG_PROPERTY_APPINFRA, configurationError);

        if (appInfraLogConfig != null && appInfraLogConfig.containsKey(CONFIG_KEY_CONSOLE_LOG_ENABLED)) {
            isAppInfraUsingConsoleLog = (boolean) appInfraLogConfig.get(CONFIG_KEY_CONSOLE_LOG_ENABLED);
        }

        if (isAppInfraStateProduction || !isAppInfraUsingConsoleLog) {
            isLogEnabled = false;
        }
        return isLogEnabled;
    }
}
