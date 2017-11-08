package com.philips.platform.dscdemo.utility;

import android.os.Handler;


import static com.janrain.android.engage.JREngage.getApplicationContext;

public class SyncScheduler {
	private static volatile SyncScheduler sSyncScheduler;
	private ScheduleSyncReceiver mScheduleSyncReceiver;
	private final Handler handler = new Handler();
	private Runnable runnable;
	private boolean isRunning = false;
	private boolean isSyncEnabled = true;


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
		if (isRunning)
			return;

		if (isSyncEnabled) {
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
	}

	public void stopSync() {
		handler.removeCallbacks(runnable);
		isRunning = false;
	}

	public boolean getSyncStatus() {
		return isRunning;
	}

	public void setSyncEnable(boolean isSyncEnabled) {
		this.isSyncEnabled = isSyncEnabled;
	}
}
