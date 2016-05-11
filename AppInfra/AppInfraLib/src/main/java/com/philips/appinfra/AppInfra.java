package com.philips.appinfra;

import android.content.Context;

import com.philips.appinfra.securestorage.SecureStorageInterface;

/**
 * Created by 310238114 on 5/5/2016.
 */
public class AppInfra {


    private SecureStorageInterface secStor;
    private LoggingInterface logger;
    private LoggingInterface appInfraLogger;
    private final static String VERSION = "1.2.3";



    Context appInfraContext;

    public static class Builder {
        private SecureStorageInterface secStor;
        private LoggingInterface       logger;

        public Builder() {
            secStor = null;
            logger = null;
        }
        public Builder setLogging(LoggingInterface log) {
            logger = log;
            return this;
        }
        public Builder setSecureStorage(SecureStorageInterface secureStorage) {
            secStor = secureStorage;
            return this;
        }
        public AppInfra build(Context pContext ) {

            AppInfra ai = new AppInfra(pContext);
           // ai.setSecureStorage(secStor == null ? new SecureStorage(ai) : secStor);
            ai.setLogging(logger == null ? new AILogging(ai) : logger);
           // ai.setLogging(new AILogging(ai));
            return ai;
        }
    }

    public SecureStorageInterface getSecureStorage() {
        return secStor;
    }

    public LoggingInterface getLogging() {
        return logger;
    }

    /*private AppInfra() {
    }*/
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

    protected LoggingInterface getAppInfraLogInstance() {
        return appInfraLogger;
    }
    public Context getAppInfraContext() {
        return appInfraContext;
    }
}
