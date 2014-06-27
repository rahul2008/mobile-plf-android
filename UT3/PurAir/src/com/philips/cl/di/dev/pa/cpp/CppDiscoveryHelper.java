package com.philips.cl.di.dev.pa.cpp;

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.datamodel.SessionDto;
import com.philips.cl.di.dev.pa.purifier.SubscriptionHandler;
import com.philips.cl.di.dev.pa.util.ALog;

public class CppDiscoveryHelper implements SignonListener {
	
	private CPPController mCppController;
	private SubscriptionHandler mSubHandler;
	private CppDiscoverEventListener mCppDiscListener;
	private boolean isCppDiscoveryPending = false;
	
	public CppDiscoveryHelper(CPPController controller, SubscriptionHandler subHandler, CppDiscoverEventListener cppDiscListener) {
		mCppController = controller;
		mSubHandler = subHandler;
		mCppDiscListener = cppDiscListener;
		mCppController.addSignOnListener(this);
		mSubHandler.setCppDiscoverListener(mCppDiscListener);
	}
	
	public void startDiscoveryViaCpp() {
		ALog.d(ALog.CPPDISCHELPER, "Start discovery via CPP");
		boolean isSignedOnToCpp = mCppController.isSignOn();
		startDiscoveryViaCpp(isSignedOnToCpp);
	}

	public void stopDiscoveryViaCpp() {
		ALog.d(ALog.CPPDISCHELPER, "Stop discovery via CPP - disabling subscription");
		isCppDiscoveryPending = false;
		mSubHandler.disableRemoteSubscription(PurAirApplication.getAppContext());
	}
	
	private void startDiscoveryViaCpp(boolean isSignedOnToCpp) {
		if (isSignedOnToCpp) {
			mCppDiscListener.onSignedOnViaCpp();
			mSubHandler.enableRemoteSubscription(PurAirApplication.getAppContext());
			mCppController.publishEvent(null, AppConstants.DISCOVERY_REQUEST, AppConstants.DISCOVER, SessionDto.getInstance()
							.getAppEui64(), "", 20, 120, SessionDto.getInstance().getAppEui64());
			isCppDiscoveryPending = false;
			ALog.i(ALog.CPPDISCHELPER, "Starting discovery via Cpp - IMMEDIATE");
		} else {
			isCppDiscoveryPending = true;
			ALog.i(ALog.CPPDISCHELPER, "Starting discovery via Cpp - DELAYED");
		}
	}
	
	@Override
	public void signonStatus(boolean signon) {
		ALog.d(ALog.CPPDISCHELPER, "Sigon on callback: " + signon);
		if (!signon) {
			ALog.i(ALog.CPPDISCHELPER, "Signed off - Notifying discovery listener");
			mCppDiscListener.onSignedOffViaCpp();
			return;
		}
		if (!isCppDiscoveryPending) return;
		
		ALog.i(ALog.CPPDISCHELPER, "Signed on - Starting discovery via CPP");
		startDiscoveryViaCpp(signon);
	}
	
	// UTILITY METHODS TO ALLOW TESTING
	public boolean getCppDiscoveryPendingForTesting() {
		return isCppDiscoveryPending;
	}
}
