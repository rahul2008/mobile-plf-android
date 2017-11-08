package com.philips.platform.dscdemo.utility;

import android.os.Handler;
import android.support.v4.app.FragmentActivity;

import com.philips.platform.dscdemo.moments.SyncByDateRangeFragment;

import static com.janrain.android.engage.JREngage.getApplicationContext;

public class SyncScheduler implements SyncByDateRangeFragment.EnableDisableSync {
	private static volatile SyncScheduler sSyncScheduler;
	private ScheduleSyncReceiver mScheduleSyncReceiver;
	private final Handler handler = new Handler();
	private Runnable runnable;
	private boolean isRunning = false;
	private boolean isSyncEnabled = false;
	private SyncByDateRangeFragment.EnableDisableSync enableDisableSync;
	private FragmentActivity mActivity;



	private SyncScheduler() {
		mScheduleSyncReceiver = new ScheduleSyncReceiver();
	}

	public SyncScheduler(FragmentActivity fragmentActivity) {
		mActivity = fragmentActivity;
		enableDisableSync = (SyncByDateRangeFragment.EnableDisableSync) fragmentActivity;
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

	public boolean getSyncStatus() {
		return isRunning;
	}



	@Override
	public void isSyncEnabled(boolean isSyncEnabled) {
		this.isSyncEnabled = isSyncEnabled;
	}
}
