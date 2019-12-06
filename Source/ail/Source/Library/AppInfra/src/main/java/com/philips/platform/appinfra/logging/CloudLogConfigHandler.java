package com.philips.platform.appinfra.logging;

import androidx.annotation.NonNull;

import com.philips.platform.appinfra.AppInfraInterface;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by abhishek on 4/25/18.
 */

public class CloudLogConfigHandler {

    private AppInfraInterface appInfra;

    public CloudLogConfigHandler(AppInfraInterface appInfra) {
        this.appInfra = appInfra;
    }

    protected void handleCloudLogConfig(LoggingConfiguration loggingConfiguration, @NonNull Logger logger) {
        CloudLogHandler cloudLogHandler = getCurrentLogCloudLogHandler(logger);
        if (loggingConfiguration.isCloudLogEnabled()) {
            if (cloudLogHandler == null) {
                cloudLogHandler = getCloudLogHandler();
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
                logger.removeHandler(cloudLogHandler);
            }

        }
    }

    @NonNull
    CloudLogHandler getCloudLogHandler() {
        return new CloudLogHandler(appInfra);
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
