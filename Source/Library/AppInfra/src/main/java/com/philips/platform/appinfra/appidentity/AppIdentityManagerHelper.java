/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.appidentity;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraLogEventID;
import com.philips.platform.appinfra.R;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;


/**
 * The type App identity manager helper.
 */
class AppIdentityManagerHelper {

    private final List<String> mSectorValuesList = Arrays.asList("b2b", "b2c", "b2b_Li", "b2b_HC");
    private final List<String> mServiceDiscoveryEnvList = Arrays.asList("STAGING", "PRODUCTION");
    private final List<String> mAppStateValuesList = Arrays.asList("DEVELOPMENT", "TEST", "STAGING", "ACCEPTANCE", "PRODUCTION");
    private AppInfra mAppInfra;
    private Context mContext;
    private AppConfigurationInterface.AppConfigurationError configError;


    AppIdentityManagerHelper(AppInfra aAppInfra) {
        mAppInfra = aAppInfra;
        AppConfigurationInterface.AppConfigurationError configError =
                new AppConfigurationInterface
                        .AppConfigurationError();
        this.mContext = aAppInfra.getAppInfraContext();
        this.configError = configError;
    }

    boolean isValidAppVersion(String appVersion) {
        return appVersion.matches("[0-9]+\\.[0-9]+\\.[0-9]+([_(-].*)?");
    }

