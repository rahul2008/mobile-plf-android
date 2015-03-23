package com.philips.cl.di.dev.pa.cpp;

import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.icpinterface.data.Errors;

public class CppDiscoveryHelper implements SignonListener, PublishEventListener {
	
	private CPPController mCppController;
	private CppDiscoverEventListener mCppDiscListener;
	private boolean isCppDiscoveryPending = false;
	private int retrySubscriptionCount ;
	private static final int MAX_RETRY_FOR_DISCOVER = 2 ;
	private int discoverEventMessageID ;
	
	public CppDiscoveryHelper(CPPController controller, CppDiscoverEventListener cppDiscListener) {
		mCppController = controller;
		mCppDiscListener = cppDiscListener;
		mCppController.addSignOnListener(this);
		mCppController.setCppDiscoverEventListener(mCppDiscListener);
	}
	
	public void startDiscoveryViaCpp() {
		ALog.d(ALog.CPPDISCHELPER, "Start discovery via CPP");
		boolean isSignedOnToCpp = mCppController.isSignOn();
		startDiscoveryViaCpp(isSignedOnToCpp);
	}

	public void stopDiscoveryViaCpp() {
		ALog.d(ALog.CPPDISCHELPER, "Stop discovery via CPP - disabling subscription");
		isCppDiscoveryPending = false;
		ALog.i(ALog.CPPDISCHELPER, "Disabling remote subscription (stop dcs)");
		mCppController.stopDCSService();
		mCppController.removePublishEventListener(this) ;
	}
	
	private void startDiscoveryViaCpp(boolean isSignedOnToCpp) {
		if (isSignedOnToCpp) {
			mCppDiscListener.onSignedOnViaCpp();
			ALog.i(ALog.CPPDISCHELPER, "Enabling remote subscription (start dcs)");
			mCppController.startDCSService();
			mCppController.addPublishEventListener(this) ;
			discoverEventMessageID = mCppController.publishEvent(null, AppConstants.DISCOVERY_REQUEST, AppConstants.DISCOVER, "", 20, 120, mCppController.getAppCppId());
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

	
	@Override
	public void onPublishEventReceived(int status, int messageId, String conversationId) {
		if( status != Errors.SUCCESS) {
			return;
		}
		if( retrySubscriptionCount > MAX_RETRY_FOR_DISCOVER ) {
			retrySubscriptionCount = 1 ;			
		}
		else if(discoverEventMessageID == messageId) {
			retrySubscriptionCount ++ ;
			discoverEventMessageID = mCppController.publishEvent(null, AppConstants.DISCOVERY_REQUEST, AppConstants.DISCOVER, "", 20, 120, mCppController.getAppCppId());
		}
	}
}
