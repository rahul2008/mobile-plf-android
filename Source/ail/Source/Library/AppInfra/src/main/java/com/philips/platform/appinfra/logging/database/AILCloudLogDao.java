package com.philips.platform.appinfra.logging.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.TypeConverters;

import static android.arch.persistence.room.OnConflictStrategy.IGNORE;

/**
 * Created by abhishek on 4/25/18.
 */

@Dao
@TypeConverters(DateConverter.class)
public interface AILCloudLogDao {
    @Insert(onConflict = IGNORE)
    void insertLog(AILCloudLogData log);
}
