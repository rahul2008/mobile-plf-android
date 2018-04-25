package com.philips.platform.appinfra.logging;

import android.util.Log;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * Created by abhishek on 4/25/18.
 */

public class CloudLogHandler extends Handler {
    @Override
    public void publish(LogRecord logRecord) {
        Log.d("test","Log messsage"+logRecord);
    }

    @Override
    public void flush() {

    }

    @Override
    public void close() throws SecurityException {

    }
}
