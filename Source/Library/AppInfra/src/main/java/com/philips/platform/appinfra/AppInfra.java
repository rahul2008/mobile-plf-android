/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra;

import android.content.Context;

import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationManager;
import com.philips.platform.appinfra.appidentity.AppIdentityInterface;
import com.philips.platform.appinfra.appidentity.AppIdentityManager;
import com.philips.platform.appinfra.internationalization.InternationalizationInterface;
import com.philips.platform.appinfra.internationalization.InternationalizationManager;
import com.philips.platform.appinfra.logging.AppInfraLogging;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.securestorage.SecureStorage;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryManager;
import com.philips.platform.appinfra.tagging.AppTagging;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.appinfra.timesync.TimeInterface;
import com.philips.platform.appinfra.timesync.TimeSyncSntpClient;

/**
 * Created by 310238114 on 5/5/2016.
 */
public class AppInfra implements AppInfraInterface {


    private SecureStorageInterface secureStorage;
    private LoggingInterface logger;
    private AppTaggingInterface tagging;
    private LoggingInterface appInfraLogger;
    private AppIdentityInterface appIdentity;
    private InternationalizationInterface local;
    private ServiceDiscoveryInterface mServiceDiscoveryInterface;
    private TimeInterface mTimeSyncInterface;
    private AppConfigurationInterface configInterface;


    /**
     * The App infra context. This MUST be Application context
     */
    Context appInfraContext;

    /**
     * The type Builder.
     */
    public static class Builder {

        private SecureStorageInterface secStor;
        private LoggingInterface logger; // builder logger
        private LoggingInterface aiLogger; // app infra logger
        private AppTaggingInterface tagging;
        private AppIdentityInterface appIdentity;
        private InternationalizationInterface local;
        private ServiceDiscoveryInterface mServiceDiscoveryInterface;
        private TimeInterface mTimeSyncInterfaceBuilder;


        private AppConfigurationInterface configInterface;

        /**
         * Instantiates a new Builder.
         * It can be configured with Builder setter methods
         */
        public Builder() {
            secStor = null;
            logger = null;
            aiLogger = null;
            tagging = null;
            appIdentity = null;
            local = null;
            mServiceDiscoveryInterface = null;
            mTimeSyncInterfaceBuilder = null;
            configInterface = null;
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

        /**
         * Sets Builder time sync overriding the default implementation.
         *
         * @param timeSyncSntpClient the time sync sntp client
         * @return the time sync
         */
        public Builder setTimeSync(TimeSyncSntpClient timeSyncSntpClient) {
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

            AppInfra ai = new AppInfra(pContext);
            ai.setTime(mTimeSyncInterfaceBuilder == null ? new TimeSyncSntpClient(ai) : mTimeSyncInterfaceBuilder);
            ai.setTagging(tagging == null ? new AppTagging(ai) : tagging);
            //ai.setAppInfraLogger(aiLogger == null ? new AppInfraLogging(ai) : aiLogger);
            ai.setSecureStorage(secStor == null ? new SecureStorage(ai) : secStor);
            ai.setLogging(logger == null ? new AppInfraLogging(ai) : logger);
            // ai.setLogging(new AppInfraLogging(ai));


            ai.setAppIdentity(appIdentity == null ? new AppIdentityManager(ai) : appIdentity);
            ai.setLocal(local == null ? new InternationalizationManager(ai) : local);

            ai.setServiceDiscoveryInterface(mServiceDiscoveryInterface == null ? new ServiceDiscoveryManager(ai) : mServiceDiscoveryInterface);
            ai.setConfigInterface(configInterface == null ? new AppConfigurationManager(ai) : configInterface);
            if (ai.getAppIdentity() != null) {
                StringBuilder appInfraLogStatement = new StringBuilder();
                appInfraLogStatement.append("AppInfra initialized for application \"");
                appInfraLogStatement.append(ai.getAppIdentity().getAppName());
                appInfraLogStatement.append("\" version \"");
                appInfraLogStatement.append(ai.getAppIdentity().getAppVersion());
                appInfraLogStatement.append("\" in state \"");
                appInfraLogStatement.append(ai.getAppIdentity().getAppState());
                appInfraLogStatement.append("\"");
                ai.getAppInfraLogInstance().log(LoggingInterface.LogLevel.INFO, "AppInfra initialized", appInfraLogStatement.toString());
            }

            return ai;
        }
    }


    public Context getAppInfraContext() {
        return appInfraContext;
    }

    @Override
    public TimeInterface getTime() {
        return mTimeSyncInterface;
    }

    @Override
    public SecureStorageInterface getSecureStorage() {
        return secureStorage;
    }

    @Override
    public AppIdentityInterface getAppIdentity() {
        return appIdentity;
    }

    @Override
    public InternationalizationInterface getInternationalization() {
        return local;
    }

    @Override
    public LoggingInterface getLogging() {
        return logger;
    }

    @Override
    public ServiceDiscoveryInterface getServiceDiscovery() {
        return mServiceDiscoveryInterface;
    }

    @Override
    public AppConfigurationInterface getConfigInterface() {
        return configInterface;
    }

    public AppInfra(Context pContext) {
        appInfraContext = pContext;
    }

    private void setTime(TimeInterface mTimeSyncInterface) {
        this.mTimeSyncInterface = mTimeSyncInterface;
    }

    private void setSecureStorage(SecureStorageInterface sec) {
        secureStorage = sec;
    }

    private void setLogging(LoggingInterface log) {
        logger = log;
        appInfraLogger = logger.createInstanceForComponent(this.getClass().getSimpleName(),
                BuildConfig.VERSION_NAME);
    }

    private void setTagging(AppTaggingInterface tagg) {
        tagging = tagg;

    }

    private void setAppIdentity(AppIdentityInterface identity) {
        appIdentity = identity;

    }

    private void setLocal(InternationalizationInterface locale) {
        local = locale;

    }

    private void setServiceDiscoveryInterface(ServiceDiscoveryInterface mServiceDiscoveryInterfac) {
        mServiceDiscoveryInterface = mServiceDiscoveryInterfac;

    }


    public AppTaggingInterface getTagging() {
        return tagging;
    }


    public LoggingInterface getAppInfraLogInstance() { // this log should be used withing App Infra library
        return appInfraLogger;
    }


    public void setConfigInterface(AppConfigurationInterface configInterface) {
        this.configInterface = configInterface;
    }

}
