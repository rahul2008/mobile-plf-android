package com.philips.cdp.dicommclient.discovery;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.philips.cdp.dicommclient.cpp.CppController;
import com.philips.cdp.dicommclient.cpp.listener.DcsEventListener;
import com.philips.cdp.dicommclient.cpp.listener.PublishEventListener;
import com.philips.cdp.dicommclient.cpp.listener.SignonListener;
import com.philips.cdp.dicommclient.util.DLog;
import com.philips.icpinterface.data.Errors;

public class CppDiscoveryHelper implements SignonListener, PublishEventListener, DcsEventListener {

	private CppController mCppController;
	private CppDiscoverEventListener mCppDiscListener;
	private boolean isCppDiscoveryPending = false;
	private int retrySubscriptionCount ;
	private static final int MAX_RETRY_FOR_DISCOVER = 2 ;
	public static final String DISCOVERY_REQUEST = "DCS-REQUEST";
	public static final String ACTION_DISCOVER = "DISCOVER" ;
	private int discoverEventMessageID ;
	private CppDiscoverEventListener mCppDiscoverEventListener;

	public CppDiscoveryHelper(CppController controller, CppDiscoverEventListener cppDiscListener) {
		mCppController = controller;
		mCppDiscListener = cppDiscListener;
		mCppController.addSignOnListener(this);
		mCppDiscoverEventListener = cppDiscListener;
		mCppController.setDCSDiscoverEventListener(this);
	}

	public void startDiscoveryViaCpp() {
		DLog.d(DLog.CPPDISCHELPER, "Start discovery via CPP");
		boolean isSignedOnToCpp = mCppController.isSignOn();
		startDiscoveryViaCpp(isSignedOnToCpp);
	}

	public void stopDiscoveryViaCpp() {
		DLog.d(DLog.CPPDISCHELPER, "Stop discovery via CPP - disabling subscription");
		isCppDiscoveryPending = false;
		DLog.i(DLog.CPPDISCHELPER, "Disabling remote subscription (stop dcs)");
		mCppController.stopDCSService();
		mCppController.removePublishEventListener(this) ;
	}

	private void startDiscoveryViaCpp(boolean isSignedOnToCpp) {
		if (isSignedOnToCpp) {
			mCppDiscListener.onSignedOnViaCpp();
			DLog.i(DLog.CPPDISCHELPER, "Enabling remote subscription (start dcs)");
			mCppController.startDCSService();
			mCppController.addPublishEventListener(this) ;
			discoverEventMessageID = mCppController.publishEvent(null, DISCOVERY_REQUEST, ACTION_DISCOVER, "", 20, 120, mCppController.getAppCppId());
			isCppDiscoveryPending = false;
			DLog.i(DLog.CPPDISCHELPER, "Starting discovery via Cpp - IMMEDIATE");
		} else {
			isCppDiscoveryPending = true;
			DLog.i(DLog.CPPDISCHELPER, "Starting discovery via Cpp - DELAYED");
		}
	}

	@Override
	public void signonStatus(boolean signon) {
		DLog.d(DLog.CPPDISCHELPER, "Sigon on callback: " + signon);
		if (!signon) {
			DLog.i(DLog.CPPDISCHELPER, "Signed off - Notifying discovery listener");
			mCppDiscListener.onSignedOffViaCpp();
			return;
		}
		if (!isCppDiscoveryPending) return;

		DLog.i(DLog.CPPDISCHELPER, "Signed on - Starting discovery via CPP");
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
			discoverEventMessageID = mCppController.publishEvent(null, DISCOVERY_REQUEST, ACTION_DISCOVER, "", 20, 120, mCppController.getAppCppId());
		}
	}

	@Override
	public void onDCSEventReceived(String data, String fromEui64, String action) {
		DiscoverInfo discoverInfo = parseDiscoverInfo(data);
		if (discoverInfo == null) return;

		DLog.i(DLog.CPPDISCHELPER, "Discovery event received - " + action);
		boolean isResponseToRequest = (action != null	&& action.toUpperCase().trim().equals(ACTION_DISCOVER));

		if (mCppDiscoverEventListener != null) {
			mCppDiscoverEventListener.onDiscoverEventReceived(discoverInfo, isResponseToRequest);
		}
	}

	public static DiscoverInfo parseDiscoverInfo(String dataToParse) {
		if (dataToParse== null || dataToParse.isEmpty()) return null;

		try {
			Gson gson = new GsonBuilder().create();
			DiscoverInfo info =  gson.fromJson(dataToParse, DiscoverInfo.class);

			if (!info.isValid()) return null;
			return info;
		} catch (JsonIOException e) {
			DLog.e(DLog.PARSER, "JsonIOException");
			return null;
		} catch (JsonSyntaxException e2) {
			DLog.e(DLog.PARSER, "JsonSyntaxException");
			return null;
		}
	}
}
