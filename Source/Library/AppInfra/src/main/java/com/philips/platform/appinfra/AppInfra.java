/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.philips.platform.appinfra.abtestclient.ABTestClientInterface;
import com.philips.platform.appinfra.abtestclient.ABTestClientManager;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationManager;
import com.philips.platform.appinfra.appidentity.AppIdentityInterface;
import com.philips.platform.appinfra.appidentity.AppIdentityManager;
import com.philips.platform.appinfra.appupdate.AppUpdateConstants;
import com.philips.platform.appinfra.appupdate.AppUpdateInterface;
import com.philips.platform.appinfra.appupdate.AppUpdateManager;
import com.philips.platform.appinfra.internationalization.InternationalizationInterface;
import com.philips.platform.appinfra.internationalization.InternationalizationManager;
import com.philips.platform.appinfra.languagepack.LanguagePackInterface;
import com.philips.platform.appinfra.languagepack.LanguagePackManager;
import com.philips.platform.appinfra.logging.AppInfraLogging;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.rest.RestInterface;
import com.philips.platform.appinfra.rest.RestManager;
import com.philips.platform.appinfra.securestorage.SecureStorage;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryManager;
import com.philips.platform.appinfra.tagging.AppTagging;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.appinfra.timesync.TimeInterface;
import com.philips.platform.appinfra.timesync.TimeSyncSntpClient;

import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Field;

/**
 * The AppInfra Base class, here using builder design pattern to create object .
 */
public class AppInfra implements AppInfraInterface ,ComponentVersionInfo,Serializable {

    private SecureStorageInterface secureStorage;
    private LoggingInterface logger;
    private AppTaggingInterface tagging;
    private LoggingInterface appInfraLogger;
    private AppIdentityInterface appIdentity;
    private InternationalizationInterface local;
    private ServiceDiscoveryInterface mServiceDiscoveryInterface;
    private TimeInterface mTimeSyncInterface;
    private AppConfigurationInterface configInterface;
    private RestInterface mRestInterface;
    private ABTestClientInterface mAbtesting;
    private AppUpdateInterface mAppupdateInterface;


    /**
     * The App infra context. This MUST be Application context
     */
    private Context appInfraContext;
    private LanguagePackInterface mLanguagePackInterface;


    private AppInfra(Context pContext) {
        appInfraContext = pContext;
    }

