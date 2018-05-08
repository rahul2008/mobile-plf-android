package com.philips.platform.appinfra.logging.database;

import android.arch.persistence.db.SupportSQLiteDatabase;

import com.philips.platform.appinfra.AppInfra;

/**
 * Created by abhishek on 5/7/18.
 */

public class AILCloudLogDBManager {
    private static AILCloudLogDBManager ailCloudLogDBManager;
    private AILCloudLogDatabase ailCloudLogDatabase;

    private AILCloudLogDBManager(AppInfra appInfra) {
        ailCloudLogDatabase = AILCloudLogDatabase.getPersistenceDatabase(appInfra.getAppInfraContext());
        SupportSQLiteDatabase sqLiteDatabase = ailCloudLogDatabase.getOpenHelper().getWritableDatabase();
        try {
            sqLiteDatabase.execSQL("drop trigger if exists clear_data_trigger");
            sqLiteDatabase.execSQL("create trigger clear_data_trigger before insert on AILCloudLogData  when (select count(*) from AILCloudLogData)>=1000 Begin delete FROM AILCloudLogData where logId in (select logId from AILCloudLogData order by logTime LIMIT 25); end");
        } catch (Exception exception) {

        }

    }

    public static AILCloudLogDBManager getInstance(AppInfra appInfra) {
        if (ailCloudLogDBManager == null) {
            ailCloudLogDBManager = new AILCloudLogDBManager(appInfra);
        }
        return ailCloudLogDBManager;
    }

    public void insertLog(AILCloudLogData ailCloudLogData) {
        ailCloudLogDatabase.logModel().insertLog(ailCloudLogData);
    }


}
