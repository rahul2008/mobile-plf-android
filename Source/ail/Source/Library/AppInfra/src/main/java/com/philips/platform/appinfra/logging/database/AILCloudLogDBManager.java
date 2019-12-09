package com.philips.platform.appinfra.logging.database;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.logging.CloudLoggingConstants;

import java.util.List;

/**
 * Created by abhishek on 5/7/18.
 */

public class AILCloudLogDBManager {
    private static AILCloudLogDBManager ailCloudLogDBManager;
    private AILCloudLogDatabase ailCloudLogDatabase;

    public enum DBLogState {
        PROCESSING("Processing"), NEW("New"), ERROR("Error"), SYNCED("Synced");

        private String state;

        DBLogState(String state) {
            this.state = state;
        }

        public String getState(){
            return this.state;
        }
    }

    AILCloudLogDBManager(AppInfraInterface appInfra) {
        ailCloudLogDatabase = getPersistenceDatabase(appInfra);
        SupportSQLiteDatabase sqLiteDatabase = ailCloudLogDatabase.getOpenHelper().getWritableDatabase();
        sqLiteDatabase.execSQL("create trigger if not exists clear_data_trigger before insert on AILCloudLogData  when (select count(*) from AILCloudLogData)>=1000 Begin delete FROM AILCloudLogData where logId in (select logId from AILCloudLogData order by logTime LIMIT 25); end");
        sqLiteDatabase.execSQL("update AILCloudLogData set status='New'");
    }

    AILCloudLogDatabase getPersistenceDatabase(AppInfraInterface appInfra) {
        return AILCloudLogDatabase.getPersistenceDatabase(appInfra.getAppInfraContext());
    }

    public static synchronized AILCloudLogDBManager getInstance(AppInfraInterface appInfra) {
        if (ailCloudLogDBManager == null) {
            ailCloudLogDBManager = new AILCloudLogDBManager(appInfra);
        }
        return ailCloudLogDBManager;
    }

    public synchronized void insertLog(AILCloudLogData ailCloudLogData) {
        ailCloudLogDatabase.logModel().insertLog(ailCloudLogData);
    }

    public synchronized LiveData<Integer> getLogCount() {
        return ailCloudLogDatabase.logModel().getNumberOfRows();
    }


    public synchronized List<AILCloudLogData> getNewAILCloudLogRecords() {
        List<AILCloudLogData> ailCloudLogDataList = ailCloudLogDatabase.logModel().getOldestRowsWithMaxLimit(CloudLoggingConstants.HSDP_LOG_MAX_LIMIT);
        for (AILCloudLogData ailCloudLogData : ailCloudLogDataList) {
            ailCloudLogData.status =DBLogState.PROCESSING.getState();
        }
        ailCloudLogDatabase.logModel().updateLogs(ailCloudLogDataList);
        return ailCloudLogDataList;
    }
    public synchronized  void updateAILCloudLogListToNewState(List<AILCloudLogData> ailCloudLogDataList){
        for (AILCloudLogData ailCloudLogData : ailCloudLogDataList) {
            ailCloudLogData.status =DBLogState.NEW.getState();
        }
        ailCloudLogDatabase.logModel().updateLogs(ailCloudLogDataList);
    }

    public synchronized void updateAILCloudLogList(List<AILCloudLogData> ailCloudLogDataList,DBLogState dbLogState){
        for (AILCloudLogData ailCloudLogData : ailCloudLogDataList) {
            ailCloudLogData.status =dbLogState.getState();
        }
        ailCloudLogDatabase.logModel().updateLogs(ailCloudLogDataList);
    }

    public synchronized void deleteLogRecords(List<AILCloudLogData> ailCloudLogDataList) {
        ailCloudLogDatabase.logModel().deleteLogs(ailCloudLogDataList);
    }


}