    String getAppVersion() {
        try {
            PackageInfo pInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
            String appVersion = String.valueOf(pInfo.versionName);
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.DEBUG, AppInfraLogEventID.AI_APP_IDENTITY,
                    "validate AppVersion" + appVersion);
            if (appVersion != null && !appVersion.isEmpty()) {
                boolean isValid = isValidAppVersion(appVersion);
                if (!isValid)
                    throw new IllegalArgumentException("AppVersion should in this format " +
                            "\" [0-9]+\\.[0-9]+\\.[0-9]+([_(-].*)?]\" ");
            } else {
                throw new IllegalArgumentException("Appversion cannot be null");
            }
            return appVersion;
        } catch (PackageManager.NameNotFoundException e) {
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, AppInfraLogEventID.AI_APP_IDENTITY, "Error in validate AppVersion " + e.getMessage());
        }
        return null;
    }

    private String validateAppState() {
        String appState = null;
        final String defAppState = (String) mAppInfra.getConfigInterface().getDefaultPropertyForKey
                ("appidentity.appState", "appinfra", configError);
        if (defAppState != null) {
            if (defAppState.equalsIgnoreCase("production")) // allow manual override only if static appstate != production
                appState = defAppState;
            else {
                final Object dynAppState = mAppInfra.getConfigInterface().getPropertyForKey("appidentity.appState", "appinfra", configError);
                if (dynAppState != null)
                    appState = dynAppState.toString();
                else
                    appState = defAppState;
            }
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.DEBUG, AppInfraLogEventID.AI_APP_IDENTITY,
                    "validate AppState " + appState);
        }

        final Set<String> set = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        set.addAll(mAppStateValuesList);
        if (appState != null && !appState.isEmpty()) {
            if (!set.contains(appState)) {
                appState = defAppState;
            }
        } else {
            throw new IllegalArgumentException("AppState cannot be empty in AppConfig.json file");
        }
        return appState;
    }

    void serviceDiscoveryEnvValidate(String serviceDiscoveryEnvironment) {
        final Set<String> set = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        set.addAll(mServiceDiscoveryEnvList);
        if (serviceDiscoveryEnvironment != null && !serviceDiscoveryEnvironment.isEmpty()) {
            if (!set.contains(serviceDiscoveryEnvironment)) {
                mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.DEBUG, AppInfraLogEventID.AI_APP_IDENTITY
                        , "validate Service Discovery Environment " + serviceDiscoveryEnvironment);
                throw new IllegalArgumentException("\"ServiceDiscovery Environment in AppConfig.json " +
                        " file must match \" +\n" +
                        "\"one of the following values \\n STAGING, \\n PRODUCTION\"");
            }
        } else {
            throw new IllegalArgumentException("ServiceDiscovery Environment cannot be empty" +
                    " in AppConfig.json file");
        }
    }

    String validateSector() {
        String sector = (String) mAppInfra.getConfigInterface().getDefaultPropertyForKey
                ("appidentity.sector", "appinfra", configError);
        final Set<String> set = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        set.addAll(mSectorValuesList);
        if (sector != null && !sector.isEmpty()) {
            if (!set.contains(sector)) {
                // sector = null;
                throw new IllegalArgumentException("\"Sector in AppConfig.json  file" +
                        " must match one of the following values\" +\n" +
                        " \" \\\\n b2b,\\\\n b2c,\\\\n b2b_Li, \\\\n b2b_HC\"");
            }
        } else {
            throw new IllegalArgumentException("\"App Sector cannot be empty in" +
                    " AppConfig.json file\"");
        }
        mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.DEBUG, AppInfraLogEventID.AI_APP_IDENTITY
                , "validate Sector");
        return sector;
    }

    void micrositeIdValidation(String micrositeId) {
        if (micrositeId != null && !micrositeId.isEmpty()) {
            /*mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.INFO, AppInfraLogEventID.AI_APP_IDENTITY
                    , "validate MicrositeId");*/
            if (!micrositeId.matches("[a-zA-Z0-9]+")) {
                throw new IllegalArgumentException("micrositeId must not contain special " +
                        "charectors in AppConfig.json json file");
            }
        } else {
            throw new IllegalArgumentException("micrositeId cannot be empty in AppConfig.json" +
                    "  file");
        }
    }


    String getApplicationName() {
        final String appName = mContext.getApplicationInfo().loadLabel(mContext.getPackageManager()).toString();
        mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.DEBUG, AppInfraLogEventID.AI_APP_IDENTITY
                , "get AppName" + appName);
        return appName;
    }


    AppIdentityInterface.AppState getApplicationState() {
        final String appState = validateAppState();
        AppIdentityInterface.AppState mAppStateEnum = null;
        if (appState != null) {
            if (appState.equalsIgnoreCase("DEVELOPMENT")) {
                mAppStateEnum = AppIdentityInterface.AppState.DEVELOPMENT;
            } else if (appState.equalsIgnoreCase("TEST")) {
                mAppStateEnum = AppIdentityInterface.AppState.TEST;
            } else if (appState.equalsIgnoreCase("STAGING")) {
                mAppStateEnum = AppIdentityInterface.AppState.STAGING;
            } else if (appState.equalsIgnoreCase("ACCEPTANCE")) {
                mAppStateEnum = AppIdentityInterface.AppState.ACCEPTANCE;
            } else if (appState.equalsIgnoreCase("PRODUCTION")) {
                mAppStateEnum = AppIdentityInterface.AppState.PRODUCTION;
            } else {
                throw new IllegalArgumentException("\"App State in AppConfig.json  file must" +
                        " match one of the following values \\\\n TEST,\\\\n DEVELOPMENT,\\\\n " +
                        "STAGING, \\\\n ACCEPTANCE, \\\\n PRODUCTION\"");
            }
        }
        mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.DEBUG, AppInfraLogEventID.AI_APP_IDENTITY,
                "App State Environment " + mAppStateEnum);
        return mAppStateEnum;
    }


    String getServiceDiscoveryEnvironments() {
        String serviceDiscoveryEnvironment = null;
        final String defSevicediscoveryEnv = (String) mAppInfra.getConfigInterface().getDefaultPropertyForKey
                ("appidentity.serviceDiscoveryEnvironment", "appinfra", configError);
        final Object dynServiceDiscoveryEnvironment = mAppInfra.getConfigInterface()
                .getPropertyForKey("appidentity.serviceDiscoveryEnvironment", "appinfra",
                        configError);
        if (defSevicediscoveryEnv != null) {
            if (defSevicediscoveryEnv.equalsIgnoreCase("production")) // allow manual override only if static appstate != production
                serviceDiscoveryEnvironment = defSevicediscoveryEnv;
            else {
                if (dynServiceDiscoveryEnvironment != null)
                    serviceDiscoveryEnvironment = dynServiceDiscoveryEnvironment.toString();
                else
                    serviceDiscoveryEnvironment = defSevicediscoveryEnv;
            }
        }

        serviceDiscoveryEnvValidate(serviceDiscoveryEnvironment);
        if (serviceDiscoveryEnvironment != null) {
            if (serviceDiscoveryEnvironment.equalsIgnoreCase("STAGING")) {
                serviceDiscoveryEnvironment = "STAGING";
            } else if (serviceDiscoveryEnvironment.equalsIgnoreCase("PRODUCTION")) {
                serviceDiscoveryEnvironment = "PRODUCTION";
            } else {
                throw new IllegalArgumentException("\"ServiceDiscovery environment in AppConfig.json " +
                        " file must match \" +\n" +
                        "\"one of the following values \\n STAGING, \\n PRODUCTION\"");
            }
        }
        mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.DEBUG, AppInfraLogEventID.AI_APP_IDENTITY,
                "service Discovery Environment " + serviceDiscoveryEnvironment);
        return serviceDiscoveryEnvironment;
    }


    String getLocalizedApplicationName() {
        final String mLocalizedAppName = mContext.getResources().getString(R.string.localized_commercial_app_name);
        mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.DEBUG, AppInfraLogEventID.AI_APP_IDENTITY,
                "Localized AppName " + mLocalizedAppName);
        return mLocalizedAppName;
    }


    String retrieveMicrositeId() {
        final String micrositeId = (String) mAppInfra.getConfigInterface().getDefaultPropertyForKey
                ("appidentity.micrositeId", "appinfra", configError);
        mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.DEBUG, AppInfraLogEventID.AI_APP_IDENTITY,
                "microsite Id " + micrositeId);
        micrositeIdValidation(micrositeId);
        return micrositeId;
    }


}
