package com.philips.platform.appinfra.logging;

import androidx.annotation.NonNull;
import android.util.Log;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.logging.database.AILCloudLogDBManager;
import com.philips.platform.appinfra.logging.database.AILCloudLogDataBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * Created by abhishek on 4/25/18.
 */

public class CloudLogHandler extends Handler {

    private static final String TAG = CloudLogHandler.class.getSimpleName();

    private AppInfraInterface appInfra;

    private AILCloudLogDataBuilder ailCloudLogDataBuilder;


    private CloudLogProcessor cloudLogProcessor;


    public CloudLogHandler(AppInfraInterface appInfra) {
        this.appInfra = appInfra;
        ailCloudLogDataBuilder = getAilCloudLogDataBuilder(appInfra);
        cloudLogProcessor = getCloudLogProcessor();
        cloudLogProcessor.start();
        cloudLogProcessor.prepareHandler();
    }

    @NonNull
    AILCloudLogDataBuilder getAilCloudLogDataBuilder(AppInfraInterface appInfra) {
        return new AILCloudLogDataBuilder(appInfra);
    }

    @NonNull
    CloudLogProcessor getCloudLogProcessor() {
        return new CloudLogProcessor("cloud log handler thread");
    }

    @Override
    public void publish(final LogRecord logRecord) {
        //To avoid looping of logs check if logs are coming from AppInfra
        if (checkIfComponentIdIsAppInfra(logRecord) || isRestrictedLogLevel(logRecord)) {
            return;
        }
        cloudLogProcessor.postTask(new Runnable() {
            @Override
            public void run() {
                try {
                    getLogDbManager().insertLog(ailCloudLogDataBuilder.buildCloudLogModel(logRecord));
                } catch (AILCloudLogDataBuilder.MessageSizeExceedsException e) {
                    Log.v(TAG, "Message size exceeds allowed length" + e.getMessage());
                }
            }
        });
    }

    private boolean isRestrictedLogLevel(LogRecord logRecord) {
        String logLevel=null;
        if (logRecord.getLevel() != null) {
            logLevel = LoggingUtils.getAILLogLevel(logRecord.getLevel().toString());
        }
        if (logLevel != null && (logLevel.equalsIgnoreCase("VERBOSE") || logLevel.equalsIgnoreCase("DEBUG"))) {
            return true;
        }
        return false;
    }

    private boolean checkIfComponentIdIsAppInfra(LogRecord logRecord) {
        if (logRecord != null && logRecord.getParameters() != null) {
            List<Object> parameters = Arrays.asList(logRecord.getParameters());
            if (parameters != null && parameters.get(AppInfraLogging.COMPONENT_ID_INDEX) != null && parameters.get(AppInfraLogging.COMPONENT_ID_INDEX).toString().equalsIgnoreCase(((AppInfra)appInfra).getComponentId())) {
                return true;
            }
        }
        return false;
    }

    AILCloudLogDBManager getLogDbManager() {
        return AILCloudLogDBManager.getInstance(appInfra);
    }

    @Override
    public void flush() {

    }

    @Override
    public void close() throws SecurityException {

    }
}
