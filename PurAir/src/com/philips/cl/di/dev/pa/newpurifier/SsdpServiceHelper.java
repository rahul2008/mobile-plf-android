package com.philips.cl.di.dev.pa.newpurifier;

import java.util.ArrayList;
import java.util.Set;

import android.os.Handler.Callback;
import android.os.HandlerThread;

import com.philips.cl.di.common.ssdp.lib.SsdpService;
import com.philips.cl.di.common.ssdp.models.DeviceModel;
import com.philips.cl.di.dev.pa.newpurifier.SsdpServiceHelperThread.StartStopInterface;
import com.philips.cl.di.dev.pa.util.ALog;

public class SsdpServiceHelper implements StartStopInterface {

	private SsdpService mSsdpService = null;
	private Callback mSsdpCallback = null;

	private Object threadLock = new Object();
	private SsdpServiceHelperThread mThread = null;

	private static final int TIMEOUT_STARTTHREAD = 3000;
	private static final int DELAY_STOPSSDP = 3000;

	private int mTestDelay = -1;

	public SsdpServiceHelper(SsdpService ssdpService, Callback callback) {
		mSsdpService = ssdpService;
		mSsdpCallback = callback;
	}

	public void startDiscoveryAsync() {
		synchronized (threadLock) {
			if (mThread == null) {
				createNewStartStopThread();
			}
			mThread.startDiscoveryAsync();
		}
	}

	public void stopDiscoveryAsync() {
		synchronized (threadLock) {
			if (mThread == null) {
				createNewStartStopThread();
			}
			mThread.stopDiscoveryAsync();
		}
	}

	public void stopDiscoveryImmediate() {
		synchronized (threadLock) {
			long startTime = System.currentTimeMillis();
			mSsdpService.stopDeviceDiscovery();
			ALog.i(ALog.SSDPHELPER,
					"Stopping SsdpService took - "
							+ (System.currentTimeMillis() - startTime) + "ms");
		}
	}

	public ArrayList<String> getOnlineDevicesEui64() {
		Set<DeviceModel> devices = mSsdpService.getAliveDeviceList();
		ArrayList<String> onlineEui64s = new ArrayList<String>();
		if (devices == null) {
			ALog.i(ALog.SSDPHELPER, "Ssdp service returned NULL online devices");
			return onlineEui64s;
		}

		for (DeviceModel model : devices) {
			if (model == null || model.getSsdpDevice() == null)
				continue;
			onlineEui64s.add(model.getSsdpDevice().getCppId());
		}
		ALog.i(ALog.SSDPHELPER, "Ssdp service returned " + onlineEui64s.size()
				+ " online devices");
		return onlineEui64s;
	}

	private void createNewStartStopThread() {
		synchronized (threadLock) {
			if (mThread != null)
				return;

			long startTime = System.currentTimeMillis();
			Object startLock = new Object();
			mThread = new SsdpServiceHelperThread(this, startLock,
					getSsdpStopDelay());

			synchronized (startLock) {
				try {
					mThread.start();
					startLock.wait(TIMEOUT_STARTTHREAD);
				} catch (InterruptedException e) {
					ALog.i(ALog.SSDPHELPER, "Interrupted - " + e);
					e.printStackTrace();
				}
			}

			ALog.i(ALog.SSDPHELPER,
					"Starting StartStopThread took "
							+ (System.currentTimeMillis() - startTime) + "ms");
		}
	}

	@Override
	public void startDiscoveryFromHandler() {
		long startTime = System.currentTimeMillis();
		mSsdpService.startDeviceDiscovery(mSsdpCallback);
		DiscoveryManager.getInstance().syncLocalAppliancesWithSsdpStackDelayed();
		ALog.i(ALog.SSDPHELPER,
				"Starting SsdpService took - "
						+ (System.currentTimeMillis() - startTime) + "ms");
	}

	@Override
	public void stopDiscoveryFromHandler() {
		long startTime = System.currentTimeMillis();
		mSsdpService.stopDeviceDiscovery();
		DiscoveryManager.getInstance().cancelSyncLocalAppliancesWithSsdpStack();
		ALog.i(ALog.SSDPHELPER,
				"Stopping SsdpService took - "
						+ (System.currentTimeMillis() - startTime) + "ms");

		synchronized (threadLock) {
			if (mThread.stopIfNecessary()) {
				mThread = null;
				ALog.i(ALog.SSDPHELPER, "Stopping StartStopThread");
			}
		}
	}

	private int getSsdpStopDelay() {
		if (mTestDelay > 0)
			return mTestDelay;
		return DELAY_STOPSSDP;
	}

	// UTILITY METHODS TO ALLOW TESTING
	public boolean testIsThreadAlive() {
		if (mThread == null)
			return false;
		if (mThread.isAlive())
			return true;
		return false;
	}

	public HandlerThread getThreadForTesting() {
		return mThread;
	}

	public boolean noMorePendingMessagesForTesting() {
		synchronized (mThread) {
			return !mThread.hasMessagesOnQueueForTesting();
		}
	}

	public void removePendingMessagesOnQueueForTesting() {
		if (mThread == null)
			return;
		synchronized (mThread) {
			mThread.clearMessagesOnQueueForTesting();
		}
	}

	public void setStopDelayForTesting(int delay) {
		mTestDelay = delay;
	}
}
