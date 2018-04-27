package com.philips.platform.appinfra.logging;

import android.content.Context;
import android.util.Log;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.logging.database.AILCloudLogData;
import com.philips.platform.appinfra.logging.database.AILCloudLogDataBuilder;
import com.philips.platform.appinfra.logging.database.AILCloudLogDatabase;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * Created by abhishek on 4/25/18.
 */

public class CloudLogHandler extends Handler {

    private AppInfra appInfra;

    private AILCloudLogDataBuilder ailCloudLogDataBuilder;

    private LoggingConfiguration loggingConfiguration;

    public CloudLogHandler(AppInfra appInfra,LoggingConfiguration loggingConfiguration){
        this.appInfra=appInfra;
        ailCloudLogDataBuilder=new AILCloudLogDataBuilder(appInfra,loggingConfiguration);
        this.loggingConfiguration=loggingConfiguration;
    }
    @Override
    public void publish(LogRecord logRecord) {
        AILCloudLogDatabase ailCloudLogDatabase=AILCloudLogDatabase.getPersistenceDatabase(appInfra.getAppInfraContext());
        ailCloudLogDatabase.logModel().insertLog(ailCloudLogDataBuilder.buildCloudLogModel(logRecord));
        Log.d("test","Log messsage"+logRecord.toString());
    }

    @Override
    public void flush() {

    }

    @Override
    public void close() throws SecurityException {

    }
}
