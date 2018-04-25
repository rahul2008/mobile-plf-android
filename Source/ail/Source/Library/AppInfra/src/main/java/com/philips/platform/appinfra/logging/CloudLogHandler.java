package com.philips.platform.appinfra.logging;

import android.content.Context;
import android.util.Log;

import com.philips.platform.appinfra.logging.database.AILCloudLogData;
import com.philips.platform.appinfra.logging.database.AILCloudLogDatabase;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * Created by abhishek on 4/25/18.
 */

public class CloudLogHandler extends Handler {

    private  Context context;

    public CloudLogHandler(Context context){
        this.context=context;
    }
    @Override
    public void publish(LogRecord logRecord) {
        AILCloudLogData ailCloudLogData=new AILCloudLogData();
        ailCloudLogData.id=""+System.currentTimeMillis();
        ailCloudLogData.details=logRecord.getMessage();
        ailCloudLogData.severity=logRecord.getLevel().toString();
        AILCloudLogDatabase ailCloudLogDatabase=AILCloudLogDatabase.getPersistenceDatabase(context);
        ailCloudLogDatabase.logModel().insertLog(ailCloudLogData);

        Log.d("test","Log messsage"+logRecord.toString());
    }

    @Override
    public void flush() {

    }

    @Override
    public void close() throws SecurityException {

    }
}
