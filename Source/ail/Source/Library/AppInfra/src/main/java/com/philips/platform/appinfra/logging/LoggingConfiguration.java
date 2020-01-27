/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.appinfra.logging;


import android.content.pm.ApplicationInfo;
import androidx.annotation.NonNull;
import android.text.TextUtils;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;

public class LoggingConfiguration {

    private static final String COMPONENT_IDS_KEY = "componentIds";
    public static final String APP_INFRA_CLOUD_LOGGING_SECRET_KEY = "appInfra.cloudLoggingSecretKey";
    public static final String APP_INFRA_CLOUD_LOGGING_SHARED_KEY = "appInfra.cloudLoggingSharedKey";
    public static final String APP_INFRA_CLOUD_LOGGING_PRODUCT_KEY = "appInfra.cloudLoggingProductKey";
    public static final String HSDP_GROUP = "hsdp";
    public final String LOG_LEVEL_KEY = "logLevel";
    public final String CONSOLE_LOG_ENABLED_KEY = "consoleLogEnabled";
    public final String CLOUD_LOG_ENABLED_KEY = "cloudLogEnabled";
    public final String CLOUD_LOG_BATCH_LIMIT = "cloudBatchLimit";
    public final String FILE_LOG_ENABLED_KEY = "fileLogEnabled";
    public final String COMPONENT_LEVEL_LOG_ENABLED_KEY = "componentLevelLogEnabled";
    private HashMap<?, ?> mLoggingProperties;
    private AppInfraInterface mAppInfra;
    private String mComponentID = "";


    public AppInfraInterface getAppInfra() {
        return mAppInfra;
    }

    public String getComponentID() {

        return mComponentID;
    }

    public String getComponentVersion() {
        return mComponentVersion;
    }

    private String mComponentVersion = "";

    LoggingConfiguration(AppInfraInterface mAppInfra, String mComponentID, String mComponentVersion) {
        this.mAppInfra = mAppInfra;
        this.mComponentID = mComponentID;
        this.mComponentVersion = mComponentVersion;
        mLoggingProperties = getLoggingProperties();
    }

    boolean isComponentLevelLogEnabled() {
        return (null != mLoggingProperties && null != mLoggingProperties.get(COMPONENT_LEVEL_LOG_ENABLED_KEY)) ? (Boolean) mLoggingProperties.get(COMPONENT_LEVEL_LOG_ENABLED_KEY) : false;
    }

    boolean isFileLogEnabled() {
        return (null != mLoggingProperties && null != mLoggingProperties.get(FILE_LOG_ENABLED_KEY)) ? (Boolean) mLoggingProperties.get(FILE_LOG_ENABLED_KEY) : false;
    }

    boolean isConsoleLogEnabled() {
        return (null != mLoggingProperties && null != mLoggingProperties.get(CONSOLE_LOG_ENABLED_KEY)) ? (Boolean) mLoggingProperties.get(CONSOLE_LOG_ENABLED_KEY) : true;
    }

    String getLogLevel() {
        return (null != mLoggingProperties && null != mLoggingProperties.get(LOG_LEVEL_KEY)) ? (String) mLoggingProperties.get(LOG_LEVEL_KEY) : "All";
    }

    /**
     * Checks whether logging is enabled or not
     *
     * @return
     */
    boolean isLoggingEnabled() {
        String logLevel = getLogLevel();
        if (!logLevel.equalsIgnoreCase("Off")) {
            if (isConsoleLogEnabled() || isFileLogEnabled() || isCloudLogEnabled()) {
                return true;
            }
        }
        return false;
    }

    boolean checkIfComponentLogCriteriaMet(String componentId) {
        if (isComponentLevelLogEnabled() && !TextUtils.isEmpty(componentId)) {
            final ArrayList<String> ComponentToBeLoggedList = getComponentsFromConfig();
            if (!ComponentToBeLoggedList.contains(mComponentID)) {
                return false;
            }
        }
        return true;
    }


    private ArrayList<String> getComponentsFromConfig() {
        ArrayList<String> componentToBeLoggedList = new ArrayList<>();
        final JSONArray jsonArray = (JSONArray) mLoggingProperties.get(COMPONENT_IDS_KEY);
        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.length(); i++) {
                componentToBeLoggedList.add(jsonArray.optString(i));
            }
        }
        return componentToBeLoggedList;
    }

    HashMap<?, ?> getLoggingProperties() {
        if (null == mLoggingProperties) {
            final String AppInfraLoggingPropertyKey;
            final boolean isDebuggable = (0 != (mAppInfra.getAppInfraContext().getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE));
            if (isDebuggable) { // debug mode
                AppInfraLoggingPropertyKey = "logging.debugConfig";
            } else {
                AppInfraLoggingPropertyKey = "logging.releaseConfig";
            }
            final AppConfigurationInterface appConfigurationInterface = mAppInfra.getConfigInterface();
            final AppConfigurationInterface.AppConfigurationError configError = getAppConfigurationError();
            mLoggingProperties = (HashMap<?, ?>) appConfigurationInterface.getPropertyForKey(AppInfraLoggingPropertyKey, "appinfra", configError);
        }
        return mLoggingProperties;
    }


    public boolean isCloudLogEnabled() {
        return (null != mLoggingProperties && null != mLoggingProperties.get(CLOUD_LOG_ENABLED_KEY)) ? (Boolean) mLoggingProperties.get(CLOUD_LOG_ENABLED_KEY) : false;
    }

    public int getBatchLimit() {
        if (null != mLoggingProperties && null != mLoggingProperties.get(CLOUD_LOG_BATCH_LIMIT)) {
            int batchLimit = (Integer) mLoggingProperties.get(CLOUD_LOG_BATCH_LIMIT);
            if (batchLimit > 1 && batchLimit <= CloudLoggingConstants.HSDP_LOG_MAX_LIMIT) {
                return batchLimit;
            } else {
                return CloudLoggingConstants.DEFAULT_BATCH_LIMIT;
            }
        } else {
            return CloudLoggingConstants.DEFAULT_BATCH_LIMIT;
        }
    }

    public String getCLSecretKey() {
        final AppConfigurationInterface appConfigurationInterface = mAppInfra.getConfigInterface();
        final AppConfigurationInterface.AppConfigurationError configError = getAppConfigurationError();
        return (String) appConfigurationInterface.getPropertyForKey(APP_INFRA_CLOUD_LOGGING_SECRET_KEY, HSDP_GROUP, configError);
    }

    @NonNull
    AppConfigurationInterface.AppConfigurationError getAppConfigurationError() {
        return new AppConfigurationInterface.AppConfigurationError();
    }

    public String getCLSharedKey() {
        final AppConfigurationInterface appConfigurationInterface = mAppInfra.getConfigInterface();
        final AppConfigurationInterface.AppConfigurationError configError = getAppConfigurationError();
        return (String) appConfigurationInterface.getPropertyForKey(APP_INFRA_CLOUD_LOGGING_SHARED_KEY, HSDP_GROUP, configError);
    }

    public String getCLProductKey() {
        final AppConfigurationInterface appConfigurationInterface = mAppInfra.getConfigInterface();
        final AppConfigurationInterface.AppConfigurationError configError = getAppConfigurationError();
        return (String) appConfigurationInterface.getPropertyForKey(APP_INFRA_CLOUD_LOGGING_PRODUCT_KEY, HSDP_GROUP, configError);
    }
}
