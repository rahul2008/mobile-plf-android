package com.philips.platform.appinfra.logging.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.IGNORE;

@Dao
public interface AILCloudLogDao {

    @Query("select count(*) from AILCloudLogData where status in ('Error','New')")
    LiveData<Integer> getNumberOfRows();

    @Query("select * FROM AILCloudLogData where status in ('Error','New') order by logTime LIMIT :maxlimit")
    List<AILCloudLogData> getOldestRowsWithMaxLimit(int maxlimit);


    @Insert(onConflict = IGNORE)
    void insertLog(AILCloudLogData log);

    @Delete
    void deleteLogs(List<AILCloudLogData> ailCloudLogDataList);

    @Update
    void updateLogs(List<AILCloudLogData> ailCloudLogDataList);

}
