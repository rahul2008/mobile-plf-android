/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.core.configuration;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

import com.philips.cdp2.commlib_api.BuildConfig;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;

import java.util.Map;

import static com.philips.platform.appinfra.appidentity.AppIdentityInterface.AppState.PRODUCTION;

@SuppressWarnings("unchecked")
public class RuntimeConfiguration {

    private static final String CONFIG_PROPERTY_APPINFRA = "appinfra";
    private static final String CONFIG_KEY_LOG_DEBUG = "logging.debugConfig";
    private static final String CONFIG_KEY_LOG_RELEASE = "logging.releaseConfig";

    @VisibleForTesting
    static final String CONFIG_KEY_CONSOLE_LOG_ENABLED = "consoleLogEnabled";

    private AppInfraInterface appInfraInterface;

    private Context context;

    public RuntimeConfiguration(final @NonNull Context context, final @Nullable AppInfraInterface appInfra) {
        this.context = context;
        this.appInfraInterface = appInfra;
    }

    public boolean isLogEnabled() {
        if (appInfraInterface == null) {
            return true;
        }

        boolean isLogEnabled = true;
        boolean isAppInfraStateProduction = appInfraInterface.getAppIdentity().getAppState() == PRODUCTION;
        boolean isAppInfraUsingConsoleLog = true;

        final String logConfigKey = BuildConfig.DEBUG ? CONFIG_KEY_LOG_DEBUG : CONFIG_KEY_LOG_RELEASE;
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

    public Context getContext() {
        return context;
    }
}
