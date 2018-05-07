package com.philips.platform.appinfra.logging;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
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

    private CloudLogProcessor cloudLogProcessor;

    private AILCloudLogDatabase ailCloudLogDatabase;

    public CloudLogHandler(AppInfra appInfra, LoggingConfiguration loggingConfiguration) {
        this.appInfra = appInfra;
        ailCloudLogDataBuilder = new AILCloudLogDataBuilder(appInfra, loggingConfiguration);
        this.loggingConfiguration = loggingConfiguration;
        ailCloudLogDatabase = AILCloudLogDatabase.getPersistenceDatabase(appInfra.getAppInfraContext());
        SupportSQLiteDatabase sqLiteDatabase = ailCloudLogDatabase.getOpenHelper().getWritableDatabase();
        sqLiteDatabase.execSQL("drop trigger if exists clear_data_trigger");
        sqLiteDatabase.execSQL("create trigger clear_data_trigger before insert on AILCloudLogData  when (select count(*) from AILCloudLogData)>=1000 Begin delete FROM AILCloudLogData where logId in (select logId from AILCloudLogData order by logTime LIMIT 25); end");
        cloudLogProcessor = new CloudLogProcessor("cloud log handler thread");
        cloudLogProcessor.start();
        cloudLogProcessor.prepareHandler();
    }

    @Override
    public void publish(final LogRecord logRecord) {
        cloudLogProcessor.postTask(new Runnable() {
            @Override
            public void run() {
                ailCloudLogDatabase.logModel().insertLog(ailCloudLogDataBuilder.buildCloudLogModel(logRecord));
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
