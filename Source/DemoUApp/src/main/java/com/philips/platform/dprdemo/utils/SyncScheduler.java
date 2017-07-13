/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.dprdemo.utils;

import android.os.Handler;

import com.philips.platform.dprdemo.reciever.ScheduleSyncReceiver;

import static com.janrain.android.engage.JREngage.getApplicationContext;

public class SyncScheduler {
    private static volatile SyncScheduler sSyncScheduler;
    private final Handler handler = new Handler();

    private ScheduleSyncReceiver mScheduleSyncReceiver;
    private boolean mIsRunning = false;

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
        if (mIsRunning)
            return;

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                mIsRunning = true;
                try {
                    mScheduleSyncReceiver.onReceive(getApplicationContext());
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    handler.postDelayed(this, ScheduleSyncReceiver.DATA_FETCH_FREQUENCY);
                }
            }
        };
        runnable.run();
    }

   /* public void stopSync() {
        handler.removeCallbacks(runnable);
        mIsRunning = false;
    }*/
}
