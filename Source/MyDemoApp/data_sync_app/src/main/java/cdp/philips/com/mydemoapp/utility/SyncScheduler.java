package cdp.philips.com.mydemoapp.utility;

import android.os.Handler;

import cdp.philips.com.mydemoapp.reciever.ScheduleSyncReceiver;

import static com.janrain.android.engage.JREngage.getApplicationContext;

public class SyncScheduler {
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
                    e.printStackTrace();
                } finally {
                    handler.postDelayed(this, ScheduleSyncReceiver.DATA_FETCH_FREQUENCY);
                }
            }
        };
        runnable.run();
    }

    public void stopSync() {
        handler.removeCallbacks(runnable);
        isRunning = false;
    }
}
