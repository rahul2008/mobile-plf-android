/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra;

import android.content.Context;

import com.philips.platform.appinfra.appidentity.AppIdentityInterface;
import com.philips.platform.appinfra.appidentity.AppIdentityManager;
import com.philips.platform.appinfra.logging.AppInfraLogging;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.securestorage.SecureStorage;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;
import com.philips.platform.appinfra.servicediscovery.LocalInterface;
import com.philips.platform.appinfra.servicediscovery.LocalManager;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryManager;
import com.philips.platform.appinfra.tagging.AIAppTagging;
import com.philips.platform.appinfra.tagging.AIAppTaggingInterface;

/**
 * Created by 310238114 on 5/5/2016.
 */
public class AppInfra {


    private SecureStorageInterface secureStorage;
    private LoggingInterface logger;
    private AIAppTaggingInterface tagging;
    private LoggingInterface appInfraLogger;
    private final static String APP_INFRA_VERSION = "1.0.1";
    private AppIdentityInterface appIdentity;
    private LocalInterface local;
    private ServiceDiscoveryInterface mServiceDiscoveryInterface;


    /**
     * The App infra context. This MUST be Application context
     */
    Context appInfraContext;

    /**
     * The type Builder.
     */
    public static class Builder {

        private SecureStorageInterface secStor;
        private LoggingInterface       logger; // builder logger
        private LoggingInterface    aiLogger; // app infra logger
        private AIAppTaggingInterface tagging;
        private AppIdentityInterface appIdentity;
        private LocalInterface local;
        private ServiceDiscoveryInterface mServiceDiscoveryInterface;

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
         * @param log the log
         * @return the logging
         */
        public Builder setLogging(LoggingInterface log) {
            logger = log;
            return this;
        }

        /**
         * Sets Builder secure storage.
         * @param secureStorage the secure storage
         * @return the secure storage
         */
        public Builder setSecureStorage(SecureStorageInterface secureStorage) {
            secStor = secureStorage;
            return this;
        }

        /**
         * Build app infra.
         * @param pContext  context
         * @return the app infra
         */
        public AppInfra build(Context pContext ) {

            AppInfra ai = new AppInfra(pContext);
            //ai.setAppInfraLogger(aiLogger == null ? new AppInfraLogging(ai) : aiLogger);
            ai.setSecureStorage(secStor == null ? new SecureStorage(ai.getAppInfraContext()) : secStor);
            ai.setLogging(logger == null ? new AppInfraLogging(ai) : logger);
            // ai.setLogging(new AppInfraLogging(ai));

            ai.setTagging(tagging == null ? new AIAppTagging(ai) : tagging);
            ai.setAppIdentity(appIdentity == null ? new AppIdentityManager(ai) : appIdentity);
            ai.setLocal(local == null ? new LocalManager(ai) : local);
            ai.setServiceDiscoveryInterface(mServiceDiscoveryInterface == null ? new ServiceDiscoveryManager(ai) : mServiceDiscoveryInterface);
            return ai;
        }
    }


    /**
     * Gets app infra context.
     * @return the app infra context
     */
    public Context getAppInfraContext() {
        return appInfraContext;
    }

    /**
     * Gets secure storage.
     * @return the secure storage value
     */
    public SecureStorageInterface getSecureStorage() {
        return secureStorage;
    }

    public AppIdentityInterface getAppIdentity() {
        return appIdentity;
    }

    public LocalInterface getLocal() {
        return local;
    }

    /**
     * Gets AppInfra log instance to be used by component and app framework.
     * @return the logging
     */
    public LoggingInterface getLogging() {
        return logger;
    }
    public ServiceDiscoveryInterface getServiceDiscoveryInterface() {
        return mServiceDiscoveryInterface;
    }


    /**
     * Instantiates a new App infra.
     * @param pContext  context
     */
    public AppInfra(Context pContext) {
        appInfraContext = pContext;
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

    private void setLocal(LocalInterface locale) {
        local = locale;

    }

    private void setServiceDiscoveryInterface(ServiceDiscoveryInterface mServiceDiscoveryInterfac) {
        mServiceDiscoveryInterface = mServiceDiscoveryInterfac;

    }

    /**
     * Gets AppInfra tagging.
     * @return the tagging
     */
    public AIAppTaggingInterface getTagging() {
        return tagging;
    }


    /**
     * Gets app infra log instance to be used within App Infra library.
     * @return the app infra log instance
     */
    public LoggingInterface getAppInfraLogInstance() { // this log should be used withing App Infra library
        return appInfraLogger;
    }



}
