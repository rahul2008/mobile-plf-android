package com.philips.platform.appinfra.logging.sync;

import android.util.Log;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.logging.LoggingConfiguration;
import com.philips.platform.appinfra.logging.database.AILCloudLogDBManager;
import com.philips.platform.appinfra.logging.database.AILCloudLogData;
import com.philips.platform.appinfra.logging.rest.CloudLogRequestBodyBuilder;

import java.util.List;

/**
 * Created by abhishek on 5/14/18.
 */

public class CloudLogSyncRunnable implements Runnable {



    private AILCloudLogDBManager ailCloudLogDBManager;

    private AppInfra appInfra;


    public CloudLogSyncRunnable(AppInfra appInfra) {
        this.appInfra=appInfra;
        ailCloudLogDBManager = AILCloudLogDBManager.getInstance(appInfra);
    }

    @Override
    public void run() {
        //Check whether to start sync or not
        //1. Fetch oldest data from DB
        List<AILCloudLogData> ailCloudLogDataList = ailCloudLogDBManager.getNewAILCloudLogRecords();
        if (ailCloudLogDataList != null && ailCloudLogDataList.size() > 0) {
            Log.d("SyncTesting", "About to sync records" + ailCloudLogDataList.size());
            //2. Build rest api call template
            //3. Make rest api call
            new CloudLogRequestBodyBuilder(appInfra).getCloudLogRequestBody(ailCloudLogDataList);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //4. Based on status delete data from db
            ailCloudLogDBManager.deleteLogRecords(ailCloudLogDataList);
            Log.d("SyncTesting", "Deleted records" + ailCloudLogDataList.size());
        }
    }
}
