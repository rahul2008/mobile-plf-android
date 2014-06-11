package com.philips.cl.di.dev.pa.newpurifier;

import android.os.Handler.Callback;
import android.os.HandlerThread;

import com.philips.cl.di.common.ssdp.lib.SsdpService;
import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.cpp.CPPController;
import com.philips.cl.di.dev.pa.cpp.SignonListener;
import com.philips.cl.di.dev.pa.datamodel.SessionDto;
import com.philips.cl.di.dev.pa.newpurifier.SsdpServiceHelperThread.StartStopInterface;
import com.philips.cl.di.dev.pa.purifier.SubscriptionHandler;
import com.philips.cl.di.dev.pa.util.ALog;

public class SsdpServiceHelper implements StartStopInterface, SignonListener {
	
	private SsdpService mSsdpService = null;
	private SubscriptionHandler mSubHandler = null;
	private CPPController mCppController = null;
	private Callback mSsdpCallback = null;
	
	private Object threadLock = new Object();
	private SsdpServiceHelperThread mThread = null;
	
	private boolean isCppDiscoveryPending = false;
	
	private static final int TIMEOUT_STARTTHREAD = 3000;
	private static final int DELAY_STOPSSDP = 3000;
	
	private int mTestDelay = -1;
	
	public SsdpServiceHelper(SsdpService ssdpService, SubscriptionHandler subHandler, CPPController cppController, Callback callback) {
		mSsdpService = ssdpService;
		mSubHandler = subHandler;
		mCppController = cppController;
		mSsdpCallback = callback;
		mCppController.addSignOnListener(this);
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
	
	private void createNewStartStopThread() {
		synchronized (threadLock) {
			if (mThread != null) return;
			
			long startTime = System.currentTimeMillis();
			Object startLock = new Object();
			mThread = new SsdpServiceHelperThread(this, startLock, getSsdpStopDelay());
			
			synchronized (startLock) {
				try {
					mThread.start();
					startLock.wait(TIMEOUT_STARTTHREAD);
				} catch (InterruptedException e) {
					ALog.i(ALog.SSDPHELPER, "Interrupted - " + e);
					e.printStackTrace();
				}
			}
			
			ALog.i(ALog.SSDPHELPER, "Starting StartStopThread took " + (System.currentTimeMillis() - startTime) + "ms");
		}
	}

	@Override
	public void startDiscoveryFromHandler() {
		long startTime = System.currentTimeMillis();
		mSsdpService.startDeviceDiscovery(mSsdpCallback);
		
		//TODO is there a better place for this code?
		startDiscoveryViaCpp(mCppController.isSignOn());
		
		ALog.i(ALog.SSDPHELPER, "Starting SsdpService took - " + (System.currentTimeMillis() - startTime) + "ms");
	}

	@Override
	public void stopDiscoveryFromHandler() {
		long startTime = System.currentTimeMillis();
		mSsdpService.stopDeviceDiscovery();
		
		//TODO is there a better place for this code?
		stopDiscoveryViaCpp();
		
		ALog.i(ALog.SSDPHELPER, "Stopping SsdpService took - " + (System.currentTimeMillis() - startTime) + "ms");
		
		synchronized (threadLock) {
			if (mThread.stopIfNecessary()) {
				mThread = null;
				ALog.i(ALog.SSDPHELPER, "Stopping StartStopThread");
			}
		}
	}
	
	private void startDiscoveryViaCpp(boolean isSignedOnToCpp) {
		//TODO is there a better place for this code?
		if (isSignedOnToCpp) {
			mSubHandler.enableRemoteSubscription(PurAirApplication.getAppContext());
			mCppController.publishEvent(null,AppConstants.DISCOVERY_REQUEST, AppConstants.DISCOVER, SessionDto.getInstance().getAppEui64(), "", 20, 120, SessionDto.getInstance().getAppEui64());
			isCppDiscoveryPending = false;
			ALog.d(ALog.SSDPHELPER, "Starting discovery via Cpp - IMMEDIATE");
		} else {
			isCppDiscoveryPending = true;
			ALog.d(ALog.SSDPHELPER, "Starting discovery via Cpp - DELAYED");
		}
	}

	private void stopDiscoveryViaCpp() {
		isCppDiscoveryPending = false;
		mSubHandler.disableRemoteSubscription(PurAirApplication.getAppContext());
	}
	
	private int getSsdpStopDelay() {
		if (mTestDelay > 0) return mTestDelay;
		return DELAY_STOPSSDP;
	}
	

	@Override
	public void signonStatus(boolean signon) {
		if (!signon) return;
		if (!isCppDiscoveryPending) return;
		
		ALog.d(ALog.SSDPHELPER, "Signed on - Starting discovery via CPP");
		startDiscoveryViaCpp(signon);
	}
	
	// UTILITY METHODS TO ALLOW TESTING
	public boolean testIsThreadAlive() {
		if (mThread == null) return false;
		if (mThread.isAlive()) return true;
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
		if (mThread == null) return;
		synchronized (mThread) {
			mThread.clearMessagesOnQueueForTesting();
		}
	}
	
	public void setStopDelayForTesting(int delay) {
		mTestDelay = delay;
	}

	public boolean getCppDiscoveryPendingForTesting() {
		return isCppDiscoveryPending;
	}
}
