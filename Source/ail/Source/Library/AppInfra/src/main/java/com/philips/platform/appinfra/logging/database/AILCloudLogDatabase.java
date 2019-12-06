package com.philips.platform.appinfra.logging.database;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

/**
 * Created by abhishek on 4/25/18.
 */
@Database(entities = {AILCloudLogData.class}, version = 1,exportSchema = false)
public abstract class AILCloudLogDatabase extends RoomDatabase {

    private static AILCloudLogDatabase INSTANCE;

    public abstract AILCloudLogDao logModel();

    public static AILCloudLogDatabase getPersistenceDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), AILCloudLogDatabase.class,"ail_cloud_log_db")
                            .build();
        }
        return INSTANCE;
    }

}
