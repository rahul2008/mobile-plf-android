/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra;

import android.content.Context;

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
import com.philips.platform.appinfra.tagging.AIAppTaggingInterface;
import com.philips.platform.appinfra.timesync.TimeSyncInterface;
import com.philips.platform.appinfra.timesync.TimeSyncSntpClient;

/**
 * Created by 310238114 on 5/5/2016.
 */
public class AppInfra implements AppInfraInterface {


    private SecureStorageInterface secureStorage;
    private LoggingInterface logger;
    private AIAppTaggingInterface tagging;
    private LoggingInterface appInfraLogger;
    private final static String APP_INFRA_VERSION = "1.1.0-rc5";
    private AppIdentityInterface appIdentity;
    private InternationalizationInterface local;
    private ServiceDiscoveryInterface mServiceDiscoveryInterface;
    private TimeSyncInterface mTimeSyncInterface;


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
        private AIAppTaggingInterface tagging;
        private AppIdentityInterface appIdentity;
        private InternationalizationInterface local;
        private ServiceDiscoveryInterface mServiceDiscoveryInterface;
        private TimeSyncInterface mTimeSyncInterfaceBuilder;

        /**
         * Instantiates a new Builder.
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
        }


        /**
         * Sets a local logger for App Infra.
         *
         * @param loggingInterface the logging interface
         * @return the appinfra logger
         */
        public Builder setAiLogger(LoggingInterface loggingInterface) {
            aiLogger = loggingInterface;
            return this;
        }


        /**
         * Sets Builder logging.
         *
         * @param log the log
         * @return the logging
         */
        public Builder setLogging(LoggingInterface log) {
            logger = log;
            return this;
        }

        /**
         * Sets Builder secure storage.
         *
         * @param secureStorage the secure storage
         * @return the secure storage
         */
        public Builder setSecureStorage(SecureStorageInterface secureStorage) {
            secStor = secureStorage;
            return this;
        }

        public Builder setServiceDiscovery(ServiceDiscoveryInterface serviceDiscoveryInterface) {
            mServiceDiscoveryInterface = serviceDiscoveryInterface;
            return this;
        }

        public Builder setTagging(AIAppTaggingInterface aIAppTaggingInterface) {
            tagging = aIAppTaggingInterface;
            return this;
        }

        public Builder setTimeSynce(TimeSyncSntpClient timeSyncSntpClient) {
            mTimeSyncInterfaceBuilder = timeSyncSntpClient;
            return this;
        }


        /**
         * Build App Infra instance with give feature instances overriding the default implementation.
         *
         * @param pContext context
         * @return the app infra
         */
        public AppInfra build(Context pContext) {

            AppInfra ai = new AppInfra(pContext);
            ai.setTimeSync(mTimeSyncInterfaceBuilder == null ? new TimeSyncSntpClient(ai) : mTimeSyncInterfaceBuilder);
            //ai.setAppInfraLogger(aiLogger == null ? new AppInfraLogging(ai) : aiLogger);
            ai.setSecureStorage(secStor == null ? new SecureStorage(ai) : secStor);
            ai.setLogging(logger == null ? new AppInfraLogging(ai) : logger);
           // ai.setLogging(new AppInfraLogging(ai));

            ai.setTagging(tagging == null ? new AppTagging(ai) : tagging);
            ai.setAppIdentity(appIdentity == null ? new AppIdentityManager(ai) : appIdentity);
            ai.setLocal(local == null ? new InternationalizationManager(ai) : local);

            ai.setServiceDiscoveryInterface(mServiceDiscoveryInterface == null ? new ServiceDiscoveryManager(ai) : mServiceDiscoveryInterface);
            return ai;
        }
    }


    public Context getAppInfraContext() {
        return appInfraContext;
    }

    @Override
    public TimeSyncInterface getTimeSync() {
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
    public ServiceDiscoveryInterface getServiceDiscoveryInterface() {
        return mServiceDiscoveryInterface;
    }


    public AppInfra(Context pContext) {
        appInfraContext = pContext;
    }

    private void setTimeSync(TimeSyncInterface mTimeSyncInterface) {
        this.mTimeSyncInterface = mTimeSyncInterface;
    }

    private void setSecureStorage(SecureStorageInterface sec) {
        secureStorage = sec;
    }

    private void setLogging(LoggingInterface log) {
        logger = log;
        appInfraLogger = logger.createInstanceForComponent(this.getClass().getPackage().toString(), APP_INFRA_VERSION);
    }

    private void setTagging(AIAppTaggingInterface tagg) {
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


    public AIAppTaggingInterface getTagging() {
        return tagging;
    }


    public LoggingInterface getAppInfraLogInstance() { // this log should be used withing App Infra library
        return appInfraLogger;
    }


}
