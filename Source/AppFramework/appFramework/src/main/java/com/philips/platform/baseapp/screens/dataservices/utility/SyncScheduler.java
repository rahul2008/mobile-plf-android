package com.philips.platform.baseapp.screens.dataservices.utility;

import android.os.Handler;

import com.philips.platform.baseapp.screens.dataservices.reciever.ScheduleSyncReceiver;
import com.philips.platform.baseapp.screens.utility.RALog;

import static com.janrain.android.engage.JREngage.getApplicationContext;

public class SyncScheduler {
    private static String TAG=SyncScheduler.class.getName();
    ScheduleSyncReceiver mScheduleSyncReceiver;
    private static volatile SyncScheduler sSyncScheduler;
    final Handler handler = new Handler();
    Runnable runnable;
    public boolean isRunning = false;

    private SyncScheduler() {
        mScheduleSyncReceiver = new ScheduleSyncReceiver();
    }

    public static synchronized SyncScheduler getInstance() {
        if (sSyncScheduler == null) {
            return sSyncScheduler = new SyncScheduler();
        }
        return sSyncScheduler;
    }

    public void scheduleSync() {

        if (isRunning == true)
            return;

        runnable = new Runnable() {
            @Override
            public void run() {

                isRunning = true;

                try {
                    mScheduleSyncReceiver.onReceive(getApplicationContext());
                } catch (Exception e) {
                    RALog.e(TAG,e.getMessage());
                } finally {
                    handler.postDelayed(this, ScheduleSyncReceiver.DATA_FETCH_FREQUENCY);
                }
            }
        };
        handler.postDelayed(runnable,ScheduleSyncReceiver.DATA_FETCH_FREQUENCY);
    }

    public void stopSync() {
        handler.removeCallbacks(runnable);
        isRunning = false;
    }
}
