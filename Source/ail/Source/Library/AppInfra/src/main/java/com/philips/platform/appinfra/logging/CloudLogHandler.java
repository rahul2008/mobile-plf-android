package com.philips.platform.appinfra.logging;

import android.support.annotation.NonNull;
import android.util.Log;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.logging.database.AILCloudLogDBManager;
import com.philips.platform.appinfra.logging.database.AILCloudLogDataBuilder;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * Created by abhishek on 4/25/18.
 */

public class CloudLogHandler extends Handler {

    private static final String TAG=CloudLogHandler.class.getSimpleName();

    private AppInfra appInfra;

    private AILCloudLogDataBuilder ailCloudLogDataBuilder;


    private CloudLogProcessor cloudLogProcessor;


    public CloudLogHandler(AppInfra appInfra) {
        this.appInfra = appInfra;
        ailCloudLogDataBuilder = getAilCloudLogDataBuilder(appInfra);
        cloudLogProcessor = getCloudLogProcessor();
        cloudLogProcessor.start();
        cloudLogProcessor.prepareHandler();
    }

    @NonNull
    AILCloudLogDataBuilder getAilCloudLogDataBuilder(AppInfra appInfra) {
        return new AILCloudLogDataBuilder(appInfra);
    }

    @NonNull
    CloudLogProcessor getCloudLogProcessor() {
        return new CloudLogProcessor("cloud log handler thread");
    }

    @Override
    public void publish(final LogRecord logRecord) {
        cloudLogProcessor.postTask(new Runnable() {
            @Override
            public void run() {
                try {
                    getLogDbManager().insertLog(ailCloudLogDataBuilder.buildCloudLogModel(logRecord));
                } catch (MessageSizeExceedsException e) {
                    Log.v(TAG,"Message size exceeds allowed length"+e.getMessage());
                }
            }
        });
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
