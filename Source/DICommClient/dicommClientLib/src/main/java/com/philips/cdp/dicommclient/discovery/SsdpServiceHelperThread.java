/*
 * © Koninklijke Philips N.V., 2015.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.discovery;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import com.philips.cdp.dicommclient.util.DICommLog;

public class SsdpServiceHelperThread extends HandlerThread {

	private Object mStartLock;
	private StartStopInterface mInterface;
	private Handler mStartStopHandler;
	private boolean isProcessingMessage = false;

	private static final int MESSAGE_START = 14007;
	private static final int MESSAGE_STOP = 14008;

	private int mStopSsdpDelay;

	public SsdpServiceHelperThread(StartStopInterface startStopInterface,
			Object startLock, int stopSsdpDelay) {
		super("ssdpservicehelperthread");
		mInterface = startStopInterface;
		mStartLock = startLock;
		mStopSsdpDelay = stopSsdpDelay;
	}

	@Override
	protected void onLooperPrepared() {
		mStartStopHandler = new Handler(getLooper()) {
			public void handleMessage(Message msg) {
				isProcessingMessage = true;

				switch (msg.what) {
				case MESSAGE_START:
					mInterface.startDiscoveryFromHandler();
					break;
				case MESSAGE_STOP:
					mInterface.stopDiscoveryFromHandler();
					break;
				default: /* NOP */
					break;
				}

				isProcessingMessage = false;
			}
		};

		synchronized (mStartLock) {
			mStartLock.notifyAll();
		}
		DICommLog.v(DICommLog.SSDPHELPER, "StartStopThread started running");
		super.onLooperPrepared();
	}

	public void startDiscoveryAsync() {
		if (mStartStopHandler.hasMessages(MESSAGE_STOP)) {
			mStartStopHandler.removeMessages(MESSAGE_STOP);
			DICommLog.v(DICommLog.SSDPHELPER, "Removed pending stop messages");
		}
		if (!mStartStopHandler.hasMessages(MESSAGE_START)) {
			mStartStopHandler.sendEmptyMessage(MESSAGE_START);
			DICommLog.d(DICommLog.SSDPHELPER, "Added start message");
		}
		DICommLog.v(DICommLog.SSDPHELPER, "No need to add start message");
	}

	public void stopDiscoveryAsync() {
		if (mStartStopHandler.hasMessages(MESSAGE_START)) {
			mStartStopHandler.removeMessages(MESSAGE_START);
			DICommLog.v(DICommLog.SSDPHELPER, "Removed pending start messages");
		}
		if (!mStartStopHandler.hasMessages(MESSAGE_STOP)) {
			mStartStopHandler.sendEmptyMessageDelayed(MESSAGE_STOP,
					mStopSsdpDelay);
			DICommLog.d(DICommLog.SSDPHELPER, "Added stop message");
		}
		DICommLog.v(DICommLog.SSDPHELPER, "No need to add stop message");
	}

	public boolean stopIfNecessary() {
		if (mStartStopHandler.hasMessages(MESSAGE_START))
			return false;

		mStartStopHandler.removeMessages(MESSAGE_STOP);
		return this.quit();
	}

	public boolean hasMessagesOnQueueForTesting() {
		return mStartStopHandler.hasMessages(MESSAGE_START)
				|| mStartStopHandler.hasMessages(MESSAGE_STOP)
				|| isProcessingMessage;
	}

	public void clearMessagesOnQueueForTesting() {
		mStartStopHandler.removeMessages(MESSAGE_START);
		mStartStopHandler.removeMessages(MESSAGE_STOP);
	}

	public interface StartStopInterface {
		public void startDiscoveryFromHandler();

		public void stopDiscoveryFromHandler();
	}
}
