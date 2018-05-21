package com.philips.platform.appinfra.logging;

import android.support.annotation.NonNull;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.logging.sync.CloudLogSyncManager;

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

    protected void handleCloudLogConfig(LoggingConfiguration loggingConfiguration, @NonNull Logger logger) {
        CloudLogHandler cloudLogHandler = getCurrentLogCloudLogHandler(logger);
        if (loggingConfiguration.isCloudLogEnabled()) {
            //Initialize SyncManager for sync cloud log to server
            CloudLogSyncManager.getInstance(appInfra,loggingConfiguration);
            if (cloudLogHandler == null) {
                cloudLogHandler = new CloudLogHandler(appInfra);
                if (null != logger.getLevel()) {
                    cloudLogHandler.setLevel(logger.getLevel());
                } else {
                    cloudLogHandler.setLevel(Level.FINE);
                }
                logger.addHandler(cloudLogHandler);
            }
        } else {
            if (cloudLogHandler != null) {
                cloudLogHandler.close();
                ;
                logger.removeHandler(cloudLogHandler);
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
