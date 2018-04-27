package com.philips.platform.appinfra.logging;

import android.support.annotation.NonNull;

import com.philips.platform.appinfra.AppInfra;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by abhishek on 4/25/18.
 */

public class CloudLogConfigHandler {

    private AppInfra appInfra;

    public CloudLogConfigHandler(AppInfra appInfra) {
        this.appInfra = appInfra;
    }

    protected void handleCloudLogConfig(LogFormatter logFormatter, LoggingConfiguration loggingConfiguration, @NonNull Logger logger) {
        boolean isCloudLogEnabled=loggingConfiguration.isCloudLogEnabled();
        if(isCloudLogEnabled){
            CloudLogHandler cloudLogHandler=getCurrentLogCloudLogHandler(logger);
            if(cloudLogHandler==null){
                cloudLogHandler=new CloudLogHandler(appInfra,loggingConfiguration);
                if (null != logger.getLevel()) {
                    cloudLogHandler.setLevel(logger.getLevel());
                } else {
                    // for appinfra internal log mJavaLogger will be null
                    cloudLogHandler.setLevel(Level.FINE);
                }
                logger.addHandler(cloudLogHandler);
            }
        }else{
            final Handler[] currentComponentHandlers =logger.getHandlers();
            if (null != currentComponentHandlers && currentComponentHandlers.length > 0) {
                for (Handler handler : currentComponentHandlers) {
                    if (handler instanceof CloudLogHandler) {
                        handler.close(); // flush and close connection of file
                        logger.removeHandler(handler);
                    }
                }
            }
        }
    }

    private CloudLogHandler getCurrentLogCloudLogHandler(Logger logger) {
        CloudLogHandler cloudLogHandler = null;
        if (null != logger) {
            Handler[] currentComponentHandlers = logger.getHandlers();
            if (null != currentComponentHandlers && currentComponentHandlers.length > 0) {
                for (Handler handler : currentComponentHandlers) {
                    if (handler instanceof CloudLogHandler) {
                        cloudLogHandler = (CloudLogHandler) handler;
                        break;
                    }
                }
            }
        }
        return cloudLogHandler;
    }
}