    private static void postLog(AppInfra ai,long startTime, String message) {
        long endTime = System.currentTimeMillis();
        long methodDuration = (endTime - startTime);
        ai.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR,AppInfraLogEventID.AI_APPINFRA,
                message + methodDuration);

    }

    public static Object getAutoRefreshValue(AppConfigurationManager appConfigurationManager) {
        AppConfigurationInterface.AppConfigurationError configurationError = new AppConfigurationInterface.AppConfigurationError();
        return appConfigurationManager.getPropertyForKey("appUpdate.autoRefresh", "appinfra", configurationError);
    }

    public Context getAppInfraContext() {
        return appInfraContext;
    }

    @Override
    public TimeInterface getTime() {
        return mTimeSyncInterface;
    }

    private void setTime(TimeInterface mTimeSyncInterface) {
        this.mTimeSyncInterface = mTimeSyncInterface;
    }

    @Override
    public SecureStorageInterface getSecureStorage() {
        return secureStorage;
    }

    private void setSecureStorage(SecureStorageInterface sec) {
        secureStorage = sec;
    }

    @Override
    public AppIdentityInterface getAppIdentity() {
        return appIdentity;
    }

    private void setAppIdentity(AppIdentityInterface identity) {
        appIdentity = identity;

    }

    @Override
    public InternationalizationInterface getInternationalization() {
        return local;
    }

    @Override
    public LoggingInterface getLogging() {
        return logger;
    }

    private void setLogging(LoggingInterface log) {
        logger = log;
        appInfraLogger = logger.createInstanceForComponent(getComponentId(),
                getVersion());
    }

    @Override
    public ServiceDiscoveryInterface getServiceDiscovery() {
        return mServiceDiscoveryInterface;
    }

    @Override
    public AppConfigurationInterface getConfigInterface() {
        return configInterface;
    }

    public void setConfigInterface(AppConfigurationInterface configInterface) {
        this.configInterface = configInterface;
    }

    @Override
    public RestInterface getRestClient() {
        return mRestInterface;
    }

    @Override
    public ABTestClientInterface getAbTesting() {
        return mAbtesting;
    }

    private void setAbTesting(ABTestClientInterface abTesting) {
        mAbtesting = abTesting;
    }

    @Override
    public LanguagePackInterface getLanguagePack() {
        return mLanguagePackInterface;
    }

    @Override
    public AppUpdateInterface getAppUpdate() {
        return mAppupdateInterface;
    }

    public void setLanguagePackInterface(LanguagePackInterface languagePackInterface) {
        this.mLanguagePackInterface = languagePackInterface;
    }

    private void setRestInterface(RestInterface restInterface) {
        mRestInterface = restInterface;

    }

    private void setLocal(InternationalizationInterface locale) {
        local = locale;

    }

    private void setServiceDiscoveryInterface(ServiceDiscoveryInterface mServiceDiscoveryInterfac) {
        mServiceDiscoveryInterface = mServiceDiscoveryInterfac;
    }

    public void setAppupdateInterface(AppUpdateInterface appupdateInterface) {
        this.mAppupdateInterface = appupdateInterface;
    }


    public AppTaggingInterface getTagging() {
        return tagging;
    }

    private void setTagging(AppTaggingInterface tagg) {
        tagging = tagg;

    }

    public LoggingInterface getAppInfraLogInstance() { // this log should be used withing App Infra library
        return appInfraLogger;
    }

    @Override
    public String getComponentId() {
        final String appInfraComponentID = "ail:";
        return appInfraComponentID;
    }

    @Override
    public String getVersion() {
        return BuildConfig.VERSION_NAME;
    }

    /**
     * The type Builder.
     */
    public static class Builder {

        private SecureStorageInterface secStor;
        private LoggingInterface logger; // builder logger
     //   private LoggingInterface aiLogger; // app infra logger
        private AppTaggingInterface tagging;
        private AppIdentityInterface appIdentity;
        private InternationalizationInterface local;
        private ServiceDiscoveryInterface mServiceDiscoveryInterface;
        private TimeInterface mTimeSyncInterfaceBuilder;
        private ABTestClientInterface aIabtesting;


        private AppConfigurationInterface configInterface;
        private RestInterface mRestInterface;
        private LanguagePackInterface languagePack;
        private AppUpdateInterface appupdateInterface;


        /**
         * Instantiates a new Builder.
         * It can be configured with Builder setter methods
         */
        public Builder() {
            secStor = null;
            logger = null;
            //aiLogger = null;
            tagging = null;
            appIdentity = null;
            local = null;
            mServiceDiscoveryInterface = null;
            mTimeSyncInterfaceBuilder = null;
            configInterface = null;
            mRestInterface = null;
            languagePack = null;
            appupdateInterface = null;
        }


        /**
         * Sets config.
         *
         * @param config the config
         * @return the config
         */
        public Builder setConfig(AppConfigurationInterface config) {
            configInterface = config;
            return this;
        }

        /**
         * Sets REST.
         *
         * @param rest the config
         * @return the config
         */
        public Builder setRestInterface(RestInterface rest) {
            mRestInterface = rest;
            return this;
        }

        /**
         * Sets Builder logging overriding the default implementation.
         *
         * @param log the log
         * @return the logging
         */
        public Builder setLogging(LoggingInterface log) {
            logger = log;
            return this;
        }

        /**
         * Sets Builder secure storage overriding the default implementation.
         *
         * @param secureStorage the secure storage
         * @return the secure storage
         */
        public Builder setSecureStorage(SecureStorageInterface secureStorage) {
            secStor = secureStorage;
            return this;
        }


        /**
         * Sets Builder service discovery overriding the default implementation.
         *
         * @param serviceDiscoveryInterface the service discovery interface
         * @return the service discovery
         */
        public Builder setServiceDiscovery(ServiceDiscoveryInterface serviceDiscoveryInterface) {
            mServiceDiscoveryInterface = serviceDiscoveryInterface;
            return this;
        }

        /**
         * Sets Builder tagging overriding the default implementation.
         *
         * @param aIAppTaggingInterface the a i app tagging interface
         * @return the tagging
         */
        public Builder setTagging(AppTaggingInterface aIAppTaggingInterface) {
            tagging = aIAppTaggingInterface;
            return this;
        }

        public Builder setAbTesting(ABTestClientInterface abtesting) {
            aIabtesting = abtesting;
            return this;
        }

        /**
         * Sets Builder time sync overriding the default implementation.
         *
         * @param timeSyncSntpClient the time sync sntp client
         * @return the time sync
         */
        public Builder setTimeSync(TimeInterface timeSyncSntpClient) {
            mTimeSyncInterfaceBuilder = timeSyncSntpClient;
            return this;
        }


        /**
         * Actual AppInfra object is created here.
         * Once build is called AppInfra is created in memory and cannot be modified during runtime.
         *
         * @param pContext Application Context
         * @return the app infra
         */


        public AppInfra build(Context pContext) {
            Log.v(AppInfraLogEventID.AI_APPINFRA, "AI Intitialization Starts");
            long startTime = System.currentTimeMillis();
            final AppInfra ai = new AppInfra(pContext);
            final AppConfigurationManager appConfigurationManager=new AppConfigurationManager(ai);
            ai.setConfigInterface(configInterface == null ? appConfigurationManager : configInterface);
            Log.v(AppInfraLogEventID.AI_APPINFRA, "AppConfig Intitialization Done");

            ai.setTime(mTimeSyncInterfaceBuilder == null ? new TimeSyncSntpClient(ai) : mTimeSyncInterfaceBuilder);
            Log.v(AppInfraLogEventID.AI_APPINFRA, "TimeSync Intitialization Done");

            //ai.setAppInfraLogger(aiLogger == null ? new AppInfraLogging(ai) : aiLogger);
            ai.setSecureStorage(secStor == null ? new SecureStorage(ai) : secStor);
            Log.v(AppInfraLogEventID.AI_APPINFRA, "SecureStorage Intitialization Done");
            ai.setLogging(logger == null ? new AppInfraLogging(ai) : logger);
            Log.v(AppInfraLogEventID.AI_APPINFRA, "Logging Intitialization Done");
            // ai.setLogging(new AppInfraLogging(ai));
            ai.getAppInfraLogInstance().log(LoggingInterface.LogLevel.INFO, AppInfraLogEventID.AI_APPINFRA,
                    "Device name:"+android.os.Build.MANUFACTURER+" "+ android.os.Build.MODEL+" "+" OS version:"+Build.VERSION.RELEASE);

            ai.setAbTesting(aIabtesting == null ? new ABTestClientManager(ai) : aIabtesting);

            ai.setAppIdentity(appIdentity == null ? new AppIdentityManager(ai) : appIdentity);
            Log.v(AppInfraLogEventID.AI_APPINFRA, "AppIdentity Intitialization Done");
            ai.setLocal(local == null ? new InternationalizationManager(ai) : local);
            Log.v(AppInfraLogEventID.AI_APPINFRA, "Local Intitialization Done");

            ai.setServiceDiscoveryInterface(mServiceDiscoveryInterface == null ? new ServiceDiscoveryManager(ai) : mServiceDiscoveryInterface);
            Log.v(AppInfraLogEventID.AI_APPINFRA, "ServiceDiscovery Intitialization Done");
            if (ai.getAppIdentity() != null) {
                final StringBuilder appInfraLogStatement = new StringBuilder();

                try {
                    appInfraLogStatement.append("AppInfra initialized for application \"");
                    appInfraLogStatement.append(ai.getAppIdentity().getAppName());
                    appInfraLogStatement.append("\" version \"");
                    appInfraLogStatement.append(ai.getAppIdentity().getAppVersion());
                    appInfraLogStatement.append("\" in state \"");
                    appInfraLogStatement.append(ai.getAppIdentity().getAppState());

                } catch (IllegalArgumentException e) {
                    Log.e(AppInfraLogEventID.AI_APPINFRA, e.getMessage());
                }
                appInfraLogStatement.append("\"");
                ai.getAppInfraLogInstance().log(LoggingInterface.LogLevel.INFO, AppInfraLogEventID.AI_APPINFRA,"AppInfra initialized " +appInfraLogStatement.toString());
            }
            ai.setRestInterface(mRestInterface == null ? new RestManager(ai) : mRestInterface);
            Log.v(AppInfraLogEventID.AI_APPINFRA, "Rest Intitialization Done");

            ai.setTagging(tagging == null ? new AppTagging(ai) : tagging);
            Log.v(AppInfraLogEventID.AI_APPINFRA, "Tagging Intitialization Done");

            Log.v(AppInfraLogEventID.AI_APPINFRA, "AppConfig Migration Starts");

            /////////////
            appConfigurationManager.migrateDynamicData();
            appConfigurationManager.refreshCloudConfig(new AppConfigurationInterface.OnRefreshListener() {
                @Override
                public void onError(AppConfigurationInterface.AppConfigurationError.AppConfigErrorEnum error, String message) {
                    ai.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR,AppInfraLogEventID.AI_APPINFRA,
                            "refreshCloudConfig "+message);

                }
                @Override
                public void onSuccess(REFRESH_RESULT result) {
                    ai.getAppInfraLogInstance().log(LoggingInterface.LogLevel.INFO,AppInfraLogEventID.AI_APPINFRA,
                            "refreshCloudConfig "+result.toString());
                }
            });
            Log.v(AppInfraLogEventID.AI_APPINFRA, "AppConfig Migration ENDS");

            ai.setLanguagePackInterface(languagePack == null? new LanguagePackManager(ai) : languagePack);
            Log.v(AppInfraLogEventID.AI_APPINFRA, "Language Pack Initialization done");

            AppUpdateManager appUpdateManager = new AppUpdateManager(ai);

            ai.setAppupdateInterface(appupdateInterface == null ? appUpdateManager : appupdateInterface);
            Log.v(AppInfraLogEventID.AI_APPINFRA, "AppUpdate Initialization done");
            Log.v(AppInfraLogEventID.AI_APPINFRA, "AppUpdate Auto Refresh Starts");
            try {
                Object isappUpdateRq = getAutoRefreshValue(appConfigurationManager);
                if (isappUpdateRq != null && isappUpdateRq instanceof Boolean) {
                    final Boolean isautorefreshEnabled = (Boolean) isappUpdateRq;
                    File appupdateCache = appUpdateManager.getAppUpdatefromCache(AppUpdateConstants.LOCALE_FILE_DOWNLOADED
                            , AppUpdateConstants.APPUPDATE_PATH);
                    if (appupdateCache != null && appupdateCache.exists() && appupdateCache.length() > 0) {
                        ai.getAppInfraLogInstance().log(LoggingInterface.LogLevel.INFO, AppInfraLogEventID.AI_APPINFRA,
                                "AppUpdate Auto Refresh Cache is available");
                    } else if (isautorefreshEnabled) {
                        appUpdateManager.refresh(new AppUpdateInterface.OnRefreshListener() {
                            @Override
                            public void onError(AIAppUpdateRefreshResult error, String message) {
                                ai.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR,AppInfraLogEventID.AI_APPINFRA,
                                        "AppConfiguration Auto refresh failed- AppUpdate" + " " + error);
                            }

                            @Override
                            public void onSuccess(AIAppUpdateRefreshResult result) {
                                ai.getAppInfraLogInstance().log(LoggingInterface.LogLevel.INFO,AppInfraLogEventID.AI_APPINFRA,
                                        "AppConfiguration Auto refresh success- AppUpdate" + " " + result);
                            }
                        });
                    }
                } else {
                    ai.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, AppInfraLogEventID.AI_APPINFRA,
                            "AppConfiguration Auto refresh failed- AppUpdate");
                }
            } catch (IllegalArgumentException exception) {
                ai.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR,AppInfraLogEventID.AI_APPINFRA,
                       "AppConfiguration "+exception.toString());
            }
            Log.v(AppInfraLogEventID.AI_APPINFRA, "AppUpdate Auto Refresh ENDS");
            Log.v(AppInfraLogEventID.AI_APPINFRA, "AppInfra Initialization ENDS");
            postLog(ai,startTime, "App-infra initialization ends with ");
            return ai;
        }

    }

    private String getAndroidOSVersion()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("android : ").append(Build.VERSION.RELEASE);

        Field[] fields = Build.VERSION_CODES.class.getFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            int fieldValue = -1;

            try {
                fieldValue = field.getInt(new Object());
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

            if (fieldValue == Build.VERSION.SDK_INT) {
                builder.append(" : ").append(fieldName).append(" : ");
                builder.append("sdk=").append(fieldValue);
            }
        }

        return "OS: " + builder.toString();
    }
}
