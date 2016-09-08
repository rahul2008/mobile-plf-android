/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.appidentity;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.R;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;


/**
 * The type App identity manager.
 */
public class AppIdentityManager implements AppIdentityInterface {

    private AppInfra mAppInfra;
    private Context context;

    private String mAppName;
    private String mAppVersion;
    private String mServiceDiscoveryEnvironment;
    private String mLocalizedAppName;
    private String micrositeId;
    private String sector;
    private String mAppState;

    private List<String> mSectorValues = Arrays.asList("b2b", "b2c", "b2b_Li", "b2b_HC");
    private List<String> mServiceDiscoveryEnv = Arrays.asList("TEST", "STAGING", "ACCEPTANCE", "PRODUCTION");
    private List<String> mAppStateValues = Arrays.asList("DEVELOPMENT", "TEST", "STAGING", "ACCEPTANCE", "PRODUCTION");
    private AppConfigurationInterface.AppConfigurationError configError;


    public AppIdentityManager(AppInfra aAppInfra) {
        mAppInfra = aAppInfra;
        context = mAppInfra.getAppInfraContext();
        configError = new AppConfigurationInterface
                .AppConfigurationError();
        // Class shall not presume appInfra to be completely initialized at this point.
        // At any call after the constructor, appInfra can be presumed to be complete.
    }


