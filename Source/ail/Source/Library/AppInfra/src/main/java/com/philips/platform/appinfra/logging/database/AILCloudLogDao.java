package com.philips.platform.appinfra.logging.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import static android.arch.persistence.room.OnConflictStrategy.IGNORE;

@Dao
public interface AILCloudLogDao {

//    @Query("SELECT COUNT(*) FROM AILCloudLogData,log WHERE log.logId = AILCloudLogData.logId")
//    int getNumberOfRows(AILCloudLogData log);
//
//    @Query("SELECT * FROM AILCloudLogData,log WHERE severity in ('DEBUG','INFO','VERBOSE') AND log.logId = AILCloudLogData.logId")
//    AILCloudLogData getRowsWithSeverityLessThanWarning(AILCloudLogData log);
//
////TODO: fetch limit from constants
//
//    @Query("SELECT * FROM AILCloudLogData,log WHERE log.logId = AILCloudLogData.logId ORDER BY logTime LIMIT 20 ")
//    AILCloudLogData getOldestRowsUnderLimit(AILCloudLogData log);
//
//    @Query("DELETE FROM AILCloudLogData where log.logId = AILCloudLogData.logId")
//    void deleteGivenRows(AILCloudLogData log);

    @Query("select count(*) from AILCloudLogData")
    int getNumberOfRows();

    @Query("delete FROM AILCloudLogData where logId in (select logId from AILCloudLogData where severity in ('DEBUG','INFO','VERBOSE') order by logTime LIMIT :batch)")
    void deleteOldestRowsByBatchSize(int batch);


    @Insert(onConflict = IGNORE)
    void insertLog(AILCloudLogData log);


}
