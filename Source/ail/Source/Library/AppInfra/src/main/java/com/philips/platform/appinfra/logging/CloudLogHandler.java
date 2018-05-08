package com.philips.platform.appinfra.logging;

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
        ailCloudLogDataBuilder = new AILCloudLogDataBuilder(appInfra);
        cloudLogProcessor = new CloudLogProcessor("cloud log handler thread");
        cloudLogProcessor.start();
        cloudLogProcessor.prepareHandler();
    }

    @Override
    public void publish(final LogRecord logRecord) {
        cloudLogProcessor.postTask(new Runnable() {
            @Override
            public void run() {
                try {
                    AILCloudLogDBManager.getInstance(appInfra).insertLog(ailCloudLogDataBuilder.buildCloudLogModel(logRecord));
                } catch (MessageSizeExceedsException e) {
                    Log.d(TAG,"Message size exceeds allowed length"+e.getMessage());
                }
            }
        });
    }

    @Override
    public void flush() {

    }

    @Override
    public void close() throws SecurityException {

    }
}
