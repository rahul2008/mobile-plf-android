package com.philips.platform.appinfra.logging;

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

    public CloudLogHandler(AppInfra appInfra, LoggingConfiguration loggingConfiguration) {
        this.appInfra = appInfra;
        ailCloudLogDataBuilder = new AILCloudLogDataBuilder(appInfra, loggingConfiguration);
        this.loggingConfiguration = loggingConfiguration;
    }

    @Override
    public void publish(LogRecord logRecord) {
        AILCloudLogDatabase ailCloudLogDatabase = AILCloudLogDatabase.getPersistenceDatabase(appInfra.getAppInfraContext());
        AILCloudLogData ailCloudLogData;
        if (ailCloudLogDataBuilder.buildCloudLogModel(logRecord) != null) {
            ailCloudLogData = ailCloudLogDataBuilder.buildCloudLogModel(logRecord);
            checkSizeForDBInsertion(ailCloudLogDatabase,ailCloudLogData);

        }

    }

    private void checkSizeForDBInsertion(AILCloudLogDatabase ailCloudLogDatabase,AILCloudLogData ailCloudLogData) {
        if(ailCloudLogDatabase.logModel().getNumberOfRows(ailCloudLogData) > CloudLoggingConstants.MAX_ROWS_IN_DB){
            ailCloudLogDatabase.logModel().deleteGivenRows(fetchRemovingDataFromDB( ailCloudLogDatabase, ailCloudLogData));
        }
        ailCloudLogDatabase.logModel().insertLog(ailCloudLogData);
        Log.d("test", "Log messsage" + ailCloudLogData.toString());
    }

    private AILCloudLogData fetchRemovingDataFromDB(AILCloudLogDatabase ailCloudLogDatabase, AILCloudLogData ailCloudLogData) {
        AILCloudLogData rowsWithSeverityLessThanWarning = ailCloudLogDatabase.logModel().getRowsWithSeverityLessThanWarning(ailCloudLogData);
        int countOfRowsWithSeverityLessThanWarning = ailCloudLogDatabase.logModel().getNumberOfRows(rowsWithSeverityLessThanWarning);
        if(countOfRowsWithSeverityLessThanWarning > 0){
            if(countOfRowsWithSeverityLessThanWarning >= CloudLoggingConstants.ROWS_TO_BE_DELETED_BATCH_SIZE){
               return ailCloudLogDatabase.logModel().getOldestRowsUnderLimit(rowsWithSeverityLessThanWarning);
            }
            else{
                //check about this flow
                return ailCloudLogDatabase.logModel().getOldestRowsUnderLimit(rowsWithSeverityLessThanWarning);
            }
        }else{
            return ailCloudLogDatabase.logModel().getOldestRowsUnderLimit(ailCloudLogData);
        }
    }

    @Override
    public void flush() {

    }

    @Override
    public void close() throws SecurityException {

    }
}