    private void validateAppVersion() {
        try {
            PackageInfo pInfo;
            pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            mAppVersion = String.valueOf(pInfo.versionName);
            if (mAppVersion != null && !mAppVersion.isEmpty()) {
                if (!mAppVersion.matches("[0-9]+\\.[0-9]+\\.[0-9]+([_-].*)?")) {
                    throw new IllegalArgumentException("AppVersion should in this format " +
                            "\" [0-9]+\\\\.[0-9]+\\\\.[0-9]+([_-].*)?]\" ");
                }
            } else {
                throw new IllegalArgumentException("Appversion cannot be null");
            }
        } catch (PackageManager.NameNotFoundException | IllegalArgumentException e) {
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, "AppIdentity exception",
                    Log.getStackTraceString(e));
        }
    }


    private void validateAppState() {

        String defAppState = (String) mAppInfra.getConfigInterface().getDefaultPropertyForKey
                ("appidentity.appState", "appinfra", configError);
        if (defAppState != null) {
            if (defAppState.equalsIgnoreCase("production")) // allow manual override only if static appstate != production
                mAppState = defAppState;
            else {
                Object dynAppState = mAppInfra.getConfigInterface().getPropertyForKey("appidentity.appState", "appinfra", configError);
                if (dynAppState != null)
                    mAppState = dynAppState.toString();
                else
                    mAppState = defAppState;
            }
        }


        Set<String> set;

        try {
            set = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
            if (mAppState != null && !mAppState.isEmpty()) {
                set.addAll(mAppStateValues);
                if (!set.contains(mAppState)) {
                    mAppState = defAppState;
//                    throw new IllegalArgumentException("\"App State in appIdentityConfig  file must" +
//                            " match one of the following values \\\\n TEST,\\\\n DEVELOPMENT,\\\\n " +
//                            "STAGING, \\\\n ACCEPTANCE, \\\\n PRODUCTION\"");
                }
            } else {
                throw new IllegalArgumentException("AppState cannot be empty in appIdentityConfig json file");
            }

        } catch (IllegalArgumentException error) {
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, "AppIdentity exception",
                    Log.getStackTraceString(error));
        }
    }

    private void validateServiceDiscoveryEnv() {
        String defSevicediscoveryEnv = (String) mAppInfra.getConfigInterface().getDefaultPropertyForKey
                ("appidentity.serviceDiscoveryEnvironment", "appinfra", configError);

        if(defSevicediscoveryEnv != null) {
            if (defSevicediscoveryEnv.equalsIgnoreCase("production")) // allow manual override only if static appstate != production
                mServiceDiscoveryEnvironment = defSevicediscoveryEnv;
            else {
                Object dynServiceDiscoveryEnvironment = mAppInfra.getConfigInterface()
                        .getPropertyForKey("appidentity.serviceDiscoveryEnvironment", "appinfra", configError);
                if (dynServiceDiscoveryEnvironment != null)
                    mServiceDiscoveryEnvironment = dynServiceDiscoveryEnvironment.toString();
                else
                    mServiceDiscoveryEnvironment = defSevicediscoveryEnv;
            }

        }

        Set<String> set;
        try {
            set = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
            if (mServiceDiscoveryEnvironment != null && !mServiceDiscoveryEnvironment.isEmpty()) {
                set.addAll(mServiceDiscoveryEnv);
                if (!set.contains(mServiceDiscoveryEnvironment)) {
                    mServiceDiscoveryEnvironment = defSevicediscoveryEnv;
                    throw new IllegalArgumentException("\"servicediscoveryENV in appIdentityConfig " +
                            " file must match \" +\n" +
                            "\"one of the following values \\n TEST,\\n STAGING, \\n ACCEPTANCE, \\n PRODUCTION\"");
                }
            } else {
                throw new IllegalArgumentException("ServiceDiscovery Environment cannot be empty" +
                        " in appIdentityConfig json file");
            }
        } catch (IllegalArgumentException error) {
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, "AppIdentity exception",
                    Log.getStackTraceString(error));
        }
    }

    private void validateSector() {
        sector = (String) mAppInfra.getConfigInterface().getDefaultPropertyForKey
                ("appidentity.sector", "appinfra", configError);

        Set<String> set;
        try {
            set = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
            if (sector != null && !sector.isEmpty()) {
                set.addAll(mSectorValues);
                if (!set.contains(sector)) {
                    sector = null;
                    throw new IllegalArgumentException("\"Sector in appIdentityConfig  file" +
                            " must match one of the following values\" +\n" +
                            " \" \\\\n b2b,\\\\n b2c,\\\\n b2b_Li, \\\\n b2b_HC\"");

                }
            } else {
                throw new IllegalArgumentException("\"App Sector cannot be empty in" +
                        " appIdentityConfig json file\"");
            }

        } catch (IllegalArgumentException error) {
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, "AppIdentity exception",
                    Log.getStackTraceString(error));
        }
    }

    private void validateMicrositeId() {
        micrositeId = (String) mAppInfra.getConfigInterface().getDefaultPropertyForKey
                ("appidentity.micrositeId", "appinfra", configError);
        try {
            if (micrositeId != null && !micrositeId.isEmpty()) {
                if (!micrositeId.matches("[a-zA-Z0-9_.-]+")) {
                    micrositeId = null;
                    throw new IllegalArgumentException("micrositeId must not contain special " +
                            "charectors in appIdentityConfig json file");
                }
            } else {
                throw new IllegalArgumentException("micrositeId cannot be empty in appIdentityConfig" +
                        "  file");
            }
        } catch (IllegalArgumentException error) {
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, "AppIdentity exception",
                    Log.getStackTraceString(error));
        }

    }

    @Override
    public String getAppName() {
        mAppName = context.getApplicationInfo().loadLabel(context.getPackageManager()).toString();
        return mAppName;
    }


    @Override
    public String getAppVersion() {
        validateAppVersion();
        return mAppVersion;
    }

    @Override
    public AppState getAppState() {
        validateAppState();

        AppState mAppStateEnum = null;
        if (mAppState != null) {
            if (mAppState.equalsIgnoreCase("DEVELOPMENT")) {
                mAppStateEnum = AppState.DEVELOPMENT;
            }
            if (mAppState.equalsIgnoreCase("TEST")) {
                mAppStateEnum = AppState.TEST;
            }
            if (mAppState.equalsIgnoreCase("STAGING")) {
                mAppStateEnum = AppState.STAGING;
            }
            if (mAppState.equalsIgnoreCase("ACCEPTANCE")) {
                mAppStateEnum = AppState.ACCEPTANCE;
            }
            if (mAppState.equalsIgnoreCase("PRODUCTION")) {
                mAppStateEnum = AppState.PRODUCTION;
            }
        }

//        if (mAppStateEnum != null) {
//            return mAppStateEnum;
//        }

        return mAppStateEnum;
    }

    @Override
    public String getServiceDiscoveryEnvironment() {
        validateServiceDiscoveryEnv();
        return mServiceDiscoveryEnvironment;
    }


    @Override
    public String getLocalizedAppName() {
        /* Vertical App should have this string defined for all supported language files
         *  default <string name="localized_commercial_app_name">AppInfra DemoApp localized</string>
         * */
        mLocalizedAppName = context.getResources().getString(R.string.localized_commercial_app_name);

        return mLocalizedAppName;
    }


    @Override
    public String getMicrositeId() {
        validateMicrositeId();
        return micrositeId;
    }


    @Override
    public String getSector() {
        validateSector();
        return sector;
    }

}
