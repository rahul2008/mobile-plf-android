package com.philips.platform.appinfra.logging.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import static androidx.room.OnConflictStrategy.IGNORE;

@Dao
public interface AILCloudLogDao {

    @Query("select count(*) from AILCloudLogData where status in ('Error','New')")
    LiveData<Integer> getNumberOfRows();

    @Query("select * FROM AILCloudLogData where status in ('New') order by logTime LIMIT :maxlimit")
    List<AILCloudLogData> getOldestRowsWithMaxLimit(int maxlimit);


    @Insert(onConflict = IGNORE)
    void insertLog(AILCloudLogData log);

    @Delete
    void deleteLogs(List<AILCloudLogData> ailCloudLogDataList);

    @Update
    void updateLogs(List<AILCloudLogData> ailCloudLogDataList);

}
