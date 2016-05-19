/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra;

import android.content.Context;

import com.philips.platform.appinfra.logging.AppInfraLogging;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;
import com.philips.platform.appinfra.tagging.AIAppTaggingInterface;

/**
 * Created by 310238114 on 5/5/2016.
 */
public class AppInfra {


    private SecureStorageInterface secStor;
    private LoggingInterface logger;




    private AIAppTaggingInterface tagging;
    private LoggingInterface appInfraLogger;

    private final static String VERSION = "1.2.3";


    /**
     * The App infra context.
     */
    Context appInfraContext;

    /**
     * The type Builder.
     */
    public static class Builder {
        private SecureStorageInterface secStor;
        private LoggingInterface       logger; // builder logger
        private AIAppTaggingInterface tagging;

        /**
         * Instantiates a new Builder.
         */
        public Builder() {
            secStor = null;
            logger = null;
            tagging = null;
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
           // ai.setSecureStorage(secStor == null ? new SecureStorage(ai) : secStor);
            ai.setLogging(logger == null ? new AppInfraLogging(ai) : logger);
           // ai.setLogging(new AppInfraLogging(ai));
            return ai;
        }
    }

    /**
     * Gets secure storage.
     * @return the secure storage
     */
    public SecureStorageInterface getSecureStorage() {
        return secStor;
    }

    /**
     * Gets AppInfra log instance to be used by component and app framework.
     * @return the logging
     */
    public LoggingInterface getLogging() {
        return logger;
    }


    /**
     * Instantiates a new App infra.
     * @param pContext  context
     */
    public AppInfra(Context pContext) {
        appInfraContext = pContext;
    }

    private void setSecureStorage(SecureStorageInterface sec) {
        secStor = sec;
    }

    private void setLogging(LoggingInterface log) {
        logger = log;
        appInfraLogger = logger.createInstanceForComponent(this.getClass().getPackage().toString(), VERSION);
    }

    private void setTagging(AIAppTaggingInterface tagg) {
        tagging = tagg;

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
    LoggingInterface getAppInfraLogInstance() { // this log should be used withing App Infra library
        return appInfraLogger;
    }

    /**
     * Gets app infra context.
     * @return the app infra context
     */
    public Context getAppInfraContext() {
        return appInfraContext;
    }
}
