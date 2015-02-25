package com.philips.cl.di.dev.pa.purifier;

import java.net.HttpURLConnection;

import android.content.Context;

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.constant.AppConstants.Port;
import com.philips.cl.di.dev.pa.cpp.CPPController;
import com.philips.cl.di.dev.pa.cpp.DCSEventListener;
import com.philips.cl.di.dev.pa.cpp.PublishEventListener;
import com.philips.cl.di.dev.pa.datamodel.SessionDto;
import com.philips.cl.di.dev.pa.newpurifier.ConnectionState;
import com.philips.cl.di.dev.pa.newpurifier.NetworkNode;
import com.philips.cl.di.dev.pa.security.Util;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.JSONBuilder;
import com.philips.cl.di.dev.pa.util.ServerResponseListener;
import com.philips.cl.di.dev.pa.util.Utils;
import com.philips.icpinterface.data.Errors;

public class SubscriptionHandler implements UDPEventListener, DCSEventListener,
		ServerResponseListener, PublishEventListener {

	private SubscriptionEventListener mSubscriptionEventListener;
	private UDPReceivingThread udpReceivingThread;
	
	private static final int MAX_RETRY_FOR_SUBSCRIPTION = 2;
	private int retrySubscriptionCount;
	
	private int airPortSubscriptionMessageID ;
	
	private NetworkNode mNetworkNode;
	
	public SubscriptionHandler(NetworkNode networkNode, SubscriptionEventListener subscriptionEventListener) {
		udpReceivingThread = new UDPReceivingThread(this);
		mNetworkNode = networkNode;
		mSubscriptionEventListener = subscriptionEventListener;	
		CPPController.getInstance(PurAirApplication.getAppContext()).addDCSEventListener(networkNode.getCppId(), this);
	}


	public void subscribeToPurifierEvents() {
		retrySubscriptionCount = 1 ;
		ALog.d(ALog.SUBSCRIPTION,
				"Subscribing to Appliance events for: " + mNetworkNode);
		if (mNetworkNode == null)
			return;

		final String portUrl = Utils.getPortUrl(Port.AIR,
				mNetworkNode.getIpAddress());
		subscribe(portUrl, mNetworkNode);
	}

	public void unSubscribeFromPurifierEvents() {
		ALog.d(ALog.SUBSCRIPTION,
				"Unsubscribing to Appliance events for : " + mNetworkNode);
		if (mNetworkNode == null)
			return;
		String portUrl = Utils.getPortUrl(Port.AIR, mNetworkNode.getIpAddress());
		unSubscribe(portUrl, mNetworkNode);
	}

	public void subscribeToFirmwareEvents() {
		if (mNetworkNode == null || PurAirApplication.isDemoModeEnable())
			return;

		boolean isLocalSubscription = mNetworkNode.getConnectionState().equals(
				ConnectionState.CONNECTED_LOCALLY);
		if (isLocalSubscription) {
			ALog.d(ALog.SUBSCRIPTION,
					"Subscribing to Firmware events for : " + mNetworkNode);
			final String portUrl = Utils.getPortUrl(Port.FIRMWARE,
					mNetworkNode.getIpAddress());
			subscribe(portUrl, mNetworkNode);
		}
	}

	public void unSubscribeFromFirmwareEvents() {
		if (mNetworkNode == null)
			return;
		ALog.d(ALog.SUBSCRIPTION,
				"Unsubscribing from Firmware events for: " + mNetworkNode);
		String portUrl = Utils.getPortUrl(Port.FIRMWARE,
				mNetworkNode.getIpAddress());
		unSubscribe(portUrl, mNetworkNode);
	}

	public void enableLocalSubscription() {
		ALog.i(ALog.SUBSCRIPTION, "Enabling local subscription (start udp)");
		if (udpReceivingThread == null) {
			udpReceivingThread = new UDPReceivingThread(this);
		}
		if (udpReceivingThread != null && !udpReceivingThread.isAlive()) {
			udpReceivingThread.start();
		}
	}

	public void disableLocalSubscription() {
		ALog.i(ALog.SUBSCRIPTION, "Disabling local subscription (stop udp)");
		if (udpReceivingThread != null && udpReceivingThread.isAlive()) {
			udpReceivingThread.stopUDPListener();
			udpReceivingThread = null;
		}
	}

	public void enableRemoteSubscription(Context context) {
		ALog.i(ALog.SUBSCRIPTION, "Enabling remote subscription (start dcs)");
		CPPController.getInstance(context).startDCSService();
	}

	public void disableRemoteSubscription(Context context) {
		ALog.i(ALog.SUBSCRIPTION, "Disabling remote subscription (stop dcs)");
		CPPController.getInstance(context).stopDCSService();
	}

	private void subscribe(String url, NetworkNode networkNode) {
		
		boolean isLocal = networkNode.getConnectionState().equals(
				ConnectionState.CONNECTED_LOCALLY);
		String subscriberId = getSubscriberId(isLocal);
		ALog.d(ALog.SUBSCRIPTION, "SubscriptionManager$subscribe bootId "
				+ networkNode.getBootId() + " URL " + url + " isLocal " + isLocal);
		if (isLocal) {
			String dataToUpload = JSONBuilder
					.getDICommBuilderForSubscribe(subscriberId,
							AppConstants.LOCAL_SUBSCRIPTIONTIME, networkNode);
			if (dataToUpload == null)
				return;

			LocalSubscription subscribe = new LocalSubscription(dataToUpload,
					this, AppConstants.REQUEST_METHOD_POST);
			subscribe.execute(url);
		} else {
			if (PurAirApplication.isDemoModeEnable())
				return;
			CPPController.getInstance(PurAirApplication.getAppContext()).addPublishEventListener(this) ;
			airPortSubscriptionMessageID = CPPController.getInstance(PurAirApplication.getAppContext())
					.publishEvent(
							JSONBuilder.getPublishEventBuilderForSubscribe(
									AppConstants.EVENTSUBSCRIBER_KEY,
									subscriberId),
							AppConstants.DI_COMM_REQUEST,
							AppConstants.SUBSCRIBE, subscriberId, "", 20,
							AppConstants.CPP_SUBSCRIPTIONTIME,
							networkNode.getCppId());

			CPPController
					.getInstance(PurAirApplication.getAppContext())
					.publishEvent(
							JSONBuilder.getPublishEventBuilderForSubscribeFirmware(
									AppConstants.EVENTSUBSCRIBER_KEY,
									subscriberId),
							AppConstants.DI_COMM_REQUEST,
							AppConstants.SUBSCRIBE, subscriberId, "", 20,
							AppConstants.CPP_SUBSCRIPTIONTIME,
							networkNode.getCppId());
		}

	}

	private void unSubscribe(String url, NetworkNode networkNode) {
		boolean isLocal = networkNode.getConnectionState().equals(
				ConnectionState.CONNECTED_LOCALLY);
		String subscriberId = getSubscriberId(isLocal);
		if (isLocal) {
			// LocalSubscription unSubscribe = new
			// LocalSubscription(null,this,AppConstants.REQUEST_METHOD_DELETE) ;
			// unSubscribe.execute(url) ;
			ALog.i(ALog.SUBSCRIPTION, "TODO Unsubscribe");
		} else {
			CPPController.getInstance(PurAirApplication.getAppContext())
					.publishEvent(
							JSONBuilder.getPublishEventBuilderForSubscribe(
									AppConstants.EVENTSUBSCRIBER_KEY,
									subscriberId),
							AppConstants.DI_COMM_REQUEST,
							AppConstants.UNSUBSCRIBE, subscriberId, "", 20,
							AppConstants.CPP_SUBSCRIPTIONTIME,
							networkNode.getCppId());

			CPPController
					.getInstance(PurAirApplication.getAppContext())
					.publishEvent(
							JSONBuilder.getPublishEventBuilderForSubscribeFirmware(
									AppConstants.EVENTSUBSCRIBER_KEY,
									subscriberId),
							AppConstants.DI_COMM_REQUEST,
							AppConstants.UNSUBSCRIBE, subscriberId, "", 20,
							AppConstants.CPP_SUBSCRIPTIONTIME,
							networkNode.getCppId());
		}
	}

	private String getSubscriberId(boolean isLocal) {
		String appEui64 = SessionDto.getInstance().getAppEui64();
		if (appEui64 != null)
			return appEui64;
		if (isLocal)
			return Util.getBootStrapID(); // Fallback for local subscription
											// when no cpp connection
		return null;
	}

	@Override
	public void onUDPEventReceived(String data, String fromIp) {
		if (data == null || data.isEmpty())
			return;
		if (fromIp == null || fromIp.isEmpty())
			return;

		ALog.i(ALog.SUBSCRIPTION, "UDP event received: " + fromIp);
		ALog.d(ALog.SUBSCRIPTION, data);
		if (mSubscriptionEventListener != null) {
			mSubscriptionEventListener.onLocalEventReceived(data, fromIp);
		}
	}

	@Override
	public void onDCSEventReceived(String data, String fromEui64, String action) {
		if (data == null || data.isEmpty())
			return;

		if (fromEui64 == null || fromEui64.isEmpty())
			return;
		ALog.i(ALog.SUBSCRIPTION, "DCS event received from " + fromEui64);
		ALog.i(ALog.SUBSCRIPTION, data);
		if (mSubscriptionEventListener != null) {
			mSubscriptionEventListener.onRemoteEventReceived(data, fromEui64);
		}
	}

	@Override
	public void receiveServerResponse(int responseCode, String responseData, String fromIp) {
		// TODO if response code not 200? retry?
		if (responseCode != HttpURLConnection.HTTP_OK) {
			ALog.i(ALog.SUBSCRIPTION, "Subscription failed");
			ALog.d(ALog.SUBSCRIPTION, "ReponseCode:  " + responseCode
					+ "   source Ip: " + fromIp);
			ALog.d(ALog.SUBSCRIPTION, "ReponseData:  " + responseData);
			subscribeToPurifierEvents() ;
			return;
		}

		ALog.i(ALog.SUBSCRIPTION, "Subscription successfull");
		onUDPEventReceived(responseData, fromIp); // Response already contains
													// first subscription
													// events, treat as UDP
		// TODO fix this
	}
	
	@Override
	public void receiveServerResponse(int responseCode, String responseData, String type, String areaId) {/**NOP*/}

//	public static void setDummySubscriptionManagerForTesting(
//			SubscriptionHandler dummyManager) {
//		mInstance = dummyManager;
//	}

	@Override
	public void onPublishEventReceived(int status, int messageId) {
		if( status == Errors.SUCCESS) {
			return;
		}
		if( retrySubscriptionCount > MAX_RETRY_FOR_SUBSCRIPTION ) {
			retrySubscriptionCount = 1 ;
			return ;
		}
		String subscriberId = getSubscriberId(false) ;
		if( airPortSubscriptionMessageID == messageId) {
			retrySubscriptionCount ++ ;
			airPortSubscriptionMessageID = CPPController.getInstance(PurAirApplication.getAppContext())
					.publishEvent(
							JSONBuilder.getPublishEventBuilderForSubscribe(
									AppConstants.EVENTSUBSCRIBER_KEY,
									subscriberId),
							AppConstants.DI_COMM_REQUEST,
							AppConstants.SUBSCRIBE, subscriberId, "", 20,
							AppConstants.CPP_SUBSCRIPTIONTIME,
							mNetworkNode.getCppId());
		}
	}
}
